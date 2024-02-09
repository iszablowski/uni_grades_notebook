package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class PopUpWindow {
    public static String getSemesterName(Window owner, String windowTitle) throws IOException {
        FXMLLoader loader = new FXMLLoader(PopUpWindow.class.getResource("NewNamePopUpWindow.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.showAndWait();
        NewNamePopUpWindowController controller = loader.getController();
        return controller.getNewNameTextField().getText();
    }
}
