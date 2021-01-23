package View;

import test.Configurations;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Properties {
    @FXML
    ChoiceBox algotighmChoice;
    @FXML
    ChoiceBox generatorChoice;

    @FXML
    AnchorPane anchorPane;
    public void setWindow(){
        ArrayList<String> generators=new ArrayList<>(3);
        generators.add("Best First Search");
        generators.add("Breadth First Search");
        generators.add("Depth First Search");
        algotighmChoice.setItems(FXCollections.observableArrayList(generators));
        ArrayList<String> algos=new ArrayList<>(3);
        algos.add("Empty Maze Generator");
        algos.add("Simple Maze Generator");
        algos.add("My Maze Generator");
        generatorChoice.setItems(FXCollections.observableArrayList(algos));
    }

    public void submit(ActionEvent actionEvent){
        String algo=(String) algotighmChoice.getValue();
        String generator=(String)generatorChoice.getValue();
        if(algo==null)
            algo="";
        if(generator==null)
            generator="";
        MyModel.setConfig(algo,generator);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("in the next maze you generate the changes will apply!");
        alert.show();
        ((Stage)algotighmChoice.getScene().getWindow()).close();
    }


}