import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ErrorDialogueBox {
    @FXML
    private Label errorMessageLabel;
    @FXML
    private Button okButton;

    public void BeginStart(String errorMessage){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("error_dialogue_box.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);

            //Creating ErrorDialogueBox instance
            ErrorDialogueBox errorDialogue = loader.getController();
            errorDialogue.set_error_message(errorMessage);
            stage.setTitle("Error");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        }catch(Exception e){
            System.out.println("Failed to load ErrorDialogueBox.fxml");
        }
    }

    public void Ok_button_listener(){
        ((Stage) okButton.getScene().getWindow()).close();
    }

    public void set_error_message(String errorMessage){
       errorMessageLabel.setText(errorMessage);
       errorMessageLabel.setWrapText(true);
    }
}
