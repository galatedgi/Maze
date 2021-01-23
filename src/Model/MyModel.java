package Model;

import Client.*;
import IO.MyDecompressorInputStream;
import Server.*;
import test.*;
import algorithms.mazeGenerators.EmptyMazeGenerator;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import test.RunCommunicateWithServers;

import javax.security.auth.login.Configuration;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Aviadjo on 6/14/2017.
 */
public class MyModel extends Observable implements IModel {

    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private Server mazeGeneratingServer;
    private Server solveSearchProblemServer;
    private Maze maze;
    private int characterPositionRow ;
    private int characterPositionColumn ;
    private int endMazeRow;
    private int endMazecol;
    int[][] sol;
    //  private int width ,height;

    public MyModel() {
        mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        //Raise the servers
    }

    public void startServers() {
        mazeGeneratingServer.start();
        solveSearchProblemServer.start();

    }

    public void stopServers() {
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();

    }



    @Override
    public void generateMaze(int width, int height) {
        //Generate maze
       /* threadPool.execute(() -> {
            //generateRandomMaze(width,height);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setChanged();
            notifyObservers();
        });*/
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{height, width};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[100000 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        maze = new Maze(decompressedMaze);
                        characterPositionRow=maze.getStartPosition().getRowIndex();
                        characterPositionColumn=maze.getStartPosition().getColumnIndex();
                        endMazecol=maze.getGoalPosition().getColumnIndex();
                        endMazeRow=maze.getGoalPosition().getRowIndex();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        maze.print();//למחוק
        setChanged();
        notifyObservers(0);

    }



    @Override
    public int[][] getMaze() {
            int[][] intMaze=new int[maze.getSizeRows()][maze.getSizeCols()];
        for(int i=0;i<intMaze.length;i++) {
            for (int j = 0; j < intMaze[0].length; j++) {
                intMaze[i][j] = maze.getValue(i, j);
            }
        }
        return intMaze;
    }

    @Override
    public void moveCharacter(KeyCode movement) {
        switch (movement) {
            case NUMPAD8:
                if(canMove(characterPositionRow-1,characterPositionColumn))
                  characterPositionRow--;
                break;
            case NUMPAD2:
                if(canMove(characterPositionRow+1,characterPositionColumn))
                    characterPositionRow++;
                break;
            case NUMPAD6:
                if(canMove(characterPositionRow,characterPositionColumn+1))
                    characterPositionColumn++;
                break;
            case NUMPAD4:
                if(canMove(characterPositionRow,characterPositionColumn-1))
                    characterPositionColumn--;
                break;
            case NUMPAD9:
                if(canMove(characterPositionRow-1,characterPositionColumn+1)){
                    characterPositionRow--;
                    characterPositionColumn++;
                }
                break;
            case NUMPAD7:
                if(canMove(characterPositionRow-1,characterPositionColumn-1)){
                    characterPositionRow--;
                    characterPositionColumn--;
                }
                break;
            case NUMPAD1:
                if(canMove(characterPositionRow+1,characterPositionColumn-1)){
                    characterPositionRow++;
                    characterPositionColumn--;
                }
                break;
            case NUMPAD3:
                if(canMove(characterPositionRow+1,characterPositionColumn+1)){
                    characterPositionRow++;
                    characterPositionColumn++;
                }
                break;
        }
        setChanged();
        notifyObservers(0);
    }

    @Override
    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    @Override
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    private boolean canMove(int r,int c){
        if(r>maze.getSizeRows())
            return false;
        if(c>maze.getSizeCols())
            return false;
        if(r<0)
            return false;
        if(c<0)
            return false;
        if(maze.getValue(r,c)==1)
            return false;
        return true;
    }

    public int getEndMazeRow() {
        return endMazeRow;
    }

    public int getEndMazecol() {
        return endMazecol;
    }
    public void solveMaze(){
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze);
                        toServer.flush();
                        Solution mazeSolution = (Solution)fromServer.readObject();
                        ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();
                        sol=new int[mazeSolutionSteps.size()][2];
                        for(int i = 0; i < mazeSolutionSteps.size(); ++i) {
                            sol[i][0]=((Position)(((MazeState)mazeSolutionSteps.get(i)).getData())).getRowIndex();
                            sol[i][1]=((Position)(((MazeState)mazeSolutionSteps.get(i)).getData())).getColumnIndex();
                        }
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }

                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }
        setChanged();
        notifyObservers(1);
    }

    public int[][] getSol() {
        return sol;
    }

    public void Save(String name) {
        try {
            String path = "./resources/SavedGames/" + name + ".maze";
            if(existingfile(name)){
                throw new IllegalArgumentException();
            }
            FileOutputStream FOSS = new FileOutputStream(path);
            ObjectOutputStream OOSS = new ObjectOutputStream(FOSS);
            Object[] write=new Object[3];
            write[0]=maze;
            write[1]=characterPositionRow;
            write[2]=characterPositionColumn;
            OOSS.writeObject(write);
            FOSS.close();
            OOSS.close();
            setChanged();
            notifyObservers(3);

        }
        catch (Exception e){
            throw new IllegalArgumentException();
        };

    }

    public void Load(String name) {
        try {
            FileInputStream FIS = new FileInputStream( "./resources/SavedGames/" + name );
            ObjectInputStream OIS = new ObjectInputStream(FIS);
            Object[] load=(Object[])OIS.readObject();
            maze=(Maze)load[0];
            characterPositionRow=(int)load[1];
            characterPositionColumn=(int)load[2];
            endMazecol=maze.getGoalPosition().getColumnIndex();
            endMazeRow=maze.getGoalPosition().getRowIndex();
            setChanged();
            notifyObservers(0);


            //Solution result = (Solution) OIS.readObject();
        }catch (Exception e){
            e.getStackTrace();}
    }

    private Boolean existingfile(String name) {
        String tempDir = "./resources/SavedGames/" ;
        File folder = new File(tempDir);
        File[] listOFFiles = folder.listFiles();
        File[] files = listOFFiles;

        for(int i = 0; i < listOFFiles.length; ++i) {
            File currFile = files[i];
            if(currFile.getName().equals(name + ".maze"))
                return true;
        }

        return false;
    }

    public ArrayList<String> getFiles(){
        ArrayList<String> filesNames=new ArrayList<>();
        String tempDir = "./resources/SavedGames/" ;
        File folder = new File(tempDir);
        File[] listOFFiles = folder.listFiles();
        File[] files = listOFFiles;

        for(int i = 0; i < listOFFiles.length; ++i) {
            File currFile = files[i];
            filesNames.add(currFile.getName());
        }
        return filesNames;
    }

    public static void setConfig(String algo,String generator){
        algo=algo.replaceAll(" ","");
        generator=generator.replaceAll( " ","");
        Configurations.setAlgo(algo);
        Configurations.setGenerator(generator);
    }
}
