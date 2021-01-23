package View;

import ViewModel.MyViewModel;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class MyViewController implements Observer, IView {

    @FXML
    private MyViewModel viewModel;
    private saveController save;
    private loadController load;
    public MazeDisplayer mazeDisplayer;
    public javafx.scene.control.TextField txtfld_rowsNum;
    public javafx.scene.control.TextField txtfld_columnsNum;
    public javafx.scene.control.TextField txtfld_save;
    public javafx.scene.control.Button btn_generateMaze;
    public javafx.scene.control.Button btn_solve;
    public javafx.scene.layout.Pane canvasPane;
    public javafx.scene.control.MenuItem itm_save;
    public StringProperty characterPositionRow = new SimpleStringProperty();
    public StringProperty characterPositionColumn = new SimpleStringProperty();
    public javafx.scene.layout.BorderPane BorderPane;
    private MediaPlayer mediaPlayer;
    private MediaPlayer mediaPlayerWIn;
    private winController winController;

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        canvasPane.setStyle("-fx-border-color: Red;-fx-border-width: 5px;-fx-padding: 0px");
        mazeDisplayer.widthProperty().bind(canvasPane.widthProperty());
        mazeDisplayer.heightProperty().bind(canvasPane.heightProperty());
        btn_solve.setDisable(true);
        itm_save.setDisable(true);
        String path = "./resources/Images/izik.mp3";
        Media media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        String path2 = "./resources/Images/itayeyal.mp3";
        Media media2 = new Media(new File(path2).toURI().toString());
        mediaPlayerWIn=new MediaPlayer(media2);
        addMouseScrolling(mazeDisplayer);
        //bindProperties(viewModel);
    }



    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof MyViewModel) {
            if((int)arg==0) {
                mediaPlayer.setAutoPlay(false);
                displayMaze(viewModel.getMaze());
                btn_generateMaze.setDisable(false);
                btn_solve.setDisable(false);
                mediaPlayer.setAutoPlay(true);

            }
            else if((int)arg==1){
                displaySol(viewModel.getSol());
            }
            else if((int)arg==2){
               changeToWinScene(null);
            }
            else if((int)arg==3){
                ((Stage)(save.btn_save.getScene().getWindow())).close();
                showAlert("Save successful");
            }
        }
        else if(o instanceof saveController){
            try {
                viewModel.Save((String)arg);
            }
            catch (Exception e){
                showAlert("Exists file with this name");
            }
        }
        else if(o instanceof winController){
                mediaPlayerWIn.stop();
                cleanup((Stage) arg);
        }
        else if(o instanceof loadController){
            viewModel.load((String)arg);
        }
    }

    @Override
    public void displayMaze(int[][] maze) {
        mazeDisplayer.setEndMazecol(viewModel.getEndMazecol());
        mazeDisplayer.setEndMazeRow(viewModel.getEndMazeRow());
         mazeDisplayer.setMaze(maze);
        int characterPositionRow = viewModel.getCharacterPositionRow();
        int characterPositionColumn = viewModel.getCharacterPositionColumn();
        mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);
        this.characterPositionRow.set(characterPositionRow + "");
        this.characterPositionColumn.set(characterPositionColumn + "");
    }
    public void generateMaze() {
        try {
            int heigth = Integer.valueOf(txtfld_rowsNum.getText());
            int width = Integer.valueOf(txtfld_columnsNum.getText());
            if(heigth<1 || width<0)
                throw new IllegalArgumentException();
          //  mediaPlayer.setAutoPlay(false);
            mazeDisplayer.setsol(null);
            btn_generateMaze.setDisable(true);
            viewModel.generateMaze(width, heigth);
            btn_solve.setDisable(false);
            itm_save.setDisable(false);
           // mediaPlayer.setAutoPlay(true);
        }
        catch (Exception e){
            showAlert("Please enter correct values");
        }
    }

    private MyViewModel getViewModel() {
        return viewModel;
    }

    public void solveMaze(ActionEvent actionEvent) {
        btn_solve.setDisable(true);
        viewModel.solveMaze();
    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void KeyPressed(KeyEvent keyEvent) {
        try {
            viewModel.moveCharacter(keyEvent.getCode());
            keyEvent.consume();
        }
        catch (Exception e){};
    }

    public String getCharacterPositionRow() {
        return characterPositionRow.get();
    }

    public StringProperty characterPositionRowProperty() {
        return characterPositionRow;
    }

    public String getCharacterPositionColumn() {
        return characterPositionColumn.get();
    }

    public StringProperty characterPositionColumnProperty() {
        return characterPositionColumn;
    }

    public void setResizeEvent(Scene scene) {
        long width = 0;
        long height = 0;
        mazeDisplayer.heightProperty().bind(canvasPane.heightProperty());
        mazeDisplayer.widthProperty().bind(canvasPane.widthProperty());
       /* scene.widthProperty().addListener(new ChangeListener<Number>() {
            /*@Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                System.out.println("Width: " + newSceneWidth);
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                System.out.println("Height: " + newSceneHeight);
            }
        });*/
    }


    public void mouseClicked(MouseEvent mouseEvent) {
        this.mazeDisplayer.requestFocus();
    }

    public void displaySol(int[][] sol){
        this.mazeDisplayer.setsol(sol);
    }



    public void New(ActionEvent actionEvent){
        btn_solve.setDisable(true);
        itm_save.setDisable(true);
        btn_generateMaze.setDisable(false);
        mazeDisplayer.New();

    }

    public void Save(ActionEvent actionEvent){
        try
        {
            Stage stage = new Stage();
            stage.setTitle("Save Game");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("SaveGame.fxml").openStream());
            Scene scene = new Scene(root, 400, 350);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
            save=((saveController)fxmlLoader.getController());
            save.addObserver(this);
        } catch (Exception e) {
        }
    }

    public void Load(ActionEvent actionEvent){
        try
        {
            Stage stage = new Stage();
            stage.setTitle("Load Game");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("loadGame.fxml").openStream());
            Scene scene = new Scene(root, 400, 350);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            load=((loadController)fxmlLoader.getController());
            load.addObserver(this);
            load.setFiles(viewModel.getFiles());
            stage.show();
        } catch (Exception e) {
        }
    }
    public String[] getAboutText(){
        String[] arr=new String[22];
        arr[0]=new String("this game was created by:");
        arr[2]=new String("Omer Reichstein");
        arr[3]=new String("King Of the seven Kingdoms");
        arr[4]=new String("King Of the Andals ,the First man");
        arr[5]=new String("and protector of the realm");
        arr[6]=new String(" ");
        arr[7]=new String("special thanks go to");
        arr[8]=new String("Gal 'bola' Atedgi");
        arr[9]=new String("for designing the upside down canvas");
        arr[10]=new String("and for the save and load options");
        arr[11]=new String(" ");
        arr[12]=new String("photos by AP");
        arr[13]=new String("itzik kala");
        arr[15]=new String("video by Eyal Golan and Maor Edri");
        arr[16]=new String("we hope you Enjoyed our game");
        arr[17]=new String("if you like to play again ");
        arr[17]=new String("click the 'new' option under 'file'  ");
        arr[19]=new String("bye");
        arr[20]=new String("bye");
        arr[21]=new String("bye");
        return arr;
    }

    public void cleanup(Stage s){
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = null;
        try {
            root = fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root,500,400);
        scene.getStylesheets().add(getClass().getResource("View.css").toExternalForm());
        s.setScene(scene);
        s.show();
        ((MyViewController)fxmlLoader.getController()).setViewModel(viewModel);
        viewModel.addObserver((MyViewController)fxmlLoader.getController());
        viewModel.deleteObserver(this);
    }

    public void changeToWinScene(ActionEvent actionEvent) {
        mediaPlayer.stop();
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = null;
        try {
            root = fxmlLoader.load(getClass().getResource("win.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root,570,330);
        Stage s=(Stage)btn_solve.getScene().getWindow();
        s.setTitle("you won!");
        s.setScene(scene);
        //s.show();
        winController=((winController)fxmlLoader.getController());
        winController.addObserver(this);
        winController.setImageWin();
        mediaPlayerWIn.play();
        s.show();

       /* try {
           // FXMLLoader fxmlLoader = new FXMLLoader();
            //Parent root = fxmlLoader.load(getClass().getResource("win.fxml").openStream());
            Scene scene = new Scene(new Group(), 500, 400);
            Stage s = (Stage) btn_solve.getScene().getWindow();
            s.setScene(scene);
            s.setTitle("win!");
         //   s.show();
            String path = "./resources/Images/Eyal.mp4";
            Media media = new Media(new File(path).toURI().toString());
            mediaPlayerWIn = new MediaPlayer(media);
            mediaPlayerWIn.setAutoPlay(true);
            MediaView mediaView = new MediaView(mediaPlayer);
            ((Group) scene.getRoot()).getChildren().add(mediaView);
            s.show();
            //s.setScene(scene);
           // s.show();
        }
        catch (Exception e){

        }*/


    }

    public void About(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            VBox vbox = new VBox();
            vbox.setId("vvv");
            javafx.geometry.Insets insets=new javafx.geometry.Insets(50,50,50,50);
            vbox.setPadding(insets);
            String[] data=getAboutText();
            for(int i=0;i<data.length;i++){
                vbox.getChildren().add(new javafx.scene.control.Label(i + ". " + data[i]));
            }
            //add a copy of the first 12 lines that will be showing as wrapped
            ScrollPane sp = new ScrollPane(vbox);
            sp.setId("scroll");
            Scene scene = new Scene(sp, 350, 150);//guess height
            scene.getStylesheets().add(getClass().getResource("About.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
            double textHeight = vbox.getHeight() / vbox.getChildren().size();
            stage.setHeight(textHeight*12+stage.getHeight()-scene.getHeight());

            Timeline timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE);
            KeyValue kv = new KeyValue(sp.vvalueProperty(), sp.getVmax());
            KeyFrame kf = new KeyFrame(Duration.millis(10000), kv);
            timeline.getKeyFrames().addAll(kf);
            timeline.play();
        } catch (Exception e) {

        }
    }

    public void openPropertirs(ActionEvent actionEvent){
        Stage stage = new Stage();
        stage.setTitle("set properties");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = null;
        try {
            root = fxmlLoader.load(getClass().getResource("properties.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root, 500, 500);
        scene.getStylesheets().add(getClass().getResource("properties.css").toExternalForm());
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
        stage.show();
        Properties conroller=(Properties) fxmlLoader.getController();
        conroller.setWindow();
    }

    public void Help(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Help");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Help.fxml").openStream());
            Scene scene = new Scene(root, 400, 350);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {
        }
    }

    private void addMouseScrolling(Node node) {
        node.setOnScroll((ScrollEvent event) -> {
            // Adjust the zoom factor as per your requirement
            System.out.println("zxczxczxc");
            double zoomFactor = 1.05;
            double deltaY = event.getDeltaY();
            if (deltaY < 0){
                zoomFactor = 2.0 - zoomFactor;
            }
            node.setScaleX(node.getScaleX() * zoomFactor);
            node.setScaleY(node.getScaleY() * zoomFactor);
        });
    }




    }
