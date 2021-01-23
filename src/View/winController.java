package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.FileInputStream;
import java.util.Observable;
    public class winController extends Observable {
        @FXML
        BorderPane mainPane;
        @FXML
        private javafx.scene.canvas.Canvas canvas;
        @FXML
        private javafx.scene.image.ImageView mediaView;

        @FXML
        private javafx.scene.layout.Pane pane;

        public winController() {
        }


        public void backToMain(ActionEvent actionEvent){
            setChanged();
            Stage s=(Stage) mainPane.getScene().getWindow();
            notifyObservers(s);
            deleteObservers();
        }

        public void setImageWin(){
            try {
                mediaView.fitHeightProperty().bind(pane.heightProperty());
                mediaView.fitWidthProperty().bind(pane.widthProperty());
                  javafx.scene.image.Image win = new Image(new FileInputStream("./resources/Images/200.gif"));
                 mediaView.setImage(win);


            }
            catch (Exception e){};
        }




    }
