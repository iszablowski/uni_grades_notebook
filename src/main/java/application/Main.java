package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Scene studiesChooseScene = new Scene(new FXMLLoader(getClass().getResource("StudiesChoose.fxml")).load());
        stage.setTitle("University notebook");
        stage.setResizable(false);
        stage.setScene(studiesChooseScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}