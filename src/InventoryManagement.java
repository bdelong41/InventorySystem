import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.Random;


/**
 * Implements the InventoryManagement Interface.
 * Use BeginStart() to instantiate the control interface.
 */
public class InventoryManagement {

    //Anchor
    @FXML
    private AnchorPane A_Anchor;
    //Buttons
    @FXML
    private Button part_add_button;
    @FXML
    private Button part_modify_button;
    @FXML
    private Button product_add_button;
    @FXML
    private Button product_modify_button;
    @FXML
    private Button exitButton;

    //TableViews
    @FXML
    TableView<Part> part_tableView;
    @FXML
    TableView<Product> product_tableView;

    //TableColumns
    @FXML
    TableColumn<Part, Integer> part_partID_tableColumn;
    @FXML
    TableColumn<Part, String> part_partName_tableColumn;
    @FXML
    TableColumn<Part, Integer> part_inventoryLevel_tableColumn;
    @FXML
    TableColumn<Part, Double> part_priceCost_tableColumn;

    @FXML
    TableColumn<Product, Integer> product_productID_tableColumn;
    @FXML
    TableColumn<Product, String> product_productName_tableColumn;
    @FXML
    TableColumn<Product, Integer> product_inventoryLevel_tableColumn;
    @FXML
    TableColumn<Product, Double> product_priceCost_tableColumn;

    //Text Fields
    @FXML
    TextField part_search_textarea;
    @FXML
    TextField product_search_textarea;

    //Inventory Objects and FilteredList(s)
    private static Inventory mainInventory = new Inventory();// Set to an empty Inventory object to avoid nullptr errors
    private static FilteredList<Part> partsFilter = new FilteredList<Part>(mainInventory.getAllParts(), p->true);
    private static FilteredList<Product> productsFilter = new FilteredList<Product>(mainInventory.getAllProducts(), p->true);

    /**
     * Sets Factory CellValue of each TableColumn.
     * and creates filter fields for both the Part table and Product table.
     */
    public void initialize(){

		//Parts Table
        part_partID_tableColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("ID"));
		part_partName_tableColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
		part_inventoryLevel_tableColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
		part_priceCost_tableColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

		//Products Table
		product_productID_tableColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        product_productName_tableColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		product_inventoryLevel_tableColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
		product_priceCost_tableColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));

        //adding a textProperty listener() to detect changes to text
        //Using newValue instead of getText() retrieves the current value stored
        part_search_textarea.textProperty().addListener((observable, oldValue, newValue)->{
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

        //adding a textProperty listener() to detect changes to text
        //Using newValue instead of getText() retrieves the current value stored
        product_search_textarea.textProperty().addListener((observable, oldValue, newValue)->{
            //Setting Predicate for filtered list
            productsFilter.setPredicate(product->{
                //creating a string filter
                String filter = newValue.toLowerCase();
                //Using the current value stored in Search_TextArea
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }

                if(product.getName().toLowerCase().contains(filter)){
                    return true;
                }
                else if (Integer.toString(product.getId()).toLowerCase().contains(filter)){
                    return true;
                }
                else{
                    ErrorDialogueBox errDiag = new ErrorDialogueBox();
                    errDiag.BeginStart("Error No such product with the name/id " + newValue +  " exists!");
                    return false;
                }
            });
        });

        SortedList<Part> partSortedData = new SortedList<>(partsFilter);
        SortedList<Product> productSortedData = new SortedList<>(productsFilter);
        partSortedData.comparatorProperty().bind(part_tableView.comparatorProperty());
        productSortedData.comparatorProperty().bind(product_tableView.comparatorProperty());
        part_tableView.setItems(partSortedData);
        product_tableView.setItems(productSortedData);
	}

    /**
     * Instantiatese and initializes a InventoryManagement object.
     * Allows data to be piped upstream to the method call.
     */
	public void BeginStart(){
        //Loader
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InventoryManagement.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Inventory Management");
            stage.setScene(new Scene(root));
            stage.show();
        }catch(Exception e){
            System.out.println("Failed to load InventoryManagement.fxml");
        }
    }

    /**
     * Creates an add_partController object and uses BeginStart() method call to initialize the add_part interface.
     */
    public void part_add_button_listener() {
        try{
        add_partController addPart = new add_partController();
        Integer id = genPartID();
        Part newPart = addPart.BeginStart(id);
        if (newPart != null) {
            mainInventory.addPart(newPart);
        }
        addPart = null;
        }catch(Exception e){
            System.out.println("Null Pointer Exception");
        }
    }
    /**
     * Creates an ModifyPart object and uses BeginStart() method call to initialize the modify_part interface.
     */
    public void part_modify_button_listener(){
        //Checking if the parts table is empty
        if(part_tableView.getItems().isEmpty()){
            //Generating error message
            ErrorDialogueBox diagBox = new ErrorDialogueBox();
            diagBox.BeginStart("Error: Unable to complete task, Part inventory is empty.");
            return;
        }
        Part part = part_tableView.getSelectionModel().getSelectedItem();
        if (part != null) {
            mainInventory.lookupPart(part.getID());
            Part newPart;
            try{
                ModifyPart controller = new ModifyPart();
                newPart = controller.BeginStart(part);
                if (newPart != null) {
                    //retrieving part index
                    Integer index = getPartIndex(newPart);
                    if(index != null) {
                        mainInventory.updatePart(index, newPart);
                        //Used to update the associated Part reference of each Product in mainInventory
                        updateCascade(newPart);
                    }
                }
                controller = null;
            }catch(Exception e){
                System.out.println("Null Pointer Exception");
            }
        }
        //User hasn't selected a part from the parts table
        else{
            //Generating error response
            ErrorDialogueBox errDiag = new ErrorDialogueBox();
            errDiag.BeginStart("Error Select a part from the table and try again.");
        }
    }

    /**
     * Removes Part from mainInventory
     */
    public void part_delete_button_listener(){
        //Checking that the parts table is not empty
        if(part_tableView.getItems().isEmpty()){
            //Generating error message
            ErrorDialogueBox diagBox = new ErrorDialogueBox();
            diagBox.BeginStart("Error: Unable to complete task, Part inventory is empty.");
            return;
        }
        Part part = part_tableView.getSelectionModel().getSelectedItem();
        if (part != null){

            //Checking if Part is associated with any product
            for(int index=0; index < mainInventory.getAllProducts().size(); index++){
                Product product = mainInventory.getAllProducts().get(index);
                for(int counter=0; counter < product.getAllAssociatedParts().size(); counter++){
                    Part assocPart = product.getAllAssociatedParts().get(counter);
                    if(assocPart.getID() == part.getID()){
                        //Generating Error Dialogue
                        ErrorDialogueBox errDiag = new ErrorDialogueBox();
                        errDiag.BeginStart("Error selected part is associated with one or more products!");
                        return;
                    }
                }
            }



            ConfirmBox confirmBox = new ConfirmBox();
            if(confirmBox.BeginStart()){
                if(!mainInventory.deletePart(part)) {
                    ErrorDialogueBox diagBox = new ErrorDialogueBox();
                    diagBox.BeginStart("Error! selected part is currently associated with product(s)");
                }
            }
        }
        //User hasn't selected a part from the parts table
        else{
            //Generating error response
            ErrorDialogueBox errDiag = new ErrorDialogueBox();
            errDiag.BeginStart("Error Select a part from the Parts table and try again.");
        }
    }
    /**
     * Creates an AddProduct controller and uses BeginStart() method call to initialize the add_product interface.
     */
    public void product_add_button_listener(){
        AddProduct addProduct = new AddProduct();
        Integer id = generate_product_id();
        Product newProduct = addProduct.BeginStart(mainInventory, id);
        if(newProduct != null) {
            mainInventory.addProduct(newProduct);
        }
        addProduct = null;
    }

    /**
     * Creates an ModifyProduct controller and uses BeginStart() method call to initialize the Modify_part interface.
     */
    public void product_modify_button_listener(){
        //Checking that the products table is not empty
        if(product_tableView.getItems().isEmpty()){
            //Generating error message
            ErrorDialogueBox diagBox = new ErrorDialogueBox();
            diagBox.BeginStart("Error: Unable to complete task, Product inventory is empty.");
            return;
        }
        Product selected = product_tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            ModifyProduct modProduct = new ModifyProduct();
            Product modifiedProduct = modProduct.BeginStart(this.mainInventory, selected);
            if(modifiedProduct != null) {
                Integer index = getProductIndex(modifiedProduct);
                mainInventory.updateProduct(index, modifiedProduct);
            }
            modifiedProduct = null;
        }
        //User hasn't selected a product from the products table
        else{
            //Generating error response
            ErrorDialogueBox errDiag = new ErrorDialogueBox();
            errDiag.BeginStart("Error Select a product from the Products table and try again.");
        }
    }

    /**
     * Removes Product from mainInventory
     */
    public void product_delete_button_listener(){
        //Checking that the products table is not empty
        if(product_tableView.getItems().isEmpty()){
            //Generating error message
            ErrorDialogueBox diagBox = new ErrorDialogueBox();
            diagBox.BeginStart("Error: Unable to complete task, Product inventory is empty.");
            return;
        }
        Product selected = product_tableView.getSelectionModel().getSelectedItem();

        if(selected != null){
            ConfirmBox confirmBox = new ConfirmBox();
            if (confirmBox.BeginStart()) {
                mainInventory.deleteProduct(selected);
            }
        }
        //User hasn't selected a product from the products table
        else{
            //Generating error response
            ErrorDialogueBox errDiag = new ErrorDialogueBox();
            errDiag.BeginStart("Error Select a product from the Products table and try again.");
        }
    }

    /**
     * Closes the Interface
     */
    public void exit(){
        ((Stage) exitButton.getScene().getWindow()).close();
    }

    /**
     * Generates a random Part ID
     * @return Integer
     */
    public Integer genPartID(){
        //Controls the upper bound of random numbers
        Integer bound = 100;
        //creating first random number
        Random rand = new Random();
        Integer id = rand.nextInt(bound);
        while(true){
            //Preventing duplicate IDs
            if (mainInventory.lookupPart(id) == null){
                return id;
            }
            //Changing the upper bound
            else if (mainInventory.getAllParts().size() > bound){
                bound = bound * 2;
            }
            //Setting id
            id = rand.nextInt(bound);
        }
    }

    /**
     * Generates random Product ID
     * @return Integer
     */
    public Integer generate_product_id(){
        //Upper bound for random number generation
        Integer bound = 10;
        Random rand = new Random();
        Integer id = rand.nextInt(bound);
        while(true){
            //Preventing duplicates
            if (mainInventory.lookupProduct(id) == null){
                return id;
            }
            //Upscaling upper bound
            else if (mainInventory.getAllProducts().size() > bound){
                bound = bound * 2;
            }
            //Setting id to random number
            id = rand.nextInt(bound);
        }
    }

    /**
     * Takes Part as arg.
     * Retrieves list index of existing Part instance from mainInventory.
     * @param part Part
     * @return Integer
     */
    public Integer getPartIndex(Part part){
        Integer index = null;
        ObservableList<Part> allparts = mainInventory.getAllParts();
        for(int counter = 0; counter < allparts.size(); counter++){
            if (allparts.get(counter).getID() == part.getID()){
                index = counter;
            }
        }
        return index;
    }

    /**
     * Takes Product as arg
     * Retrieves list index of existing Product instance from mainInventory
     * @param product Product
     * @return Integer
     */
    public Integer getProductIndex(Product product){
        Integer index = null;
        ObservableList<Product> allProducts = mainInventory.getAllProducts();
        for(int counter = 0; counter < allProducts.size(); counter++){
            if (allProducts.get(counter).getId() == product.getId()){
                index = counter;
            }
        }
        return index;
    }

    /**
     * Takes Part as arg.
     * Updates Part reference of each Products associated parts list.
     * @param part Part
     */
    public void updateCascade(Part part){
        for(int index = 0; index < mainInventory.getAllProducts().size(); index++){
            Product product = mainInventory.getAllProducts().get(index);
            for(int counter = 0; counter < product.getAllAssociatedParts().size(); counter++){
                Part assoc = product.getAllAssociatedParts().get(counter);
                if(assoc.getID() == part.getID()){
                    product.deleteAssociatedPart(part);
                    product.addAssociatedPart(part);
                }
            }
        }
    }

    /**
     * This function checks if the parts table is empty and generates an error dialogue to the user.
     * Using mainInventory allParts the parts table can be double checked, this is necessary because parts are
     * actively added and removed from the table based on criteria from the search box.
     */
   public void checkPartsTable(){
       //Checking if the part_tableView is empty
        if(part_tableView.getItems().isEmpty() && mainInventory.getAllParts().isEmpty()){
           ErrorDialogueBox errDiag = new ErrorDialogueBox();
           errDiag.BeginStart("Error Parts table is empty please add a part and try again.");
       }
    }
    /**
     * This function checks if the products table is empty and generates an error dialogue to the user.
     * Using mainInventory allProducts the products table can be double checked, this is necessary because products are
     * actively added and removed from the table based on criteria from the search box.
     */
    public void checkProductsTable(){
        //Checking if the table is empty
        if(product_tableView.getItems().isEmpty() && mainInventory.getAllProducts().isEmpty()){
            ErrorDialogueBox errDiag = new ErrorDialogueBox();
            errDiag.BeginStart("Error Products table is empty please add a product and try again.");
        }
    }

}
