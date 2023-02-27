import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controls the 'Modify Part' interface
 * Using a Part as reference to populate the proper fields the user can generate a new Part object.
 */
public class ModifyPart {
    //TextFields
    @FXML
    private TextField machineidTextField;
    @FXML
    private TextField companyNameTextField;
    @FXML
    private TextField maxTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextField invTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField idTextfield;
    @FXML
    private TextField minTextField;

    //RadioButtons
    @FXML
    private RadioButton inHouseRadioButton;
    @FXML
    private RadioButton outsourcedRadioButton;

    //Save/Cancel Buttons
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    //labels
    @FXML
    private Label companyNameLabel;
    @FXML
    private Label machineidLabel;

    static Integer radioOption = 0;
    private static Part newPart;
    private static Part referencePart;

    /**
     *Performs initial populating of TextField(s), as well as type selection
     *
     */
    public void initialize(){
        try {
            //Determining class type of referencePart
            if (referencePart.getClass().getName() == "Outsourced") {
                Outsourced var = (Outsourced) referencePart;
                companyNameTextField.setText(var.getCompanyName());
                outsourcedRadioButton.setSelected(true);
                enable_prompt(false, machineidTextField, machineidLabel);
            } else if (referencePart.getClass().getName() == "InHouse") {
                InHouse var = (InHouse) referencePart;
                machineidTextField.setText(Integer.toString(var.getMachineID()));
                inHouseRadioButton.setSelected(true);
                enable_prompt(false, companyNameTextField, companyNameLabel);
            }
        }catch(Exception e){
            System.out.println("Error, Part type could not be determined");
        }
        //Populating text fields
        idTextfield.setText(Integer.toString(referencePart.getID()));
        nameTextField.setText(referencePart.getName());
        invTextField.setText(Integer.toString(referencePart.getStock()));
        minTextField.setText(Integer.toString(referencePart.getMin()));
        maxTextField.setText(Integer.toString(referencePart.getMax()));
        priceTextField.setText(Double.toString(referencePart.getPrice()));

        //Making the id text field uneditable
        idTextfield.setEditable(false);
        idTextfield.setStyle("-fx-control-inner-background: #D3D3D3");//Greying out the field
    }

    /**
     * Takes Part as arg, initializes static member referencePart and returns a Part object.
     * Instantiates and initializes ModifyPart controller object as well as the Stage object.
     * @param part Part
     * @return Part
     */
    public Part BeginStart(Part part){
        //Using static newPart is initialized here
        referencePart = part;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modify_part.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            ModifyPart modPart = loader.getController();
            stage.setTitle("Modify Part");
            stage.setScene(new Scene(root));
            //Overloading close operations for exit button
            stage.setOnCloseRequest(e->{
                e.consume();
                modPart.close_window(stage);
            });
            stage.showAndWait();
        } catch(Exception e) {
            System.out.println("Failed to load modify_part.fxml");
        }
        return newPart;
    }

    /**
     * called whenever a radio button is clicked to hide and reveal the appropriate fields
     */
    public void radio_button_listener() {
        if (inHouseRadioButton.isSelected()) {
            radioOption = 1;
            enable_prompt(true, machineidTextField, machineidLabel);
            enable_prompt(false, companyNameTextField, companyNameLabel);
        } else if (outsourcedRadioButton.isSelected()) {
            radioOption = 2;
            enable_prompt(true, companyNameTextField, companyNameLabel);
            enable_prompt(false, machineidTextField, machineidLabel);
        }
    }

    /**
     * Called when the user clicks cancel.
     * Setting newPart to null notifies InventoryManagement controller the user canceled as newPart is returned up the
     * pipeline.
     */
    public void Cancel_ButtonListener(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        newPart = null;
        stage.close();
    }

    /**
     * Handles that validation and storage of user input
     * @return boolean
     */
    public boolean Save_ButtonListener(){
        try {

            //validating user input
            Integer maxInv = Integer.parseInt(maxTextField.getText());
            Integer minInv = Integer.parseInt(minTextField.getText());
            Integer currentInv = Integer.parseInt(invTextField.getText());

            //Resetting inventory text borders
            invTextField.setStyle("");
            maxTextField.setStyle("");
            minTextField.setStyle("");

            if (minInv > maxInv) {
                maxTextField.setStyle("-fx-text-box-border: red");
                ErrorDialogueBox diagBox = new ErrorDialogueBox();
                diagBox.BeginStart("Error Maximum Inventory cannot be smaller than Minimum Inventory");
                newPart = null;
                return(false);
            }
            else if (currentInv < minInv) {
                invTextField.setStyle("-fx-text-box-border: red");
                ErrorDialogueBox diagBox = new ErrorDialogueBox();
                diagBox.BeginStart("Error Inventory cannot be smaller than Minimum Inventory");
                newPart = null;
                return(false);
            }
            else if (currentInv > maxInv) {
                invTextField.setStyle("-fx-text-box-border: red");
                ErrorDialogueBox diagBox = new ErrorDialogueBox();
                diagBox.BeginStart("Error Inventory cannot be greater than Maximum Inventory");
                newPart = null;
                return(false);

            }

            //Checking if the companyName field has been left blank
            if(outsourcedRadioButton.isSelected()) {
                if (companyNameLabel.getText().isEmpty()) {
                    ErrorDialogueBox diagBox = new ErrorDialogueBox();
                    diagBox.BeginStart("Error company name field cannot be left blank");
                    return false;
                }
            }

            //Checking if name or companyName field has been left blank
            if(nameTextField.getText().isEmpty()){
                ErrorDialogueBox diagBox = new ErrorDialogueBox();
                diagBox.BeginStart("Error name field cannot be left blank");
                return false;
            }
            else if(outsourcedRadioButton.isSelected()) {
                if (companyNameTextField.getText().isEmpty()) {
                    ErrorDialogueBox diagBox = new ErrorDialogueBox();
                    diagBox.BeginStart("Error company name field cannot be left blank");
                    return false;
                }
            }

            //Gathering user input
            final Integer id = Integer.parseInt(idTextfield.getText());
            final String name = nameTextField.getText();
            final Integer inv = Integer.parseInt(invTextField.getText());
            final Double price = Double.parseDouble(priceTextField.getText());
            final Integer max = Integer.parseInt(maxTextField.getText());
            final Integer min = Integer.parseInt(minTextField.getText());

            //Handling the gathering of data for each Part subclass
            if (inHouseRadioButton.isSelected()) {
                //Specific to InHouse Components
                final Integer machineID = Integer.parseInt(machineidTextField.getText());
                this.newPart = new InHouse(id, name, price, inv, min, max, machineID);
            } else if (outsourcedRadioButton.isSelected()) {
                //Specific to Outsourced Components
                final String companyName = companyNameTextField.getText();
                this.newPart = new Outsourced(id, name, price, inv, min, max, companyName);
            }
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        }
        //Exception is generated when the data inside a text field cannot be parsed into an Integer or Double
        catch(Exception fieldException){
            System.out.println("Error, Incorrect Data Entry");
            ErrorDialogueBox diagBox = new ErrorDialogueBox();
            //Displaying the error to the user
            diagBox.BeginStart("ERROR! please enter the correct data in the appropriate fields!");
            newPart = null;
            return(false);
        }
        return (true);
    }
    /**
     * Takes boolean,TextField,Label as argument
     * textField's and label's visibility status it determined by boolean arg
     * @param tf boolean
     * @param textField TextField
     * @param label Label
     */
    public void enable_prompt(boolean tf, TextField textField, Label label){
        textField.setVisible(tf);
        label.setVisible(tf);
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
        //Checking for changes made
        if (change_detect(referencePart)) {
            CloseProgram close = new CloseProgram();
            String tf = close.BeginStart();
            if (tf == "apply") {
                Save_ButtonListener();
            }
            else if (tf == "dont apply") {
                Cancel_ButtonListener();
            }
        }
        else{
            Cancel_ButtonListener();
        }
    }

    /**
     * Takes Part arg
     * Returns boolean.
     * This function determines if the user has made any modifications, allowing the program to close without prompting
     * confirmation if no changes were detected.
     * @param part Part
     * @return boolean
     */
    public boolean change_detect(Part part){
        if(!idTextfield.getText().equals(Integer.toString(part.getID()))){
            return true;
        }
        else if (!nameTextField.getText().equals(part.getName())){
            return true;
        }
        else if (!invTextField.getText().equals(Integer.toString(part.getStock()))){
            return true;
        }
        else if (!priceTextField.getText().equals(Double.toString(part.getPrice()))){
            return true;
        }
        else if (!minTextField.getText().equals(Integer.toString(part.getMin()))){
            return true;
        }
        else if (!maxTextField.getText().equals(Integer.toString(part.getMax()))){
            return true;
        }
        //Checking criteria for each Part subclass
        else if (part.getClass().getName() == "Outsourced"){
            Outsourced newOutsourced = (Outsourced) part;
            if (!companyNameTextField.getText().equals(newOutsourced.getCompanyName())){
                return true;
            }
            else if (inHouseRadioButton.isSelected()){
                return true;
            }
        else if (part.getClass().getName() == "sample.InHouse"){
            InHouse newInHouse = (InHouse) part;
            if (!machineidTextField.getText().equals(Integer.toString(newInHouse.getMachineID()))){
                return true;
            }
            else if (outsourcedRadioButton.isSelected()){
                return true;
            }
        }
        }
        return false;
    }
}
