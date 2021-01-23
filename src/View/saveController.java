package View;
import javafx.beans.InvalidationListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.FileInputStream;
import java.util.Observable;

public class saveController extends Observable {
    public javafx.scene.control.TextField txtfld_save;
    public javafx.scene.control.Button btn_save;
    public javafx.scene.control.Button btn_cancel;

    public void SaveGame(){
        try {
            String name = txtfld_save.getText();
            legalName(name);
            setChanged();
            notifyObservers(name);

        }
        catch (Exception e){
            showAlert("Illegal name");
        }
    }

    private void legalName(String name){
        if(name.isEmpty()){
            throw new IllegalArgumentException();
        }
        if(name.contains("/")){
            throw new IllegalArgumentException();
        }
        if(name.contains("?")){
            throw new IllegalArgumentException();
        }
        if(name.contains("*")){
            throw new IllegalArgumentException();
        }
        if(name.contains(":")){
            throw new IllegalArgumentException();
        }
        if(name.contains(">")){
            throw new IllegalArgumentException();
        }
        if(name.contains("<")){
            throw new IllegalArgumentException();
        }
        if(name.contains("|")){
            throw new IllegalArgumentException();
        }
        if(name.contains("\"")){
            throw new IllegalArgumentException();
        }
        if(name.contains("\\")){
            throw new IllegalArgumentException();
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
