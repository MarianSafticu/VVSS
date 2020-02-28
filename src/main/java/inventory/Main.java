package inventory;

import inventory.controller.AddPartController;
import inventory.repository.InventoryRepository;
import inventory.repository.RepoException;
import inventory.service.InventoryService;
import inventory.controller.MainScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.logging.Logger;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {
            Logger logger = Logger.getLogger(AddPartController.class.getName());
            InventoryRepository repo = new InventoryRepository();
            InventoryService service = new InventoryService(repo);
            String products = service.getAllProducts().toString();
            logger.info(products);
            String parts = service.getAllParts().toString();
            logger.info(parts);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainScreen.fxml"));

            Parent root = loader.load();
            MainScreenController ctrl = loader.getController();
            ctrl.setService(service);

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        }catch (RepoException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Data file error!");
            alert.setHeaderText("Error!");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
