

package mysmartlibrary;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import java.sql.SQLException;
public class MemberController_1 {

  
    @FXML
    private Button btnAdd;
    @FXML 
    private Button btnEdit;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnRemove;

    @FXML 
    private TextField txtName;
    @FXML 
    private TextField txtContact;

    @FXML 
    private TextField searchField;
    @FXML 
    private Slider     colorSlider;
     @FXML 
     private ComboBox<String> comboBoxSort;
    @FXML
    private TableView<Member_1>  tableMembers;
    @FXML 
    private TableColumn<Member_1, String>  colName;
    @FXML
    private TableColumn<Member_1, String>  colContact;
    @FXML 
    private TableColumn<Member_1, Integer> colId;
    

    
    private final ObservableList<Member_1> members = FXCollections.observableArrayList();
     private final MemberDAO memberDAO = new MemberDAO();
   
    private static final Path DATA_DIR  = Paths.get(System.getProperty("user.home"), "mySmartLibrary");
    private static final Path DATA_FILE = DATA_DIR.resolve("members.txt");

    
    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));

     tableMembers.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    colId.setResizable(false);
   colName.setResizable(false);
   colContact.setResizable(false);

       
        tableMembers.setItems(members);
        tableMembers.setPlaceholder(new Label("No members yet"));

        
        tableMembers.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, sel) -> {
            if (sel != null) {
                txtName.setText(sel.getName());
                txtContact.setText(sel.getContact());
            }
        });

      
        if (searchField != null) {
            searchField.setOnKeyPressed(e -> { if (e.getCode() == KeyCode.ENTER) searchMembers(); });
        }
         if (comboBoxSort != null) {
        comboBoxSort.setItems(FXCollections.observableArrayList(
            "Name (A–Z)",
            "Name (Z–A)"
        ));
    }
        
        if (colorSlider != null) {
            colorSlider.setMin(0); colorSlider.setMax(1); colorSlider.setValue(1);
            Platform.runLater(() -> {
                Node root = tableMembers.getScene() != null ? tableMembers.getScene().getRoot() : null;
                if (root != null) colorSlider.valueProperty().addListener((o, ov, nv) -> root.setOpacity(nv.doubleValue()));
            });
        }

        refreshTable();
        
    }
 private void refreshTable() {
        try {
            //connect table view with the table in data base
          List<Member_1> list = memberDAO.getAllMembers();
          members.setAll(list);
          tableMembers.setItems(members);
        } catch (SQLException e) {
            warn("Could not load members:\n" + e.getMessage());
        }}
@FXML
private void addMember() {
String name = safe(txtName.getText());
        String contact = safe(txtContact.getText());
        if (name.isEmpty() || contact.isEmpty()) {
            warn("Please fill in all fields.");
            return;
        }
        try {
            memberDAO.insertMember(name, contact);
            refreshTable();
            clearForm();
        } catch (SQLException e) {
            warn(e.getMessage());
        }
}

private static String normalizeContact(String s) {
    return s == null ? "" : s.replaceAll("\\s+", "").toLowerCase();
}

private boolean contactExists(String contact, Member_1 except) {
    String c = normalizeContact(contact);
    return members.stream().anyMatch(m ->
        m != except && normalizeContact(m.getContact()).equals(c)
    );
}




@FXML
private void editSelected() {
    Member_1 sel = tableMembers.getSelectionModel().getSelectedItem();
        if (sel == null) { warn("Select a member to edit."); return; }
        String name = safe(txtName.getText());
        String contact = safe(txtContact.getText());
        if (name.isEmpty() || contact.isEmpty()) { warn("Please fill in all fields."); return; }
        try {
            memberDAO.updateMember(sel.getId(), name, contact);
            refreshTable();
            clearForm();
        } catch (SQLException e) {
            warn(e.getMessage());
        }
}



    @FXML
    private void deleteSelected() {
        Member_1 sel = tableMembers.getSelectionModel().getSelectedItem();
        if (sel == null) { warn("Select a member to delete."); return; }
        try {
            memberDAO.deleteMember(sel.getId());
            refreshTable();
            clearForm();
        } catch (SQLException e) {
            warn(e.getMessage());
        }
    }

@FXML
private void searchMembers() {
    String q = safe(searchField.getText()).toLowerCase();
        ObservableList<Member_1> result;
        if (q.isEmpty()) {
            result = FXCollections.observableArrayList(members);
        } else {
            result = members.stream()
                    .filter(m -> m.getName() != null && m.getName().toLowerCase().contains(q))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
        }
        if (result.isEmpty()) { warn("No members match your search."); }
        tableMembers.setItems(result);
}


    @FXML
    private void clearForm() {
        txtName.clear();
        txtContact.clear();
        tableMembers.getSelectionModel().clearSelection();
        tableMembers.setItems(members); 
    }

    
    private void loadMembersFromFile() throws IOException {
        
String choice = comboBoxSort.getSelectionModel().getSelectedItem();
        ObservableList<Member_1> currentList = FXCollections.observableArrayList(tableMembers.getItems());
        if (choice == null) { tableMembers.setItems(currentList); return; }
        Comparator<Member_1> byName = Comparator.comparing(m -> m.getName() == null ? "" : m.getName(),
                String.CASE_INSENSITIVE_ORDER);
        ObservableList<Member_1> sortedList;
        if (choice.equals("Name (A–Z)")) {
            sortedList = currentList.stream().sorted(byName).collect(Collectors.toCollection(FXCollections::observableArrayList));
        } else if (choice.equals("Name (Z–A)")) {
            sortedList = currentList.stream().sorted(byName.reversed()).collect(Collectors.toCollection(FXCollections::observableArrayList));
        } else {
            sortedList = currentList;
        }
        tableMembers.setItems(sortedList);
    }

    

  
   private boolean existsDuplicate(String contact, Member_1 except) {
    String c = normalizeContact(contact);
    return members.stream().anyMatch(m ->
        m != except && normalizeContact(m.getContact()).equals(c)
    );
}
   
   
    @FXML
void removemember(ActionEvent event) {
    Member_1 sel = tableMembers.getSelectionModel().getSelectedItem();
    if (sel == null) {
        warn("Select a member to remove.");
        return;
    }
    try {
        memberDAO.deleteMember(sel.getId());  
        refreshTable();                        
        clearForm();                            
    } catch (SQLException e) {
        warn(e.getMessage());
    }
}


@FXML
void sortTheTableView(ActionEvent event) {
    //user ckoice
    String choice = comboBoxSort.getSelectionModel().getSelectedItem();

  //put the shown memebers in the table view in an observable list
    ObservableList<Member_1> currentList =
            FXCollections.observableArrayList(tableMembers.getItems());

    // if nothing is choosen from a combo box default sorting is returned
    if (choice == null ) {
        tableMembers.setItems(currentList);
        return;
    }

    //to compare the members and discuss which is bigger than the other 
    Comparator<Member_1> byName = Comparator.comparing(
            //check if the name is null or no
            m -> m.getName() == null ? "" : m.getName(),
            //discuss that its insensitive order
            String.CASE_INSENSITIVE_ORDER
    );

     
    ObservableList<Member_1> sortedList;

    if (choice.equals("Name (A–Z)")) {
        sortedList = currentList.stream()
                .sorted(byName)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
   //reverse the sorting
    } else if (choice.equals("Name (Z–A)")) {
        sortedList = currentList.stream()
                .sorted(byName.reversed())
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

    } else {
        
        sortedList = currentList;
    }

    tableMembers.setItems(sortedList);
}



    private static int    parseIntSafe(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; } }
    private static String safe(String s)         {
        return s == null ? "" : s.trim(); }
    private static String nullSafe(String s)     {
        return s == null ? "" : s; }

    private void warn(String msg) {
        new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK).showAndWait(); }
}



