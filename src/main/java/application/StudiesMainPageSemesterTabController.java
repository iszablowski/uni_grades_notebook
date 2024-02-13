package application;

import application.tools.CustomIntegerStringConverter;
import application.tools.DatabaseManager;
import application.tools.InputValidation;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StudiesMainPageSemesterTabController implements Initializable {

    private Semester semester;

    private Studies studies;

    private Integer semesterId;

    private Integer studiesId;

    @FXML
    private Button addClassButton;

    @FXML
    private TableColumn<Class, String> classNameColumn;

    @FXML
    private TableColumn<Class, String> codeColumn;

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
    private MenuItem renameSemesterMenuItem;

    @FXML
    private TextField newClassCodeTextField;

    @FXML
    private CheckMenuItem isClassesTableEditable;

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

    public void setStudies(Studies studies) {
        this.studies = studies;
    }

    public void setStudiesId(Integer studiesId) {
        this.studiesId = studiesId;
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

    @FXML
    private void renameSemesterMenuItemAction() throws IOException {
        final String newSemesterCode = PopUpWindow.getSemesterName(this.semesterTab.getContent().getScene().getWindow(), "Enter new semester name");

        if (!InputValidation.isValidString(newSemesterCode) || studies.getSemesterByCode(newSemesterCode) != null) {
            return;
        }
        if (DatabaseManager.updateSemester(newSemesterCode, semesterId)) {
            semester.setSemesterCode(newSemesterCode);
            semesterTab.setText(newSemesterCode);
            ((MenuItem)semesterTab.getUserData()).setText(newSemesterCode);
        }
    }

    private String getNewClassEctsString() {
        return newClassEctsTextField.getText();
    }

    private void addClass(String newClassName, String newClassCode, int newClassEcts, Double newClassGrade) {
        Class newClass = new Class(newClassName, newClassCode, newClassEcts, newClassGrade);
        if (DatabaseManager.addClass(newClass, this.semesterId)) {
            semester.addClass(newClass);
            this.addClassToTable(newClass);
        }
    }

    private void addClassToTable(Class classToAdd) {
        classesTable.getItems().add(classToAdd);
        updateSemesterData();
    }

    @FXML
    public void semesterTabOpenAction() {
        Platform.runLater(() -> {
            if (semesterTab.isSelected()) {
                updateSemesterCumulativeAverage();
            }
        });
    }

    @FXML
    private void addClassButtonClick() {
        String classEctsString = this.getNewClassEctsString();
        String className = this.getNewClassName();
        String classCode = this.getNewClassCode();
        Double classGrade = this.getNewClassGrade();
        if (!InputValidation.isClassDataValid(className, classCode, classEctsString, classGrade) || !(semester.getClassByName(className) == null)) {
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
        this.newClassGradeCombobox.setDisable(false);
    }

    private void initClassTable() {
        this.updateTableData();
        classesTable.setEditable(true);
        classNameColumn.setEditable(true);
        classNameColumn.setCellValueFactory(new PropertyValueFactory<>("className"));
        classNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        classNameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Class, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Class, String> classStringCellEditEvent) {
                String newClassName = classStringCellEditEvent.getNewValue();
                if (InputValidation.isValidString(newClassName) && semester.getClassByName(newClassName) == null) {
                    Class classToUpdate = classStringCellEditEvent.getTableView().getItems().get(classStringCellEditEvent.getTablePosition().getRow());
                    if (DatabaseManager.updateClassName(classToUpdate, newClassName, semesterId)) {
                        classToUpdate.setClassName(newClassName);
                    } else {
                        classesTable.refresh();
                    }
                } else {
                    classesTable.refresh();
                }
            }
        });

        codeColumn.setEditable(true);
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("classCode"));
        codeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        codeColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Class, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Class, String> classStringCellEditEvent) {
                String newClassCode = classStringCellEditEvent.getNewValue();
                if (InputValidation.isValidString(newClassCode)) {
                    Class classToUpdate = classStringCellEditEvent.getTableView().getItems().get(classStringCellEditEvent.getTablePosition().getRow());
                    if (DatabaseManager.updateClassCode(classToUpdate, newClassCode, semesterId)) {
                        classToUpdate.setClassCode(newClassCode);
                    } else {
                        classesTable.refresh();
                    }
                } else {
                    classesTable.refresh();
                }
            }
        });

        ectsColumn.setEditable(true);
        ectsColumn.setCellValueFactory(new PropertyValueFactory<>("classEcts"));
        ectsColumn.setCellFactory(TextFieldTableCell.<Class, Integer>forTableColumn(new CustomIntegerStringConverter()));
        ectsColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Class, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Class, Integer> classIntegerCellEditEvent) {
                Integer newEcts = classIntegerCellEditEvent.getNewValue();
                if (newEcts != null && newEcts > 0) {
                    Class classToUpdate = classIntegerCellEditEvent.getTableView().getItems().get(classIntegerCellEditEvent.getTablePosition().getRow());
                    if (DatabaseManager.updateClassEcts(classToUpdate, newEcts, semesterId)) {
                        classToUpdate.setClassEcts(newEcts);
                        updateSemesterData();
                    } else {
                        classesTable.refresh();
                    }
                } else {
                    classesTable.refresh();
                }
            }
        });

        gradeColumn.setEditable(true);
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("classGrade"));
        gradeColumn.setCellFactory(ComboBoxTableCell.<Class, Double>forTableColumn(newClassGradeCombobox.getItems()));
        gradeColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Class, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Class, Double> classDoubleCellEditEvent) {
                Double newGrade = (classDoubleCellEditEvent.getNewValue() != null) ? classDoubleCellEditEvent.getNewValue(): 0;
                Class classToUpdate = classDoubleCellEditEvent.getTableView().getItems().get(classDoubleCellEditEvent.getTablePosition().getRow());
                if (DatabaseManager.updateClassGrade(classToUpdate, newGrade, semesterId)) {
                    classToUpdate.setClassGrade(newGrade);
                    updateSemesterData();
                } else {
                    classesTable.refresh();
                }
            }
        });
    }

    private void updateTableData() {
        for (Class classToAdd: semester.getSemesterClasses()) {
            this.addClassToTable(classToAdd);
        }
    }

    @FXML
    private void isClassesTableEditableChange() {
        if (isClassesTableEditable.isSelected()) {
            classesTable.setEditable(true);
        } else {
            classesTable.setEditable(false);
        }
    }

    private void updateSemesterData()  {
        ectsLabel.setText(semester.getSemesterEcts() + "/" + String.valueOf(semester.getSemesterPlannedEcts()));
        semesterAverageLabel.setText(String.valueOf(semester.getSemesterAverage()));
        this.updateSemesterCumulativeAverage();
    }

    private void updateSemesterCumulativeAverage() {
        cumulativeAverageLabel.setText(String.valueOf(studies.getStudiesCumulativeAverage(semester)));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            try {
                this.semesterId = DatabaseManager.getSemesterId(semester, studiesId);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            this.initClassTable();
            semesterTab.setText(semester.getSemesterCode());
            newClassGradeCombobox.getItems().add(null);
            newClassGradeCombobox.getItems().addAll(Grades.getGrades());
        });
    }
}
