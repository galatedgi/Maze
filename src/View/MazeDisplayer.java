package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Aviadjo on 3/9/2017.
 */
public class MazeDisplayer extends Canvas {

    private int[][] maze;
    private int characterPositionRow = 1;
    private int characterPositionColumn = 1;
    private int endMazeRow;
    private int endMazecol;
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();
    private StringProperty ImageEndMaze=new SimpleStringProperty();
    private StringProperty ImageSolMaze=new SimpleStringProperty();
    private StringProperty ImageBackground=new SimpleStringProperty();
    private int[][] sol=null;
    private Boolean New=false;



    public MazeDisplayer(){
        widthProperty().addListener(e->redraw());
        heightProperty().addListener(e->redraw());
    }

    public void setMaze(int[][] maze) {
        this.maze = maze;
        redraw();
    }

    public void setCharacterPosition(int row, int column) {
        characterPositionRow = row;
        characterPositionColumn = column;
        redraw();
    }

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    public void redraw() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / (maze.length+2);
            double cellWidth = canvasWidth / (maze[0].length+2);

            try {
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
                Image endMaze=new Image(new FileInputStream(ImageEndMaze.get()));
                Image solMaze=new Image(new FileInputStream(ImageSolMaze.get()));
                Image background=new Image(new FileInputStream(ImageBackground.get()));

                GraphicsContext gc = getGraphicsContext2D();
                //gc.drawImage(background,0,0);
                gc.clearRect(0, 0, getWidth(), getHeight());
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, getWidth(), getHeight());
               // gc.drawImage(background,0,0);

                //gc.fillRect(0 * cellHeight, 8 * cellWidth, cellHeight, cellWidth);
                //Draw Maze
                for (int i = 0; i < maze.length; i++) {
                    for (int j = 0; j < maze[i].length; j++) {
                        if (maze[i][j] == 1) {
                            gc.fillRect(j * cellWidth,i * cellHeight,   cellWidth,cellHeight);
                            gc.drawImage(wallImage, j * cellWidth,i * cellHeight,   cellWidth,cellHeight);
                        }
                    }
                }

                //Draw Character
                //gc.setFill(Color.RED);
                //gc.fillOval(characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
                if(this.sol!=null){
                    for(int i=0;i<sol.length;i++){
                        gc.drawImage(solMaze, sol[i][1] * cellWidth, sol[i][0]* cellHeight, cellWidth, cellHeight);
                    }
                }
                gc.drawImage(characterImage, characterPositionColumn * cellWidth, characterPositionRow * cellHeight, cellWidth, cellHeight);
                gc.drawImage(endMaze, endMazecol * cellWidth, endMazeRow * cellHeight, cellWidth, cellHeight);

            } catch (FileNotFoundException e) {
                //e.printStackTrace();
            }
        }
        else{
            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, getWidth(), getHeight());
        }
    }


    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }
    //endregion

    public void setEndMazeRow(int endMazeRow) {
        this.endMazeRow = endMazeRow;
    }

    public void setEndMazecol(int endMazecol) {
        this.endMazecol = endMazecol;
    }

    public String getImageEndMaze() {
        return ImageEndMaze.get();
    }

    public void setImageEndMaze(String imageEndMaze) {
        this.ImageEndMaze.set(imageEndMaze);
    }

    public void setsol(int[][] sol){
        this.sol=sol;
        redraw();
    }

    public void New(){
        maze=null;
        redraw();
    }

    public String getImageSolMaze() {
        return ImageSolMaze.get();
    }


    public void setImageSolMaze(String imageSolMaze) {
        this.ImageSolMaze.set(imageSolMaze);
    }

    public String getImageBackground() {
        return ImageBackground.get();
    }


    public void setImageBackground(String imageBackground) {
        this.ImageBackground.set(imageBackground);
    }
}
