
package mysmartlibrary;

import java.sql.SQLException;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

// Login page
public class FirstPhaseController implements Initializable {
     @FXML
    private RadioButton FemaleButton;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label emailLabel;

    @FXML
    private TextField emailTextField;
    @FXML
    private Button loginButton;

    @FXML
    private RadioButton maleButton;

    @FXML
    private Label passLabel;
    
      @FXML
    private Label accountLabel;

    @FXML
    private PasswordField passTextField;

    @FXML
    private Button signupButton;

    @FXML
    private Label welcomeLabel;
    
    @FXML
      private ToggleGroup genderGroup;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
        
    
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
   
       public String getMD5(String input){
            try{
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] messge = md.digest(input.getBytes());
                StringBuilder hexString = new StringBuilder();
                for(byte b: messge){
                    hexString.append(String.format("%02x", b));
                }
                return hexString.toString();
               
            }catch (NoSuchAlgorithmException e){
                throw new RuntimeException (e);
            }
        }
    

    @FXML
   public void handleButtonLogin(ActionEvent event) {
       String email = emailTextField.getText();
        String password = passTextField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.WARNING, "Login Failed", "Please fill in all required fields.");
            return;
        }

        LoginDatabaseService log = new LoginDatabaseService();
        
       try{
           List<User> allUsers = log.getAllUsers();
           Optional<User> userOptional = allUsers.stream()
                   .filter(user-> user.getEmail().equalsIgnoreCase(email))
                   .findFirst();
           if(userOptional.isPresent()){
               User foundUser = userOptional.get();
               String storedHashPassword = foundUser.getPasswordHash();
               String enteredHashPassword = getMD5(password);
            
            if(storedHashPassword.equals(enteredHashPassword)){
                showAlert(AlertType.INFORMATION, "Login Success", "Welcome "+foundUser.getFirstName()+" "+foundUser.getLastName());
                
                loginButton.getScene().getWindow().hide();
                 FXMLLoader loader = new FXMLLoader(getClass().getResource("/mysmartlibrary/DashBord.fxml"));
                    AnchorPane root = loader.load();
                    DashBordController dashController = loader.getController();
                    dashController.setUserName(foundUser.getFirstName()+" "+foundUser.getLastName());
                    Stage primaryStage = new Stage();
                    Scene scene = new Scene(root);
                    primaryStage.setScene(scene);
                    primaryStage.setTitle("Dashboard");
                    primaryStage.show();
            }else{
                showAlert(AlertType.WARNING, "Login Fail", "Your password is not correct, please try again");
            }
               
           }else{
               showAlert(AlertType.WARNING, "Login Fail", "Your Email is not exist, please try again");
           }
       }catch(SQLException e){
           showAlert(AlertType.WARNING, "Error while reading the database", "Try again latter");
           e.printStackTrace();
       }catch(IOException e){
           showAlert(AlertType.WARNING, "Error ", "while loading dashboard");
       }
           
    }

    

    @FXML
    public  void handleButtonMale(ActionEvent event) {
       

    }
    
        @FXML
    public void handleButtonFemale(ActionEvent event) {
       

    }

    @FXML
     public void handleButtonSignup(ActionEvent event) throws IOException {
       
        signupButton.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mysmartlibrary/signUp.fxml"));
        AnchorPane root = loader.load();
        Stage primaryStage = new Stage();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sign Up Page");
        primaryStage.show();
       

    }

}
