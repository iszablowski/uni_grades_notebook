package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class SemesterNamePopUpWindowController {

    @FXML
    private StackPane newSemesterCodePopUpPane;

    @FXML
    private TextField newSemesterCode;

    @FXML
    private Button submitButton;

    public TextField getNewSemesterCode() {
        return newSemesterCode;
    }

    @FXML
    private void submitButtonAction() {
        this.newSemesterCodePopUpPane.getScene().getWindow().hide();
    }
}
