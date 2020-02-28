
package inventory.controller;

import inventory.model.Part;
import inventory.model.Product;
import inventory.service.InventoryService;
import inventory.service.ServiceException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;


public class MainScreenController implements Initializable,Controller {
    //Sonar
    private String errHeader = "Error!";

     // Declare fields
    private static int modifyPartIndex;
    private static int modifyProductIndex;
    private Logger logger = Logger.getLogger(MainScreenController.class.getName());
    // Declare methods
    public static int getModifyPartIndex() {
        return modifyPartIndex;
    }
    public static void setModifyPartIndex(int index) {
        modifyPartIndex = index;
    }

    public static int getModifyProductIndex() {
        return modifyProductIndex;
    }
    public static void setModifyProductIndex(int index) {
        modifyProductIndex = index;
    }

    private InventoryService service;

    @FXML
    private TableView<Part> partsTableView;

    @FXML
    private TableColumn<Part, Integer> partsIdCol;

    @FXML
    private TableColumn<Part, String> partsNameCol;

    @FXML
    private TableColumn<Part, Integer> partsInventoryCol;

    @FXML
    private TableColumn<Part, Double> partsPriceCol;


    @FXML
    private TableView<Product> productsTableView;

    @FXML
    private TableColumn<Product, Integer> productsIdCol;

    @FXML
    private TableColumn<Product, String> productsNameCol;

    @FXML
    private TableColumn<Product, Integer> productsInventoryCol;

    @FXML
    private TableColumn<Product, Double> productsPriceCol;
    
    @FXML
    private TextField partsSearchTxt;
    
    @FXML
    private TextField productsSearchTxt;

    public void setService(InventoryService service){
        this.service=service;
        partsTableView.setItems(service.getAllParts());
        productsTableView.setItems(service.getAllProducts());
    }


    /**
     * Initializes the controller class and populate table views.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Populate parts table view
        partsIdCol.setCellValueFactory(new PropertyValueFactory<>("partId"));
        partsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        partsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Populate products table view
        productsIdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        productsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Method to add to button handler to switch to scene passed as source
     * @param event
     * @param source
     * @throws IOException
     */
    private void displayScene(ActionEvent event, String source) throws IOException {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        FXMLLoader loader= new FXMLLoader(getClass().getResource(source));
        Parent scene = loader.load();
        Controller ctrl=loader.getController();
        ctrl.setService(service);
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Ask user for confirmation before deleting selected part from list of parts.
     * @param event 
     */
    @FXML
    void handleDeletePart(ActionEvent event) {
        Part part = partsTableView.getSelectionModel().getSelectedItem();
        if(part == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!!!");
            alert.setHeaderText(errHeader);
            alert.setContentText("A part must be selected from the table to be deleted!");
            alert.showAndWait();
            return;
        }
        try {

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Confirm Part Deletion?");
            alert.setContentText("Are you sure you want to delete part " + part.getName() + " from parts?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                logger.info("Part deleted.");
                service.deletePart(part);
            } else {
                logger.info("Canceled part deletion.");
            }
        } catch (ServiceException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Searching Part!!");
            alert.setHeaderText(errHeader);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Ask user for confirmation before deleting selected product from list of products.
     * @param event 
     */
    @FXML
    void handleDeleteProduct(ActionEvent event) {
        Product product = productsTableView.getSelectionModel().getSelectedItem();
        if(product == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!!!");
            alert.setHeaderText(errHeader);
            alert.setContentText("A product must be selected from the table to be deleted!");
            alert.showAndWait();
            return;
        }
        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Confirm Product Deletion?");
            alert.setContentText("Are you sure you want to delete product " + product.getName() + " from products?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                service.deleteProduct(product);
                String msg = String.format("Product %s was removed.", product.getName());
                logger.info(msg);
            } else {
                String msg = String.format("Product %s was removed.", product.getName());
                logger.info(msg);
            }
        } catch (ServiceException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Searching Part!");
            alert.setHeaderText(errHeader);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Switch scene to Add Part
     * @param event
     * @throws IOException
     */
    @FXML
    void handleAddPart(ActionEvent event) throws IOException {
        displayScene(event, "/fxml/AddPart.fxml");
    }

    /**
     * Switch scene to Add Product
     * @param event
     * @throws IOException
     */
    @FXML
    void handleAddProduct(ActionEvent event) throws IOException {
        displayScene(event, "/fxml/AddProduct.fxml");
    }

    /**
     * Changes scene to Modify Part screen and passes values of selected part
     * and its index
     * @param event
     * @throws IOException
     */
    @FXML
    void handleModifyPart(ActionEvent event) throws IOException {
        Part modifyPart = partsTableView.getSelectionModel().getSelectedItem();
        if(modifyPart == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!!");
            alert.setHeaderText(errHeader);
            alert.setContentText("A part must be selected from the table to be updated!");
            alert.showAndWait();
            return;
        }
        MainScreenController.setModifyPartIndex(service.getAllParts().indexOf(modifyPart));

        displayScene(event, "/fxml/ModifyPart.fxml");
    }

    /**
     * Switch scene to Modify Product
     * @param event
     * @throws IOException
     */
    @FXML
    void handleModifyProduct(ActionEvent event) throws IOException {
        Product modifyProduct = productsTableView.getSelectionModel().getSelectedItem();
        if(modifyProduct == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!!");
            alert.setHeaderText("Error!");
            alert.setContentText("A product must be selected from the table to be updated!");
            alert.showAndWait();
            return;
        }
        MainScreenController.setModifyProductIndex(service.getAllProducts().indexOf(modifyProduct));

        displayScene(event, "/fxml/ModifyProduct.fxml");
    }

    /**
     * Ask user for confirmation before exiting
     * @param event 
     */
    @FXML
    void handleExit(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation Needed");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            logger.info("Ok selected. Program exited");
            System.exit(0);
        } else {
            logger.info("Cancel clicked.");
        }
    }

    /**
     * Gets search text and inputs into lookupPart method to highlight desired part
     * @param event 
     */
    @FXML
    void handlePartsSearchBtn(ActionEvent event) {
        try{
            String x = partsSearchTxt.getText();
            partsTableView.getSelectionModel().select(service.lookupPart(x));
        } catch (ServiceException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Searching Part!");
            alert.setHeaderText(errHeader);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Gets search text and inputs into lookupProduct method to highlight desired product
     * @param event 
     */
    @FXML
    void handleProductsSearchBtn(ActionEvent event) {
        try {
            String x = productsSearchTxt.getText();
            productsTableView.getSelectionModel().select(service.lookupProduct(x));
        } catch (ServiceException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Searching Product!");
            alert.setHeaderText(errHeader);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


}
