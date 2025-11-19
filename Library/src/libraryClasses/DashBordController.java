
package mysmartlibrary;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;


public class DashBordController implements Initializable {
   @FXML
    private Label actionLabel;
    @FXML
    private ImageView imagebook;

    @FXML
    private Button bookPageButton;

    @FXML
    private Label booksLabel;

    @FXML
    private Label borrowingLabel;

    @FXML
    private Button borrowingPageButton;

    @FXML
    private Label dashLabel;

    @FXML
    private Button memberPageButton;

    @FXML
    private Label membersLabel;

    @FXML
    private Label orgBooksLabel;

    @FXML
    private Label orgBorrowingLabel;

    @FXML
    private Label orgMembersLabel;

    @FXML
    private Button signOutButton;

    @FXML
    private Label welcomeUserLabel;
    public void setUserName(String userName){
         welcomeUserLabel.setText("Welcome, " + userName);
    }
    
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
        
    }
     @FXML
    public void handleBookPage(ActionEvent event) {
        try{
            SceneManager.loadScene("/mysmartlibrary/Book.fxml", event);
        }catch(Exception e){
            e.printStackTrace();
            
        }

    }

    @FXML
   public void handleBorrowingPage(ActionEvent event) {
        try{
            SceneManager.loadScene("/mysmartlibrary/Borrowing.fxml", event);
        }catch(Exception e){
            e.printStackTrace();
            
        }

    }

    @FXML
   public void handleMemberPage(ActionEvent event) {
        try{
            SceneManager.loadScene("/mysmartlibrary/Member_1.fxml", event);
        }catch(Exception e){
            e.printStackTrace();
            
        }

    }

    @FXML
   public void handleSignOut(ActionEvent event) {
        try{
            SceneManager.loadScene("/mysmartlibrary/LoginPage.fxml", event);
        }catch(Exception e){
            e.printStackTrace();
            
        }

    }

    
    
    
    
}
     