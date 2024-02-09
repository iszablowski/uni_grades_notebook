package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class NewNamePopUpWindowController {

    @FXML
    private StackPane newNamePopUpPane;

    @FXML
    private TextField newNameTextField;

    @FXML
    private Button submitButton;

    public TextField getNewNameTextField() {
        return newNameTextField;
    }

    @FXML
    private void submitButtonAction() {
        this.newNamePopUpPane.getScene().getWindow().hide();
    }
}
