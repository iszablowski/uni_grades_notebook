package application;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class StudiesMainPageSemesterTabController implements Initializable {

    private Semester semester;

    @FXML
    private Button addClassButton;

    @FXML
    private TableColumn<?, ?> classNameColumn;

    @FXML
    private TableColumn<?, ?> codeColum;

    @FXML
    private Label cumulativeAverageLabel;

    @FXML
    private TableColumn<?, ?> ectsColumn;

    @FXML
    private Label ectsLabel;

    @FXML
    private TableColumn<?, ?> gradeColumn;

    @FXML
    private CheckBox haveGradeCheckbox;

    @FXML
    private TextField newClassCodeTextField;

    @FXML
    private TextField newClassEctsTextField;

    @FXML
    private ComboBox<?> newClassGradeCombobox;

    @FXML
    private TextField newClassNameTextField;

    @FXML
    private Label semesterAverageLabel;

    @FXML
    private Tab semesterTab;

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            semesterTab.setText(semester.getSemesterCode());
        });
    }
}
