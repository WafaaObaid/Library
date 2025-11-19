package mysmartlibrary;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.HashSet;
import javafx.beans.property.SimpleStringProperty;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import mysmartlibrary.Borrowing_1;

public class BorrowingController implements Initializable {
      @FXML
    private HBox TitleHbox;

    @FXML
    private ImageView book1;

    @FXML
    private TableColumn<Borrowing_1, String> bookCol;

    @FXML
    private ComboBox<Integer> bookComboBox;

    @FXML
    private DatePicker borrowDate2;

    @FXML
    private Label borrowDateLabel2;

    @FXML
    private TableColumn<Borrowing_1,String> borrowingDateCol;

    @FXML
    private Button btnBorrow;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnExit;

    @FXML
    private Button clickSearch2;

    @FXML
    private Label header;

    @FXML
    private Label label;

    @FXML
    private TableColumn<Borrowing_1, String> memberCol;

    @FXML
    private ComboBox<Integer> memberComboBox;

    @FXML
    private BorderPane root;

    @FXML
    private Label searchLable2;

    @FXML
    private TextField searchTextField2;

    @FXML
    private Label sort2;

    @FXML
    private ComboBox<String> sortCombbox2;
    
     @FXML
    private Button btnDelete;
     @FXML
   private Button btnEdit;
     @FXML 
    private ImageView book2;

    @FXML
    private TableView<Borrowing_1> tableView;
    
    private final BorrowingDAO borrowingDAO = new BorrowingDAO();
    private final BookDAO bookDAO = new BookDAO();
    private final MemberDAO memberDAO = new MemberDAO();

    private final Map<Integer, String> bookIdToTitle = new HashMap<>();
    private final Map<Integer, String> memberIdToName = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TableView columns
        bookCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getBookTitle()));
        memberCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMemberName()));
        borrowingDateCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getBorrowDate()));

        sortCombbox2.getItems().setAll("Ascending", "Descending");

        btnBorrow.setOnAction(e -> borrowBook(e));
        btnClear.setOnAction(e -> clearForm());
        btnExit.setOnAction(e -> exitStage());
        btnDelete.setOnAction(e -> deleteBorrowing());

        borrowDate2.setValue(LocalDate.now());

        try {
            loadBooks();
            loadMembers();
            wireConverters();
            refreshTable();
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load data", ex.getMessage());
        }
    }

    private void loadBooks() throws Exception {
        bookIdToTitle.clear();
        bookDAO.getAllBooks().forEach(b -> bookIdToTitle.put(b.getId(), b.getTitle()));
        bookComboBox.getItems().setAll(bookIdToTitle.keySet());
    }

    private void loadMembers() throws Exception {
        memberIdToName.clear();
        memberDAO.getAllMembers().forEach(m -> memberIdToName.put(m.getId(), m.getName()));
        memberComboBox.getItems().setAll(memberIdToName.keySet());
    }

    private void wireConverters() {
        bookComboBox.setConverter(new StringConverter<>() {
            @Override public String toString(Integer id) {
                return id == null ? "" : bookIdToTitle.getOrDefault(id, "Book #" + id);
            }
            @Override public Integer fromString(String s) { return null; }
        });
        memberComboBox.setConverter(new StringConverter<>() {
            @Override public String toString(Integer id) {
                return id == null ? "" : memberIdToName.getOrDefault(id, "Member #" + id);
            }
            @Override public Integer fromString(String s) { return null; }
        });
    }

    private void refreshTable() {
        try {
            List<Borrowing_1> borrowings = borrowingDAO.getAllBorrowings();
            tableView.getItems().setAll(borrowings);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot load borrowings", e.getMessage());
        }
    }

    @FXML
    private void borrowBook(ActionEvent event) {
        Integer bookId = bookComboBox.getValue();
        Integer memberId = memberComboBox.getValue();
        LocalDate date = borrowDate2.getValue();

        if (bookId == null || memberId == null || date == null) {
            showAlert(Alert.AlertType.WARNING, "Missing Data", "Select book, member, and date", null);
            return;
        }

        Borrowing_1 borrowing = new Borrowing_1(
                0,
                bookId,
                memberId,
                date.toString(),
                "",
                memberIdToName.get(memberId),
                bookIdToTitle.get(bookId)
        );

        try {
            borrowingDAO.insertBorrowing(borrowing);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Borrowing recorded", null);
            refreshTable();
            clearForm();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot add borrowing", e.getMessage());
        }
    }

    private void deleteBorrowing() {
        Borrowing_1 selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No selection", "Select a borrowing to delete", null);
            return;
        }
        try {
            borrowingDAO.deleteBorrowing(selected.getId());
            showAlert(Alert.AlertType.INFORMATION, "Deleted", "Borrowing deleted", null);
            refreshTable();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot delete borrowing", e.getMessage());
        }
    }

    @FXML
    void clearForm() {
        bookComboBox.getSelectionModel().clearSelection();
        memberComboBox.getSelectionModel().clearSelection();
        borrowDate2.setValue(LocalDate.now());
    }

    @FXML
    void exitStage() {
        Stage st = (Stage) root.getScene().getWindow();
        st.close();
    }

    @FXML
    void handleClickSearch() {
        String query = searchTextField2.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            refreshTable();
            return;
        }

        try {
            List<Borrowing_1> filtered = borrowingDAO.getAllBorrowings().stream()
                    .filter(b -> b.getBookTitle().toLowerCase().contains(query)
                            || b.getMemberName().toLowerCase().contains(query)
                            || b.getBorrowDate().contains(query))
                    .collect(Collectors.toList());
            tableView.getItems().setAll(filtered);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot search borrowings", e.getMessage());
        }
    }

    @FXML
    void handleSortCombbox() {
        String choice = sortCombbox2.getValue();
        if (choice == null) return;

        List<Borrowing_1> borrowings = new ArrayList<>(tableView.getItems());
        borrowings.sort(Comparator.comparing(Borrowing_1::getBorrowDate));
        if ("Descending".equals(choice)) borrowings.sort(Comparator.comparing(Borrowing_1::getBorrowDate).reversed());
        tableView.getItems().setAll(borrowings);
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(header);
        a.setContentText(content);
        a.showAndWait();
    }
    @FXML
    void editBorrowing(ActionEvent event) {
        Borrowing_1 selected = tableView.getSelectionModel().getSelectedItem();
    if (selected == null) {
        showAlert(Alert.AlertType.WARNING, "No selection", "Select a borrowing to edit", null);
        return;
    }

    Integer newBookId = bookComboBox.getValue();
    Integer newMemberId = memberComboBox.getValue();
    LocalDate newDate = borrowDate2.getValue();

    if (newBookId == null || newMemberId == null || newDate == null) {
        showAlert(Alert.AlertType.WARNING, "Missing Data", "Choose book, member, and date", null);
        return;
    }

    selected.setBookId(newBookId);
    selected.setMemberId(newMemberId);
    selected.setBorrowDate(newDate.toString());

    try {
        BorrowingDAO dao = new BorrowingDAO();
        dao.updateBorrowing(selected);  // تحديث البيانات في قاعدة البيانات
        showAlert(Alert.AlertType.INFORMATION, "Success", "Borrowing updated successfully", null);
        refreshTable(); // إعادة تحميل TableView
    } catch (Exception e) {
        showAlert(Alert.AlertType.ERROR, "Error", "Cannot update borrowing", e.getMessage());
    }
    }
    
   
    @FXML
    void handleBorrowDate(ActionEvent event) {
    }
    @FXML
    void handleSearchField(ActionEvent event) {

    }
    @FXML
    void chooseBook(ActionEvent event) {
    }

    @FXML
    void chooseMember(ActionEvent event) {
    }
}



