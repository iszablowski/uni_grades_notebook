package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class StudiesChooseController implements Initializable {

    private HashMap<String, Studies> nameToStudies = new HashMap<String, Studies>();

    private void addStudies(String newStudiesName) {
        Studies newStudies = new Studies(newStudiesName);
        nameToStudies.put(newStudiesName, newStudies);
        studiesList.getItems().add(newStudiesName);
    }

    @FXML
    private AnchorPane studiesMainPagePane;
    @FXML
    private Button addStudiesBtn;
    @FXML
    private TextField studiesName;
    @FXML
    private ListView<String> studiesList = new ListView<String>();
    @FXML
    protected void addStudiesBtnClick() {
        this.addStudies(studiesName.getText());
    }

    @FXML
    protected void studiesChoose() throws IOException {
        String chosenElement = studiesList.getSelectionModel().getSelectedItem();
        if (chosenElement == null) {
            return;
        }

        Studies choosenStudies = nameToStudies.get(studiesList.getSelectionModel().getSelectedItem());
        StudiesMainPageController.setStudies(choosenStudies);

        Parent root = FXMLLoader.load(getClass().getResource("StudiesMainPage.fxml"));
        Stage stage = new Stage();
        stage.setTitle(choosenStudies.getStudiesName());
        stage.setScene(new Scene(root));
        stage.show();

        studiesMainPagePane.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}