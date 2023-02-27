import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controls add_part interface
 * Provides self-contained operations that allow data to be pipelined to the InventoryManagement controller.
 */
public class add_partController {

    //TextFields
    @FXML
    private TextField machineidTextfield;
    @FXML
    private TextField maxTextfield;
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
    @FXML
    private TextField companyNameTextField;

    //RadioButtons
    @FXML
    private RadioButton inHouseRadioButton;
    @FXML
    private RadioButton outsourcedRadioButton;

    //Save/Cancel Buttons
    @FXML
    private Button addPartCancelButton;

    //Labels
    @FXML
    private Label companyNameLabel;
    @FXML
    private Label machineIdLabel;

    //Part object being generated
    static Part newPart = null;

    /**
     * Default values are set for radioButtons and textFields here.
     */
    public void initialize(){
        //Setting outsourced_RadioButton as default
        outsourcedRadioButton.setSelected(true);
        radio_button_listener();

        //Making the id text field uneditable
        idTextfield.setEditable(false);
        idTextfield.setStyle("-fx-control-inner-background: #D3D3D3");
    }

    //FXMLLoader contained within object class, allows data to be returned to its caller

    /**
     * Takes int arg
     * Initializes Part objects ID member to arg
     * Here the interface is launched and managed.
     * Creates and instantiates add_partController object.
     * Here a newly created Part object is pipelined to the InventoryManagement controller.
     * Returns null if the user cancels or quits without applying any changes.
     * @param id int
     * @return Part
     */
    public Part BeginStart(int id){
        try {
            //creating an fxmlloader and instantiating add_partController object
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add_part.fxml"));
            Parent root = loader.load();

            add_partController controller = loader.getController();
            //Setting id_textField to id.
            controller.set_id_field(id);
            Stage stage = new Stage();
            stage.setTitle("Add Part");
            stage.setScene(new Scene(root));
            //overloading default close operations
            stage.setOnCloseRequest(e->{
                e.consume();
                //Transferring closing protocols to Close_program()
                Close_program(stage);
            });
            stage.showAndWait();
        } catch (Exception e) {
            System.out.println("Failed to load Window2.fxml");
        }

        return this.newPart;
    }

    /**
     * Takes int as arg
     * Sets id_TextField to arg
     * This allows id_TextField to remain private and still allow information to be passed from BeginStart().
     * @param id int
     */
    public void set_id_field(Integer id){
        idTextfield.setText(Integer.toString(id));
    }

    /**
     * Handles closing of the interface when the user clicks the cancel button
     */
    public void Cancel_ButtonListener(){
        Stage stage = (Stage) addPartCancelButton.getScene().getWindow();
        newPart = null;
        stage.close();
    }

    /**
     * called whenever a radio button is clicked to hide and reveal the appropriate fields
     */
    public void radio_button_listener() {
        //Hides companyName_textField and its corresponding label
        if (inHouseRadioButton.isSelected()) {
            enable_prompt(true, machineidTextfield, machineIdLabel);
            enable_prompt(false, companyNameTextField, companyNameLabel);

        }
        //Hides MachineID_textField and corresponding label
        else if (outsourcedRadioButton.isSelected()) {
            enable_prompt(false, machineidTextfield, machineIdLabel);
            enable_prompt(true, companyNameTextField, companyNameLabel);
        }
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
     * Manages creation of Part object based on user input.
     * Handles input that is inconsistent by providing an error dialogue box, as well as highlighting the TextField that
     * is inconsistent.
     * Returns true if user input is valid and consistent
     * Returns false if user input is either invalid or inconsistent
     * save_button_listener() is also called throughout the controller as a stand-alone function
     * @return boolean
     */
    public boolean save_button_listener(){
        try {

            //validating user input
            Integer maxInv = Integer.parseInt(maxTextfield.getText());
            Integer minInv = Integer.parseInt(minTextField.getText());
            Integer currentInv = Integer.parseInt(invTextField.getText());

            //Resetting inventory text borders
            invTextField.setStyle("");
            maxTextfield.setStyle("");
            minTextField.setStyle("");

            if (minInv > maxInv) {
                minTextField.setStyle("-fx-text-box-border: red");
                ErrorDialogueBox diagBox = new ErrorDialogueBox();
                diagBox.BeginStart("Error Maximum Inventory cannot be less than Minimum Inventory");
                newPart = null;
                return(false);
            }
            else if (currentInv < minInv) {
                invTextField.setStyle("-fx-text-box-border: red");
                ErrorDialogueBox diagBox = new ErrorDialogueBox();
                diagBox.BeginStart("Error Inventory cannot be less than Minimum Inventory");
                newPart = null;
                return(false);
            }
            else if (currentInv > maxInv) {
                maxTextfield.setStyle("-fx-text-box-border: red");
                ErrorDialogueBox diagBox = new ErrorDialogueBox();
                diagBox.BeginStart("Error Inventory cannot be greater than Maximum Inventory");
                newPart = null;
                return(false);
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

            //UserInput separated into vars
            final Integer id = Integer.parseInt(idTextfield.getText());
            final String name = nameTextField.getText();
            final Integer inv = Integer.parseInt(invTextField.getText());
            final Double price = Double.parseDouble(priceTextField.getText());
            final Integer max = Integer.parseInt(maxTextfield.getText());
            final Integer min = Integer.parseInt(minTextField.getText());

            if (inHouseRadioButton.isSelected()) {
                //Specific to InHouse Components
                final Integer machineID = Integer.parseInt(machineidTextfield.getText());
                this.newPart = new InHouse(id, name, price, inv, min, max, machineID);
            } else if (outsourcedRadioButton.isSelected()) {
                //Specific to Outsourced Components
                final String companyName = companyNameTextField.getText();
                newPart = new Outsourced(id, name, price, inv, min, max, companyName);
            }
            Stage stage = (Stage) addPartCancelButton.getScene().getWindow();
            stage.close();
        }
        //Exception is generated when the data inside a text field cannot be parsed into an Integer or Double
        catch(Exception inputException){
            System.out.println("Error, invalid input");
            ErrorDialogueBox diagBox = new ErrorDialogueBox();
            diagBox.BeginStart("ERROR! please enter the correct data in the appropriate fields!");
            return false;
        }
        return true;
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
        }
        else if (tf == "dont apply"){
            newPart = null;
            stage.close();
        }
    }

}

