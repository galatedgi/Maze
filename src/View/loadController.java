package View;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Observable;

public class loadController extends Observable {
    public javafx.scene.control.ChoiceBox choiceBox_files;
    public javafx.scene.control.Button btn_load;
    public javafx.scene.control.Button btn_cancel;

    public void setFiles(ArrayList<String> files){
        choiceBox_files.setItems(FXCollections.observableArrayList(files));
    }

    public void load(ActionEvent actionEvent){
        try {
            String select = (String) choiceBox_files.getValue();
            if(select==null)
                throw new IllegalArgumentException();
            setChanged();
            notifyObservers(select);
            ((Stage)(btn_cancel.getScene().getWindow())).close();
        }
        catch (Exception e){
            showAlert("Please choose game to load");
        }
    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void close(ActionEvent actionEvent){
        ((Stage)(btn_cancel.getScene().getWindow())).close();
    }
}
