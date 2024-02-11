package application;

import application.tools.DatabaseManager;
import application.tools.InputValidation;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StudiesMainPageController implements Initializable {
    private static Studies studies;

    @FXML
    private MenuItem addSemesterMenuItem;

    @FXML
    private MenuItem removeSemesterMenuItem;

    @FXML
    private TabPane semestersTabPane;

    public static void setStudies(Studies newStudies) {
        studies = newStudies;
    }

    private void addSemesterTab(Semester newSemester) throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("StudiesMainPageSemesterTab.fxml"));
        Tab newTab = loader.load();
        this.semestersTabPane.getTabs().add(newTab);
        this.semestersTabPane.getSelectionModel().select(newTab);

        StudiesMainPageSemesterTabController controller = loader.getController();
        controller.setSemester(newSemester);
    }

    @FXML
    private void addSemesterMenuItemClick() throws IOException {

        final String semesterCode = PopUpWindow.getSemesterName(this.semestersTabPane.getScene().getWindow(), "Enter semester name");

        if (!InputValidation.isValidString(semesterCode)) {
            return;
        }
        Semester newSemester = new Semester(semesterCode);
        studies.addSemester(newSemester);
        this.addSemesterTab(newSemester);
    }

    private void loadSemester() throws IOException {
        DatabaseManager.loadStudiesData(studies);
        this.updateSemesterTab();
    }

    private void updateSemesterTab() throws IOException {
        for (Semester semester: studies.getSemesters()) {
            this.addSemesterTab(semester);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            try {
                this.loadSemester();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

