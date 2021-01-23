package Model;

import javafx.scene.input.KeyCode;

import java.util.ArrayList;

/**
 * Created by Aviadjo on 6/14/2017.
 */
public interface IModel {
    void generateMaze(int width, int height);
    void moveCharacter(KeyCode movement);
    int[][] getMaze();
    int getCharacterPositionRow();
    int getCharacterPositionColumn();
     int getEndMazeRow();
     int getEndMazecol() ;
     void solveMaze();
    int[][] getSol();
    void Save(String name);
    void Load(String name);
    ArrayList<String> getFiles();
    void stopServers();
}
