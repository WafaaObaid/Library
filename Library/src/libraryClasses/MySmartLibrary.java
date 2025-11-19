
package mysmartlibrary;

import java.sql.Connection;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MySmartLibrary extends Application{

     @Override
    public void start(Stage primaryStage) throws Exception {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/mysmartlibrary/LoginPage.fxml"));
       
         AnchorPane root = loader.load();
         Scene scene = new Scene(root);
         primaryStage.setScene(scene);
         primaryStage.setTitle("Login Page");
         primaryStage.show();
        
    }
 
    public static void main(String[] args) {
         try (Connection conn = DataBase.getConnection()) {
            if (conn != null) {
                System.out.println("Success");
            } else {
                System.out.println("Fail");
            }
        } catch (SQLException e) {
            System.err.println("Fail");
            e.printStackTrace();
        }
        launch(args);
       
    }

   
    
}
