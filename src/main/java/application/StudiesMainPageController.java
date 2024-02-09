package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StudiesMainPageController implements Initializable {
    private static Studies studies;
    private ArrayList<StudiesMainPageSemesterTabController> semesterTabs = new ArrayList<>();

    @FXML
    private MenuItem addSemesterMenuItem;

    @FXML
    private CheckMenuItem editableCheckMenuItem;

    @FXML
    private MenuItem removeSemesterMenuItem;

    @FXML
    private TabPane semestersTabPane;

    public static void setStudies(Studies newStudies) {
        studies = newStudies;
    }

    private void addSemesterTab(String semesterName) throws IOException {
        Semester newSemester = new Semester(semesterName);
        studies.addSemester(newSemester);

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("StudiesMainPageSemesterTab.fxml"));
        Tab newTab = loader.load();
        this.semestersTabPane.getTabs().add(newTab);
        this.semestersTabPane.getSelectionModel().select(newTab);

        StudiesMainPageSemesterTabController controller = loader.getController();
        controller.setSemester(newSemester);
    }

    @FXML
    private void addSemesterMenuItemClick() throws IOException {

        final String semesterCode = this.getSemesterName(this.semestersTabPane.getScene().getWindow());

        if (semesterCode == null || semesterCode.trim().isEmpty()) {
            return;
        }
        this.addSemesterTab(semesterCode);
    }

    private String getSemesterName(Window owner) throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("SemesterNamePopUpWindow.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Enter semester name");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.showAndWait();
        SemesterNamePopUpWindowController controller = loader.getController();
        return controller.getNewSemesterCode().getText();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}

