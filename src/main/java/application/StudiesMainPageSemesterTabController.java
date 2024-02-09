package application;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class StudiesMainPageSemesterTabController implements Initializable {

    private Semester semester;

    @FXML
    private Button addClassButton;

    @FXML
    private TableColumn<Class, String> classNameColumn;

    @FXML
    private TableColumn<Class, String> codeColum;

    @FXML
    private Label cumulativeAverageLabel;

    @FXML
    private TableColumn<Class, Integer> ectsColumn;

    @FXML
    private Label ectsLabel;

    @FXML
    private TableColumn<Class, Double> gradeColumn;

    @FXML
    private TableView classesTable;

    @FXML
    private CheckBox haveGradeCheckbox;

    @FXML
    private TextField newClassCodeTextField;

    @FXML
    private TextField newClassEctsTextField;

    @FXML
    private ComboBox<Double> newClassGradeCombobox;

    @FXML
    private TextField newClassNameTextField;

    @FXML
    private Label semesterAverageLabel;

    @FXML
    private Tab semesterTab;

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    @FXML
    private void setHaveGradeCheckbox() {
        if (!haveGradeCheckbox.isSelected()) {
            newClassGradeCombobox.setDisable(true);
            newClassGradeCombobox.getSelectionModel().clearSelection();
        } else {
            newClassGradeCombobox.setDisable(false);
        }
    }

    private String getNewClassName() {
        return newClassNameTextField.getText();
    }

    private String getNewClassCode() {
        return newClassCodeTextField.getText();
    }

    private Double getNewClassGrade() {
        Double grade = newClassGradeCombobox.getSelectionModel().getSelectedItem();
        return (grade == null) ? 0 :  grade;
    }

    private String getNewClassEctsString() {
        return newClassEctsTextField.getText();
    }

    private void addClass(String newClassName, String newClassCode, int newClassEcts, Double newClassGrade) {
        Class newClass = new Class(newClassName, newClassCode, newClassEcts, newClassGrade);
        semester.addClass(newClass);
        classesTable.getItems().add(newClass);
    }

    @FXML
    private void addClassButtonClick() {
        String classEctsString = this.getNewClassEctsString();
        String className = this.getNewClassName();
        String classCode = this.getNewClassCode();
        Double classGrade = this.getNewClassGrade();
        if (!InputValidation.isClassDataValid(className, classCode, classEctsString, classGrade)) {
            return;
        }
        this.addClass(className, classCode, Integer.parseInt(classEctsString), classGrade);
        this.clearNewClassTextFields();
    }

    private void clearNewClassTextFields() {
        this.newClassNameTextField.clear();
        this.newClassCodeTextField.clear();
        this.newClassEctsTextField.clear();
        this.newClassGradeCombobox.getSelectionModel().clearSelection();
        this.haveGradeCheckbox.setSelected(true);
    }

    private void initClassTable() {
        classNameColumn.setCellValueFactory(new PropertyValueFactory<>("className"));
        codeColum.setCellValueFactory(new PropertyValueFactory<>("classCode"));
        ectsColumn.setCellValueFactory(new PropertyValueFactory<>("classEcts"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("classGrade"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            semesterTab.setText(semester.getSemesterCode());
            newClassGradeCombobox.getItems().add(null);
            newClassGradeCombobox.getItems().addAll(Grades.getGrades());
        });
    }
}
