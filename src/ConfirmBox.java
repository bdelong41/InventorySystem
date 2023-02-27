import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;

    //By default tf is false
    private static boolean truefalse = false;

    public boolean BeginStart(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("confirm_box.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            ConfirmBox confirmBox = loader.getController();
            stage.setTitle("Confirm Action");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }catch(Exception e){
            System.out.println("Failed to load ConfirmBox.fxml");
        }
        System.out.println("TrueFalse: "+ truefalse);
        return truefalse;
    }

    public void confirm_button_listener(){
        truefalse = true;

        ((Stage) confirmButton.getScene().getWindow()).close();
    }
    public void cancel_button_listener(){
        truefalse = false;

        ((Stage) cancelButton.getScene().getWindow()).close();
    }
}
