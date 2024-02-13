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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class StudiesMainPageController implements Initializable {
    private static Studies studies;

    private Integer studiesId;

    @FXML
    private MenuItem addSemesterMenuItem;

    @FXML
    private Menu removeSemesterMenu;

    @FXML
    private TabPane semestersTabPane;

    public static void setStudies(Studies newStudies) {
        studies = newStudies;
    }

    private void addSemesterTab(Semester newSemester) throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("StudiesMainPageSemesterTab.fxml"));
        Tab newTab = loader.load();
        newTab.setUserData(this.addRemoveSemesterMenuItem(newSemester));
        this.semestersTabPane.getTabs().add(newTab);
        this.semestersTabPane.getSelectionModel().select(newTab);

        StudiesMainPageSemesterTabController controller = loader.getController();
        controller.setSemester(newSemester);
        controller.setStudies(studies);
        controller.setStudiesId(studiesId);
    }

    private MenuItem addRemoveSemesterMenuItem(Semester semester) {
        MenuItem removeSemesterMenuItem = new MenuItem(semester.getSemesterCode());
        removeSemesterMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    if (DatabaseManager.deleteSemester(DatabaseManager.getSemesterId(semester, studiesId))) {
                        removeSemester(semester);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        removeSemesterMenu.getItems().add(removeSemesterMenuItem);
        return removeSemesterMenuItem;
    }

    @FXML
    private void addSemesterMenuItemClick() throws IOException {

        final String semesterCode = PopUpWindow.getSemesterName(this.semestersTabPane.getScene().getWindow(), "Enter semester name");

        if (!InputValidation.isValidString(semesterCode) || studies.getSemesterByCode(semesterCode) != null) {
            return;
        }
        Semester newSemester = new Semester(semesterCode);
        if (DatabaseManager.addSemester(newSemester, studiesId)) {
            studies.addSemester(newSemester);
            this.addSemesterTab(newSemester);
        }
    }

    private void removeSemester(Semester semester) {
        studies.removeSemester(semester);
        this.removeSemesterTab(semester);
        this.removeSemesterRemoveMenuItem(semester);
    }

    private void removeSemesterTab(Semester semester) {
        ObservableList<Tab> tabs = semestersTabPane.getTabs();
        Tab tabToRemove = null;
        for (Tab tab: tabs) {
            if (Objects.equals(tab.getText(), semester.getSemesterCode())) {
                tabToRemove = tab;
            }
        }
        semestersTabPane.getTabs().remove(tabToRemove);
    }

    private void removeSemesterRemoveMenuItem(Semester semester) {
        ObservableList<MenuItem> menuItems = removeSemesterMenu.getItems();
        MenuItem menuItemToRemove = null;
        for (MenuItem menuItem: menuItems) {
            if(Objects.equals(menuItem.getText(), semester.getSemesterCode())) {
                menuItemToRemove = menuItem;
            }
        }
        removeSemesterMenu.getItems().remove(menuItemToRemove);
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
        try {
            studiesId = DatabaseManager.getStudiesId(studies);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Platform.runLater(() -> {
            try {
                this.loadSemester();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

