import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{

        //FXMLLoader loader = new FXMLLoader(getClass().getResource("InventoryManagement.fxml"));

        //InventoryManagementController
        InventoryManagement inventoryController = new InventoryManagement();
        inventoryController.BeginStart();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
