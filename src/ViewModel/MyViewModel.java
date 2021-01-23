package ViewModel;

import Model.IModel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Aviadjo on 6/14/2017.
 */
public class MyViewModel extends Observable implements Observer {

    private IModel model;

    private int characterPositionRowIndex;
    private int characterPositionColumnIndex;
    private int endMazeRow;
    private int endMazecol;
    private int[][] sol;
    public StringProperty characterPositionRow = new SimpleStringProperty("1"); //For Binding
    public StringProperty characterPositionColumn = new SimpleStringProperty("1"); //For Binding

    public MyViewModel(IModel model){
        this.model = model;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o==model){
            if((int)arg==0) {
                characterPositionRowIndex = model.getCharacterPositionRow();
                characterPositionRow.set(characterPositionRowIndex + "");
                characterPositionColumnIndex = model.getCharacterPositionColumn();
                characterPositionColumn.set(characterPositionColumnIndex + "");
                endMazecol = model.getEndMazecol();
                endMazeRow = model.getEndMazeRow();
                if(endMazeRow==characterPositionRowIndex&&endMazecol==characterPositionColumnIndex){
                    setChanged();
                    notifyObservers(2);
                    return;
                }
                setChanged();
                notifyObservers(0);
            }
            else if((int)arg==1){
                sol=model.getSol();
                setChanged();
                notifyObservers(1);
            }
            else if((int)arg==3){
                setChanged();
                notifyObservers(3);
            }
        }
    }

    public void generateMaze(int width, int height){
        model.generateMaze(width, height);
    }

    public void moveCharacter(KeyCode movement){
        model.moveCharacter(movement);
    }

    public int[][] getMaze() {
        return model.getMaze();
    }

    public int getCharacterPositionRow() {
        return characterPositionRowIndex;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumnIndex;
    }

    //---------------------//

    public int getEndMazeRow() {
        return endMazeRow;
    }

    public int getEndMazecol() {
        return endMazecol;
    }

    public void solveMaze(){
        model.solveMaze();
    }

    public int[][] getSol(){
        return sol;
    }

    public void Save(String name){
        model.Save(name);

    }

    public ArrayList<String> getFiles(){
        return model.getFiles();
    }

    public void load(String select){
        model.Load(select);
    }


}
