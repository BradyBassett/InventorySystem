package c482performanceassessment.controllers;

import c482performanceassessment.model.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is responsible for handling all functionality for the main-form scene.
 * FUTURE ENHANCEMENT - Could add the ability to resize the window and have the scenes be responsive to the size change.
 * @author Brady Bassett
 */
public class MainFormController extends BaseFormController implements Initializable {
    private Stage stage;
    private Parent root;

    @FXML
    private TableView<Part> partTable;
    @FXML
    private TableColumn<Part, Integer> partId;
    @FXML
    private TableColumn<Part, String> partName;
    @FXML
    private TableColumn<Part, Integer> partInventoryLevel;
    @FXML
    private TableColumn<Part, Double> partPrice;
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> productId;
    @FXML
    private TableColumn<Product, String> productName;
    @FXML
    private TableColumn<Product, Integer> productInventoryLevel;
    @FXML
    private TableColumn<Product, Double> productPrice;
    @FXML
    private TextField partSearch;
    @FXML
    private TextField productSearch;

    private ObservableList<Part> displayedParts = Inventory.getPartsInventory();
    private ObservableList<Product> displayedProducts = Inventory.getProductsInventory();

    /**
     * Initializes the partTable and the productTable.
     * @param url url to the main-form.fxml file
     * @param resourceBundle contains locale-specific objects
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partTableInit(partTable, partId, partName, partInventoryLevel, partPrice, displayedParts);
        productTableInit(productTable, productId, productName, productInventoryLevel, productPrice, displayedProducts);
    }

    /**
     * Handles closing out of the application by clicking the exit button.
     * @param event ActionEvent that provides the main stage declared in main
     */
    @FXML
    private void onExitButtonClick(ActionEvent event) {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to close the application?");
        Optional<ButtonType> exitConfirmation = alert.showAndWait();
        if (exitConfirmation.isPresent() && exitConfirmation.get() == ButtonType.OK) {
            stage.close();
        }
    }

    /**
     * Switches the scene to the part-form scene and changes part-form title to "Add".
     * RUNTIME ERROR - Was unable to load in the part-form.fxml file. Fixed this by recreating the part-form.fxml file
     * ensuring that it was using the correct form of javafx.
     * @param event ActionEvent that provides the main stage declared in main
     * @throws IOException if the part-form.fxml file is not found
     */
    @FXML
    private void onPartAdd(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/c482performanceassessment/part-form.fxml"));
        root = loader.load();
        PartFormController partFormController = loader.getController();
        partFormController.setTitle("Add");
        setStage(root, event);
    }

    /**
     * Switches the scene to the part-form scene, changes part-form title to "Modify", and loads in the selected part data.
     * @param event ActionEvent that provides the main stage declared in main
     * @throws IOException if the part-form.fxml file is not found
     */
    @FXML
    private void onPartModify(ActionEvent event) throws IOException {
        if (partTable.getSelectionModel().getSelectedItem() != null) {
            Part part = partTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/c482performanceassessment/part-form.fxml"));
            root = loader.load();
            PartFormController partFormController = loader.getController();
            partFormController.setTitle("Modify");
            partFormController.loadPartData(part);
            setStage(root, event);
        }
    }

    /**
     * If a part is capable of being removed, removes the part from the parts list.
     */
    @FXML
    private void onPartDelete() {
        Part part = partTable.getSelectionModel().getSelectedItem();
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?");
        Alert errorAlert = new Alert(Alert.AlertType.ERROR, "This part is an associated part within one or many products, " +
                "please either remove the part association or remove the product the part is associated with");
        if (part != null) {
            if (Inventory.validatePartRemoval(part)) {
                errorAlert.showAndWait();
            } else {
                Optional<ButtonType> deleteConfirmation = confirmationAlert.showAndWait();
                if (deleteConfirmation.isPresent() && deleteConfirmation.get() == ButtonType.OK) {
                    Inventory.removePartsInventory(part);
                }
            }
        }
    }

    /**
     * Takes string from partSearch and displays all matching parts in the partsTable.
     * @throws MalformedURLException if main-form.fxml is not found
     */
    @FXML
    private void onPartSearch() throws MalformedURLException {
        displayedParts = partSearch(partSearch);
        URL url = new URL("file:/c482performanceassessment/main-form.fxml");
        initialize(url, null);
    }

    /**
     * Switches the scene to the product-form scene and changes product-form title to "Add".
     * @param event ActionEvent that provides the main stage declared in main
     * @throws IOException if the product-form.fxml file is not found
     */
    @FXML
    private void onProductAdd(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/c482performanceassessment/product-form.fxml"));
        root = loader.load();
        ProductFormController productFormController = loader.getController();
        productFormController.setTitle("Add");
        setStage(root, event);
    }

    /**
     * Switches the scene to the product-form scene, changes product-form title to "Modify", and loads in the selected product data.
     * @param event ActionEvent that provides the main stage declared in main
     * @throws IOException if the product-form.fxml file is not found
     */
    @FXML
    private void onProductModify(ActionEvent event) throws IOException {
        if (productTable.getSelectionModel().getSelectedItem() != null) {
            Product product = productTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/c482performanceassessment/product-form.fxml"));
            root = loader.load();
            ProductFormController productFormController = loader.getController();
            productFormController.setTitle("Modify");
            productFormController.setCurrentProduct(product);
            productFormController.loadProductData(product);
            setStage(root, event);
        }
    }

    /**
     * If a product is capable of being removed, removes the product from the products list.
     */
    @FXML
    private void onProductDelete() {
        Product product = productTable.getSelectionModel().getSelectedItem();
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this product?");
        Alert errorAlert = new Alert(Alert.AlertType.ERROR, "This product still contains one or more part associations, " +
                "please first remove all associated parts and try again.");
        if (product != null) {
            if (product.getAssociatedParts().size() == 0) {
                Optional<ButtonType> deleteConfirmation = confirmationAlert.showAndWait();
                if (deleteConfirmation.isPresent() && deleteConfirmation.get() == ButtonType.OK) {
                    Inventory.removeProductsInventory(product);
                }
            } else {
                errorAlert.showAndWait();
            }
        }
    }

    /**
     * Takes string from productSearch and displays all matching products in the productsTable.
     * @throws MalformedURLException if main-form.fxml is not found
     */
    @FXML
    private void onProductSearch() throws MalformedURLException {
        displayedProducts = productSearch(productSearch);
        URL url = new URL("file:/c482performanceassessment/main-form.fxml");
        initialize(url, null);
    }

    /**
     * Sets stage with the main-form.fxml file.
     * @param root Parent pointing towards the main-form.fxml file
     * @param event ActionEvent that provides the main stage declared in main
     */
    private void setStage(Parent root, ActionEvent event) {
        Scene scene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
