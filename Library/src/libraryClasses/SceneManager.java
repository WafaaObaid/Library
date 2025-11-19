
package mysmartlibrary;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
     public static void loadScene(String fxmlFile, ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(SceneManager.class.getResource(fxmlFile));
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    
}
