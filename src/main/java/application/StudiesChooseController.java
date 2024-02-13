package application;

import application.tools.DatabaseManager;
import application.tools.InputValidation;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class StudiesChooseController implements Initializable {

    private ArrayList<Studies> studiesArrayList = new ArrayList<>();

    @FXML
    private AnchorPane studiesMainPagePane;

    @FXML
    private MenuItem addStudiesMenuItem;

    @FXML
    private Menu removeStudiesMenu;

    @FXML
    private ListView<String> studiesList = new ListView<String>();

    private void addStudies(Studies newStudies) {
        studiesArrayList.add(newStudies);
        studiesList.getItems().add(newStudies.getStudiesName());
        this.addRemoveStudiesMenuItem(newStudies);
    }

    private void addRemoveStudiesMenuItem(Studies studies) {
        MenuItem removeStudiesMenuItem = new MenuItem(studies.getStudiesName());

        removeStudiesMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (DatabaseManager.deleteStudies(studies.getStudiesName())) {
                    removeStudies(studies);
                }
            }
        });
        removeStudiesMenu.getItems().add(removeStudiesMenuItem);
    }

    private void removeStudies(Studies studies) {
        this.studiesArrayList.remove(studies);
        this.removeStudiesFromListView(studies.getStudiesName());
        this.removeStudiesRemoveMenuItem(studies.getStudiesName());
    }

    private void removeStudiesFromListView(String studiesToRemoveName) {
        ObservableList<String> listViewStrings = studiesList.getItems();
        String viewStringToRemove = null;
        for (String viewString: listViewStrings) {
            if (viewString.equals(studiesToRemoveName)) {
                viewStringToRemove = viewString;
            }
        }
        studiesList.getItems().remove(viewStringToRemove);
    }

    private void removeStudiesRemoveMenuItem(String studiesToRemoveName) {
        ObservableList<MenuItem> menuItems = removeStudiesMenu.getItems();
        MenuItem menuItemToRemove = null;
        for (MenuItem menuItem: menuItems) {
            if (Objects.equals(menuItem.getText(), studiesToRemoveName)) {
                menuItemToRemove = menuItem;
            }
        }
        removeStudiesMenu.getItems().remove(menuItemToRemove);
    }
    @FXML
    protected void addStudiesMenuItemAction() throws IOException {
        final String studiesName = PopUpWindow.getSemesterName(this.studiesMainPagePane.getScene().getWindow(), "Enter studies name");

        if (!InputValidation.isValidString(studiesName) || !(this.getStudiesByName(studiesName) == null)) {
            return;
        }
        Studies studiesToAdd = new Studies(studiesName);
        if (DatabaseManager.addStudies(studiesToAdd)) {
            this.addStudies(studiesToAdd);
        }
    }

    @FXML
    protected void studiesChoose() throws IOException {
        String chosenElement = studiesList.getSelectionModel().getSelectedItem();
        if (chosenElement == null) {
            return;
        }

        Studies choosenStudies = this.getStudiesByName(chosenElement);
        StudiesMainPageController.setStudies(choosenStudies);

        Parent root = FXMLLoader.load(getClass().getResource("StudiesMainPage.fxml"));
        Stage stage = new Stage();
        stage.setTitle(choosenStudies.getStudiesName());
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();

        studiesMainPagePane.getScene().getWindow().hide();
    }

    private Studies getStudiesByName(String studiesToFindName) {
        for (Studies studies: studiesArrayList) {
            if (Objects.equals(studies.getStudiesName(), studiesToFindName)) {
                return studies;
            }
        }
        return null;
    }


    private void loadStudies() {
        ArrayList<Studies> studiesToLoadArrayList = DatabaseManager.loadStudiesList();
        for (Studies studies: studiesToLoadArrayList) {
            this.addStudies(studies);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            this.loadStudies();
        });
    }
}