import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CloseProgram {
    @FXML
    private Button closeCancelButton;

    private static String tf;
    public String BeginStart(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("close_program.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Save dialogue");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        }catch(Exception e){
            System.out.println("Failed to load close_program.fxml");
        }
        return tf;
    }

    public void apply_button_listener(){
        tf = "apply";
        Stage stage = (Stage) closeCancelButton.getScene().getWindow();
        stage.close();
    }

    public void cancel_button_listener(){
        tf = "cancel";
        Stage stage = (Stage) closeCancelButton.getScene().getWindow();
        stage.close();
    }
    public void dontApply_button_listener(){
        tf = "dont apply";
        Stage stage = (Stage) closeCancelButton.getScene().getWindow();
        stage.close();
    }
}
