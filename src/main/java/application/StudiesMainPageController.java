package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class StudiesMainPageController implements Initializable {
    private static Studies studies;

    @FXML
    private Label studiesName;

    public static void setStudies(Studies newStudies) {
        studies = newStudies;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        studiesName.setText(studies.getStudiesName());
    }
}
