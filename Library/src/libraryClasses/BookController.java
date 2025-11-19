
package mysmartlibrary;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import mysmartlibrary.Book;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;



public class BookController implements Initializable {
   
     @FXML
    private Button addButton;

    @FXML
    private Label authorLabel;

    @FXML
    private TableColumn<Book, String> authorTable;

    @FXML
    private TextField authorTextField;

    @FXML
    private Button backButton;

    @FXML
    private ComboBox<String> comboxStatus;

    @FXML
    private Button editButton;

    @FXML
    private TableColumn<Book, Integer> idTable;

    @FXML
    private TableColumn<Book, Integer> salesTable;

    @FXML
    private Button saveButton;

    @FXML
    private Label statusLabel;

    @FXML
    private TableColumn<Book, String> statusTable;

    @FXML
    private TableView<Book> tableView;

    @FXML
    private Label titleLabel;

     @FXML
    private Button deleteButton;
     
    @FXML
    private TableColumn<Book, String> titleTable;

    @FXML
    private TextField titleTextField;
    
    @FXML
    private Button clickSearchButton;
    @FXML
    private ComboBox<String> combBoxSort;
      @FXML
    private TextField searchField;

    @FXML
    private Label searchLabel;

    @FXML
    private Label sortLabel;


    @FXML
    private Label viewLabel;
   
    private ObservableList<Book> bookTable = FXCollections.observableArrayList();
        private BookDAO dao = new BookDAO();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboxStatus.getItems().addAll("Available"," Borrowed");
        comboxStatus.getSelectionModel().selectFirst();
        
        combBoxSort.getItems().addAll(
                "Title - Asce",
                "Title - Desce",
                "Author - Asce",
                "Author - Desce"
        );
        combBoxSort.setOnAction(event->handleCombBox(event));
        
        
        idTable.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleTable.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorTable.setCellValueFactory(new PropertyValueFactory<>("author"));
        salesTable.setCellValueFactory(new PropertyValueFactory<>("numberOfSales"));
        
        statusTable.setCellValueFactory(new PropertyValueFactory<>("status"));


        tableView.setItems(bookTable);
         loadBooksFromDB();


    }  
    private void loadBooksFromDB() {
        bookTable.clear();
        try {
            List<Book> list = dao.getAllBooks();
            bookTable.addAll(list);
        } catch (SQLException e) {
            showAlert("Database Error", "Could not load books: " + e.getMessage());
        }
    }
    
    
    public void showAlert(String title, String messege){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(messege);
        alert.showAndWait();
    }

    
    public void clearInputField(){
        titleTextField.clear();
        authorTextField.clear();
        comboxStatus.getSelectionModel().selectFirst();
    }
    
    
    @FXML
    public void handleAdd(ActionEvent event) throws FileNotFoundException {
    
        String title = titleTextField.getText().trim();
        String author = authorTextField.getText().trim();
        String status = comboxStatus.getSelectionModel().getSelectedItem();

        if (title.isEmpty() || author.isEmpty()) {
            showAlert("Empty field", "Please fill the field");
            return;
        }

        if (!author.matches("[a-zA-Z\\s]+")) {
            showAlert("Invalid Author name", "Author name must be alphabetic");
            return;
        }

        boolean exists = bookTable.stream()
                .anyMatch(b -> b.getTitle().equalsIgnoreCase(title));
        if (exists) {
            showAlert("Duplication", "This book already exists");
            return;
        }

        Book newBook = new Book(0, title, author, status, 0);
        try {
           
            dao.insertBook(newBook);
            bookTable.add(newBook);
            showAlert("Success", "Book added");
            clearInputField();
        } catch (SQLException e) {
            String msg = e.getMessage().toLowerCase();
            if (msg.contains("duplicate") || msg.contains("unique")) {
                showAlert("Duplication", "This book title already exists in database.");
            } else {
                showAlert("Database Error", "Could not add book: " + e.getMessage());
            }
        }
   
      
    }

    @FXML
   public void handleBack(ActionEvent event) throws Exception {
       SceneManager.loadScene("/mysmartlibrary/DashBord.fxml", event);

    }

    @FXML
   public void handleEdit(ActionEvent event) {
  Book selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a book to edit");
            return;
        }
        titleTextField.setText(selected.getTitle());
        authorTextField.setText(selected.getAuthor());
        comboxStatus.getSelectionModel().select(selected.getStatus());
        addButton.setDisable(true);
        saveButton.setDisable(false);
      
    }

    @FXML
   public void handleSave(ActionEvent event) throws FileNotFoundException {
       Book selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        if (titleTextField.getText().isEmpty() || authorTextField.getText().isEmpty()) {
            showAlert("Empty field", "Please fill the field");
            return;
        }

        selected.setTitle(titleTextField.getText().trim());
        selected.setAuthor(authorTextField.getText().trim());
        selected.setStatus(comboxStatus.getSelectionModel().getSelectedItem());

        try {
            dao.updateBook(selected);
            tableView.refresh();
            showAlert("Success", "Book updated");
            clearInputField();
            addButton.setDisable(false);
            saveButton.setDisable(true);
        } catch (SQLException e) {
            showAlert("Database Error", "Could not update book: " + e.getMessage());
        }
        }
  @FXML
    public void handleDelete(ActionEvent event) throws FileNotFoundException {
 Book selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a book to delete.");
            return;
        }

        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to delete the book: " + selected.getTitle() + "?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                dao.deleteBook(selected.getId());
                bookTable.remove(selected);
                showAlert("Success", "Book deleted successfully.");
            } catch (SQLException e) {
                showAlert("Database Error", "Could not delete book: " + e.getMessage());
            }
        }

    }
  
    public void handleClickSreach(ActionEvent event) {

 String term = searchField.getText().trim();
        if (term.isEmpty()) {
            loadBooksFromDB();
            return;
        }
        try {
            List<Book> found = dao.searchByTitleOrAuthor(term);
            if (found.isEmpty()) {
                showAlert("No Results", "Cannot find the searched book/author");
            }
            bookTable.setAll(found);
        } catch (SQLException e) {
            showAlert("Database Error", "Search failed: " + e.getMessage());
        }
    }
    @FXML
    public void handleCombBox(ActionEvent event) {
          String selected = combBoxSort.getValue();
    if (selected == null) return;

    ObservableList<Book> sorted = bookTable.stream()
        .sorted(sortValues(selected))
        .collect(Collectors.toCollection(FXCollections::observableArrayList));
    tableView.setItems(sorted);
    }
    
    private Comparator<Book> sortValues(String sortOption){
        switch(sortOption){
            case "Title - Asce":
                return Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER);
            case "Title - Desce":
                return Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER).reversed();
            case "Author - Asce":
                return Comparator.comparing(Book::getAuthor, String.CASE_INSENSITIVE_ORDER);
            case "Author - Desce":
                 return Comparator.comparing(Book::getAuthor, String.CASE_INSENSITIVE_ORDER).reversed();
            default:
                  return Comparator.comparing(Book::getId); 
                
        }
    }
    
   
    
}
