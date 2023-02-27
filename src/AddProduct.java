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
 * Implements the interface for add product.
 * Instance for Stage object is self-contained in BeginStart().
 */
public class AddProduct {

    //Table Views
    @FXML
    private TableView<Part> partDataTableView;
    @FXML
    private TableView<Part> associatedPartDataTableView;
    //Total Parts
    @FXML
    TableColumn<Part, Integer> partDataIdTableColumn;
    @FXML
    TableColumn<Part, String> partDataNameTableColumn;
    @FXML
    TableColumn<Part, Integer> partDataInvTableColumn;
    @FXML
    TableColumn<Part, Double> partDataPriceTableColumn;
    //Associated Parts Table
    @FXML
    TableColumn<Part, Integer> assocPartsIdTableColumn;
    @FXML
    TableColumn<Part, String> assocPartsNameTableColumn;
    @FXML
    TableColumn<Part, Integer> assocPartsInvTableColumn;
    @FXML
    TableColumn<Part, Double> assocPartsPriceTableColumn;

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
    private TextField searchTextField;

    //Buttons
    @FXML
    private Button cancelProductButton;
    @FXML
    private Button saveProductButton;

    private static Product newProduct;
    private static Inventory mainInventory = new Inventory();


    //Initializing associatedParts to a new observable list
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private static ObservableList<Part> partsList;
    private static FilteredList<Part> partsFilter = new FilteredList<>(mainInventory.getAllParts(), p->true);

    /**
     * Handles the factory value of the TableColumns used as well as creating a filterListener for the search TextField.
     */
    public void initialize(){

        //total parts columns
        partDataIdTableColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("ID"));
        partDataNameTableColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partDataInvTableColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partDataPriceTableColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        //Associated Parts
        assocPartsIdTableColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("ID"));
        assocPartsNameTableColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        assocPartsInvTableColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        assocPartsPriceTableColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        //Making the id text field uneditable
        idTextField.setEditable(false);
        idTextField.setStyle("-fx-control-inner-background: #D3D3D3");

        //adding a textProperty listener() to detect changes to text
        //Using newValue instead of getText() retrieves the current value stored
        searchTextField.textProperty().addListener((observable, oldValue, newValue)->{
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
        //Binding the filter to part_data_tableView
        SortedList<Part> sortedPartData = new SortedList<>(partsFilter);
        sortedPartData.comparatorProperty().bind(partDataTableView.comparatorProperty());
        partDataTableView.setItems(sortedPartData);

        //Setting content for associated_part_data_tableView
        associatedPartDataTableView.setItems(associatedParts);
    }

    /**
     * Takes Inventory,int as args and initializes partsList and id TextField.
     * Returns Product if a Product is successfully created by the user, and returns null if the user cancels/quits.
     * @param inventory Inventory
     * @param id int
     * @return boolean
     */
    public Product BeginStart(Inventory inventory, int id){
        partsList = inventory.getAllParts();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add_product.fxml"));
            Parent root = loader.load();
            AddProduct addProduct = loader.getController();
            addProduct.initialize_id_text_field(id);
            Stage stage = new Stage();
            stage.setOnCloseRequest(e->{
                e.consume();
                addProduct.Close_program(stage);
            });
            stage.setTitle("Add Product");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch(Exception e){
            System.out.println("Failed to load add_product.fxml");
        }

        return newProduct;
    }

    /**
     * Takes Integer as arg
     * Sets te text of id_Modify_TextField to arg.
     * This allows the private data member to be altered from instance call.
     * @param id
     */
    public void initialize_id_text_field(Integer id){
        idTextField.setText(Integer.toString(id));
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
    public void save_button_listener(){
        try {
            final Integer id = Integer.parseInt(idTextField.getText());
            final String name = this.nameTextField.getText();
            final Integer inv = Integer.parseInt(invTextField.getText());
            final Double price = Double.parseDouble(priceTextField.getText());
            final Integer max = Integer.parseInt(maxTextField.getText());
            final Integer min = Integer.parseInt(minTextField.getText());

            //Resetting inventory text borders
            invTextField.setStyle("");
            maxTextField.setStyle("");
            minTextField.setStyle("");

            if (min > max) {
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
            if(this.nameTextField.getText().isEmpty()){
                ErrorDialogueBox diagBox = new ErrorDialogueBox();
                diagBox.BeginStart("Error name field cannot be left blank");
                return;
            }

            this.newProduct = new Product(id, name, price, inv, min, max);
            for (int index = 0; index < associatedParts.size(); index++) {
                newProduct.addAssociatedPart(associatedParts.get(index));
            }
            Stage stage = (Stage) saveProductButton.getScene().getWindow();
            stage.close();
        }
        //Exception is generated when the data inside a text field cannot be parsed into an Integer or Double
        catch(Exception inputException){
            System.out.println("Error, Invalid Input");
            ErrorDialogueBox diagBox = new ErrorDialogueBox();
            diagBox.BeginStart("ERROR! please enter the correct data in the appropriate fields!");

        }
    }

    /**
     * Called when the user adds a part to the product.
     * Part reference is added from part_data_tableView to associatedParts.
     */
    public void add_button_listener(){
        //Checking if partTableView is populated
        if(partDataTableView.getItems().isEmpty()){
            //Generating error response
            ErrorDialogueBox errDiag = new ErrorDialogueBox();
            errDiag.BeginStart("Error No Par(s) found");
        }
        Part part = partDataTableView.getSelectionModel().getSelectedItem();
        if (part != null){
            associatedParts.add(part);
            //Alteration detected
        }
        //User hasn't selected a part from the part table view
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
        if(associatedPartDataTableView.getItems().isEmpty()){
            //Generating error response
            ErrorDialogueBox errDiag = new ErrorDialogueBox();
            errDiag.BeginStart("Error No associated part(s) found");
        }
        Part part = associatedPartDataTableView.getSelectionModel().getSelectedItem();
        if (part != null){
            ConfirmBox confirmBox = new ConfirmBox();
            if(confirmBox.BeginStart()) {
                //Detects whether a part is added to or removed from associated parts
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
            System.out.println("Error Nullptr returned!");
        }
    }

    /**
     * Takes Stage instance as arg
     * This function takes a stage instance as argument and calls its close() function. This fixed an issue with calling
     * Close_program from within BeginStart() as a Stage instance with a value of null would be called instead.
     * Close_program implements "close_program.fxml" to confirm the user's action. If the user applies their changes
     * the function save_button_listener() is called.
     * @param stage Stage
     */
    public void Close_program(Stage stage){

        CloseProgram closeProgram = new CloseProgram();
        final String tf = closeProgram.BeginStart();
        if (tf == "apply") {
            save_button_listener();
        } else if (tf == "dont apply") {
            newProduct = null;
            Cancel_ButtonListener();
        }
    }
    /**
     * This function checks if the parts table is empty and generates an error dialogue to the user.
     * Using mainInventory allParts the parts table can be double checked, this is necessary because parts are
     * actively added and removed from the table based on criteria from the search box.
     */
    public void checkPartsTable(){
        //Checking if the part_tableView is empty
        if(partDataTableView.getItems().isEmpty() && mainInventory.getAllParts().isEmpty()){
            ErrorDialogueBox errDiag = new ErrorDialogueBox();
            errDiag.BeginStart("Error There are no parts in the parts table please add a part and try again.");
        }
    }
}
