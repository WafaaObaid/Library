
package mysmartlibrary;

import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class SignUpController implements Initializable {

    @FXML
    private TextField EmailFieldsignup;

    @FXML
    private Label EmailLabelsignup;

    @FXML
    private Button SignUpButton;

    @FXML
    private PasswordField confirmField;

    @FXML
    private Label confirmLabel;

    @FXML
    private TextField firstNameField;

    @FXML
    private Label firstNameLabel;

    @FXML
    private Label lastNameLabel;

    @FXML
    private TextField lastNamefield;

    @FXML
    private Label niceLabel;

    @FXML
    private PasswordField passwordFieldsignup;

    @FXML
    private Label passwordLabelsignup;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }    
    
    private void showAlert(AlertType type, String title, String message){
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
    
    
    private boolean isEmailExists(String email) throws FileNotFoundException, IOException{
        String filePath = "C:/Users/97056/mySmartLibrary/users.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",", 3);
            if (parts.length == 3) {
                String emailFromFile = parts[1].trim();
                if (emailFromFile.equalsIgnoreCase(email.trim())) {
                    return true;
                }
            }
        }
    }
    return false; 

    }
    
    private boolean isValidEmail(String email){
        String emailCheck = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailCheck);
        return pat.matcher(email).matches();
    }
    
    private void saveUserToFile(String userFullname, String email, String password) throws IOException{
        String filePath = "C:/Users/97056/mySmartLibrary/users.txt";
        try(PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))){
            writer.println(userFullname+","+email+","+password);
        }
    }
    
    @FXML
    public void handleSignupButtonPage(ActionEvent event) {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNamefield.getText().trim();
        String email = EmailFieldsignup.getText().trim();
        String password = passwordFieldsignup.getText().trim();
        String confrimPassword = confirmField.getText().trim();
        
        //Empty fields
        if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confrimPassword.isEmpty()){
            showAlert(AlertType.WARNING, "Empty fields", "Please fill all fields");
            return;
        }
        
        //Email field
        if(!isValidEmail(email)){
            showAlert(AlertType.WARNING, "Invalid Email", "Fill the email field correct");
            return;
        }
        
        // Passwords are the same
        if(!password.equals(confrimPassword)){
            showAlert(AlertType.WARNING, "Password Mismatch", "Passwords are not the same");
            return;
        }
        
        LoginDatabaseService dbService = new LoginDatabaseService();
        try{
            if(dbService.isEmailExist(email)){
                showAlert(AlertType.WARNING, "Email Exists", "This Email is already exists");
                return;
            }
            String hashPassword = getMD5(password);
            User newUser = new User(0,firstName,lastName,email,hashPassword);
            dbService.saveUser(newUser);
            showAlert(AlertType.INFORMATION, "Sign up success", "Account created successfully! ");
            
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mysmartlibrary/DashBord.fxml"));
        Parent root = loader.load();
        DashBordController dashController = loader.getController();
        dashController.setUserName(firstName+" "+lastName); 

        Stage stage = (Stage) SignUpButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Dashboard");
        stage.show();

        }catch(SQLException e){
         showAlert(AlertType.ERROR, "Database Error", "An error occurred while saving the user to the database.");
        e.printStackTrace();
        }catch(IOException e){
        showAlert(AlertType.ERROR, "Page Error", "Could not load the login page.");
        e.printStackTrace();
        }
            
         
         
      
    }
}
