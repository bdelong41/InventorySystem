import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Implements the Interface for ModifyProduct.
 * Uses BeginStart to initialize and control the interface.
 */
public class ModifyProduct {

    //Table Views
    @FXML
    private TableView<Part> partTableview;
    @FXML
    private TableView<Part> associatedPartsTableView;

    //TableColumns
    @FXML
    private TableColumn idTableColumn;
    @FXML
    private TableColumn nameTableColumn;
    @FXML
    private TableColumn invTableColumn;
    @FXML
    private TableColumn priceTableColumn;
    @FXML
    private TableColumn assocIdColumn;
    @FXML
    private TableColumn assocNameColumn;
    @FXML
    private TableColumn assocInvColumn;
    @FXML
    private TableColumn assocPriceColumn;

    //Text Fields
    @FXML
    private TextField idTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField invTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextField maxTextField;
    @FXML
    private TextField minTextField;
    @FXML
    private TextField searchTextArea;

    //Buttons
    @FXML
    private Button cancelProductButton;
    @FXML
    private Button saveProductButton;


    private static Product newProduct;
    //Initializing associatedParts to a new observable list
    private static Inventory mainInventory = new Inventory();
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private static ObservableList<Part> partsList = mainInventory.getAllParts();
    private static FilteredList<Part> partsFilter = new FilteredList<>(partsList, p->true);
    private static Product referenceProduct;
    private Integer changeDetectToken = 0;

    /**
     * Sets the Factory CellValue of each TableColumn and creates a filter field for Modify_part_TableView.
     */
    public void initialize(){

        //total parts columns
        idTableColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("ID"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        invTableColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        priceTableColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        //Associated Parts
        assocIdColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("ID"));
        assocNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        assocInvColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        assocPriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        //adding a textProperty listener() to detect changes to text
        //Using newValue instead of getText() retrieves the current value stored
        searchTextArea.textProperty().addListener((observable, oldValue, newValue)->{
            //Setting Predicate for filtered list
            partsFilter.setPredicate(part->{
                //creating a string filter
                String filter = newValue.toLowerCase();
                //Using the current value stored in Search_TextArea
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }

                if(part.getName().toLowerCase().contains(filter)){
                    return true;
                }
                else if (Integer.toString(part.getID()).toLowerCase().contains(filter)){
                    return true;
                }
                else{
                    ErrorDialogueBox errDiag = new ErrorDialogueBox();
                    errDiag.BeginStart("Error No such part with the name/id " + newValue +  " exists!");
                    return false;
                }
            });
        });

            SortedList<Part> sortedPartData = new SortedList<>(partsFilter);
            sortedPartData.comparatorProperty().bind(partTableview.comparatorProperty());
            partTableview.setItems(sortedPartData);
            associatedPartsTableView.setItems(associatedParts);
    }

    /**
     * Takes Product as arg.
     * Sets the text of each textField to the corresponding data members of arg.
     * @param product Product
     */
    public void initialize_text_fields(Product product){
        idTextField.setText(Integer.toString(product.getId()));
        nameTextField.setText(product.getName());
        invTextField.setText(Integer.toString(product.getStock()));
        minTextField.setText(Integer.toString(product.getMin()));
        maxTextField.setText(Integer.toString(product.getMax()));
        priceTextField.setText(Double.toString(product.getPrice()));

        referenceProduct = product;
        associatedParts.addAll(product.getAllAssociatedParts());
    }
    /**
     * Takes Inventory,int as args and initializes partsList and id TextField.
     * Returns Product if a Product is successfully created by the user, and returns null if the user cancels/quits.
     * @param inventory Inventory
     * @param product Product
     * @return boolean
     */
    public Product BeginStart(Inventory inventory, Product product){

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modify_product.fxml"));
            Parent root = loader.load();
            ModifyProduct modProduct = loader.getController();


            modProduct.initialize_text_fields(product);

            Stage stage = new Stage();
            stage.setOnCloseRequest(e->{
                e.consume();
                modProduct.close_window(stage);
            });
            stage.setTitle("Modify Product");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch(Exception e){
            System.out.println("Failed to load modify_product.fxml");
        }
        return newProduct;
    }
    /**
     * Controls closing protocols for the interface.
     * Closes the interface and sets newProduct as a nullptr.
     */
    public void Cancel_ButtonListener(){
        Stage stage = (Stage) cancelProductButton.getScene().getWindow();
        newProduct = null;
        stage.close();
    }
    /**
     * Checks user input, ensuring the data is consistent and accurate.
     */
    public void save_button_listener() {
        try {
            final Integer id = Integer.parseInt(idTextField.getText());
            final String name = nameTextField.getText();
            final Integer inv = Integer.parseInt(invTextField.getText());
            final Double price = Double.parseDouble(priceTextField.getText());
            final Integer max = Integer.parseInt(maxTextField.getText());
            final Integer min = Integer.parseInt(minTextField.getText());

            //Resetting inventory text borders
            invTextField.setStyle("");
            maxTextField.setStyle("");
            minTextField.setStyle("");

            if (max < min) {
                minTextField.setStyle("-fx-text-box-border: red");
                ErrorDialogueBox diagBox = new ErrorDialogueBox();
                diagBox.BeginStart("Error Maximum Inventory cannot be less than Minimum Inventory");
                newProduct = null;
                return;
            }
            else if (inv < min) {
                invTextField.setStyle("-fx-text-box-border: red");
                ErrorDialogueBox diagBox = new ErrorDialogueBox();
                diagBox.BeginStart("Error Inventory cannot be less than Minimum Inventory");
                newProduct = null;
                return;
            }
            else if (inv > max) {
                maxTextField.setStyle("-fx-text-box-border: red");
                ErrorDialogueBox diagBox = new ErrorDialogueBox();
                diagBox.BeginStart("Error Inventory cannot be greater than Maximum Inventory");
                newProduct = null;
                return;
            }

            //Checking if name text field has been left blank
            if(nameTextField.getText().isEmpty()){
                ErrorDialogueBox diagBox = new ErrorDialogueBox();
                diagBox.BeginStart("Error name field cannot be left blank");
                return;
            }

            newProduct = new Product(id, name, price, inv, min, max);
            for (int index = 0; index < associatedParts.size(); index++) {
                newProduct.addAssociatedPart(associatedParts.get(index));
            }
            Stage stage = (Stage) saveProductButton.getScene().getWindow();
            stage.close();
        }
        //Exception is generated when the data inside a text field cannot be parsed into an Integer or Double
        catch (Exception invalidInput) {
            System.out.println("Error, Invalid Input");
            ErrorDialogueBox diagBox = new ErrorDialogueBox();
            diagBox.BeginStart("ERROR! please enter the correct data in the appropriate fields!");
        }
    }
    /**
     * Called when the user adds a part to the product.
     * Part reference is added from part_data_tableView to associatedParts.
     * trigger is set to true to indicate that alterations have been made.
     */
    public void add_button_listener(){
        //Checking if partTableView is populated
        if(partTableview.getItems().isEmpty()){
            //Generating error response
            ErrorDialogueBox errDiag = new ErrorDialogueBox();
            errDiag.BeginStart("Error No Par(s) found");
        }
        Part part = partTableview.getSelectionModel().getSelectedItem();
        if (part != null){
            //Detects whether a part is added to or removed from associated parts
            changeDetectToken = 1;
            associatedParts.add(part);
        }
        //User hasn't selected a part from the parts table
        else{
            //Generating error response
            ErrorDialogueBox errDiag = new ErrorDialogueBox();
            errDiag.BeginStart("Error Select a part from the table and try again.");
        }
    }
    /**
     * Removes a Part from associated_part_data_tableView.
     */
    public void remove_assoc_button_listener(){
        //Checking if associatedPartsTableView is populated
        if(associatedPartsTableView.getItems().isEmpty()){
            //Generating error response
            ErrorDialogueBox errDiag = new ErrorDialogueBox();
            errDiag.BeginStart("Error No associated part(s) found");
        }
        Part part = associatedPartsTableView.getSelectionModel().getSelectedItem();
        if (part != null){
            ConfirmBox confirmBox = new ConfirmBox();
            if(confirmBox.BeginStart()) {
                //Detects whether a part is added to or removed from associated parts
                changeDetectToken = 1;
                for (int index = 0; index < associatedParts.size(); index++) {
                    if (associatedParts.get(index).getID() == part.getID()) {
                        associatedParts.remove(index);
                        break;
                    }
                }
            }
        }
        //User hasn't selected a part from the associated parts table
        else{
            //Generating error response
            ErrorDialogueBox errDiag = new ErrorDialogueBox();
            errDiag.BeginStart("Error Select a part from the table and try again.");
        }
    }
    /**
     * Takes Product arg
     * Returns boolean.
     * This function determines if the user has made any modifications, allowing the program to close without prompting
     * confirmation if no changes were detected.
     * @param product Product
     * @return boolean
     */
    public boolean change_detect(Product product){

        if(!idTextField.getText().equals(Integer.toString(product.getId()))){
            return true;
        }
        else if (!nameTextField.getText().equals(product.getName())){
            return true;
        }
        else if (!invTextField.getText().equals(Integer.toString(product.getStock()))){
            return true;
        }
        else if (!priceTextField.getText().equals(Double.toString(product.getPrice()))){
            return true;
        }
        else if (!minTextField.getText().equals(Integer.toString(product.getMin()))){
            return true;
        }
        else if (!maxTextField.getText().equals(Integer.toString(product.getMax()))){
            return true;
        }
        else {
            if (changeDetectToken != 0){
                return true;
            }
        }
        return false;
    }
    /**
     * Takes Stage instance as arg
     * This function takes a stage instance as argument and calls its close() function. This fixed an issue with calling
     * Close_program from within BeginStart() as a Stage instance with a value of null would be called instead.
     * Close_program implements "close_program.fxml" to confirm the user's action. If the user applies their changes
     * the function save_button_listener() is called.
     * @param stage Stage
     */
    public void close_window(Stage stage){
        if (change_detect(referenceProduct)) {
            CloseProgram close = new CloseProgram();
            String tf = close.BeginStart();
            if (tf == "apply") {
                changeDetectToken = 0;
                save_button_listener();
            }
            else if (tf == "dont apply") {
                changeDetectToken = 0;
                Cancel_ButtonListener();
            }
        }
        else{
            changeDetectToken = 0;
            Cancel_ButtonListener();
        }
    }

    public Product getNewProduct() {
        return newProduct;
    }
    /**
     * This function checks if the parts table is empty and generates an error dialogue to the user.
     * Using mainInventory allParts the parts table can be double checked, this is necessary because parts are
     * actively added and removed from the table based on criteria from the search box.
     */
    public void checkPartsTable(){
        //Checking if the part_tableView is empty
        if(partTableview.getItems().isEmpty() && mainInventory.getAllParts().isEmpty()){
            ErrorDialogueBox errDiag = new ErrorDialogueBox();
            errDiag.BeginStart("Error There are no parts in the parts table please add a part and try again.");
        }
    }
}
