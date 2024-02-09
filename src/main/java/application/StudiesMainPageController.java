package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

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

        final String semesterCode = PopUpWindow.getSemesterName(this.semestersTabPane.getScene().getWindow(), "Enter semester name");

        if (!InputValidation.isValidString(semesterCode)) {
            return;
        }
        this.addSemesterTab(semesterCode);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}

