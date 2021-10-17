package c482performanceassessment.controllers;

import c482performanceassessment.model.Inventory;
import c482performanceassessment.model.Part;
import c482performanceassessment.model.Product;
import c482performanceassessment.model.SearchParts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is responsible for controlling all functionality relating to the product form.
 * FUTURE ENHANCEMENT - Could add a button that adds a new part from the Add/Modify product scene.
 * @author Brady Bassett
 */
public class ProductFormController extends BaseFormController implements Initializable {
    @FXML
    private Label productFormTitle;
    @FXML
    private TextField productId;
    @FXML
    private TextField productName;
    @FXML
    private TextField productInventory;
    @FXML
    private TextField productPrice;
    @FXML
    private TextField productMaxInventory;
    @FXML
    private TextField productMinInventory;
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
    private TableView<Part> associatedPartTable;
    @FXML
    private TableColumn<Part, Integer> associatedPartId;
    @FXML
    private TableColumn<Part, String> associatedPartName;
    @FXML
    private TableColumn<Part, Integer> associatedPartInventoryLevel;
    @FXML
    private TableColumn<Part, Double> associatedPartPrice;
    @FXML
    private TextField partSearch;
    @FXML
    private TextField associatedPartSearch;

    private Product currentProduct = new Product();

    private ObservableList<Part> displayedParts = Inventory.getPartsInventory();
    private ObservableList<Part> displayedAssociatedParts = currentProduct.getAssociatedParts();
    private final ObservableList<Part> temporaryAddedParts = FXCollections.observableArrayList();
    private final ObservableList<Part> temporaryRemovedParts = FXCollections.observableArrayList();

    /**
     * Sets productFormTitle text to either "Modify Product" or "Add Product".
     * @param title String containing "Modify" or "Add"
     */
    protected void setTitle(String title) {
        productFormTitle.setText(title + " Product");
    }

    /**
     * Sets the product currently being worked with
     * @param product product to be assigned to workingProduct
     */
    protected void setCurrentProduct(Product product) {
        this.currentProduct = product;
    }

    /**
     * Initializes the partTable and the associatedPartTable
     * @param url url to the product-form.fxml file
     * @param resourceBundle contains locale-specific objects
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partTableInit(partTable, partId, partName, partInventoryLevel, partPrice, displayedParts);

        if (currentProduct != null) {
            partTableInit(associatedPartTable, associatedPartId, associatedPartName, associatedPartInventoryLevel,
                    associatedPartPrice, displayedAssociatedParts);
        }
    }

    /**
     * Fills the TextBoxes with the appropriate data from product being modified.
     * LOGIC ERROR - AssociatedParts list was not being displayed to the table on call of loadProductData because, the
     * initialize function was being called before loadProductData was called. This was fixed but calling initialize again
     * from inside the loadProductData function to initialize with the provided products associatedParts list.
     * @param product product data being loaded
     * @throws MalformedURLException if "file:/c482performanceassessment/product-form.fxml" is not found
     */
    @FXML
    protected void loadProductData(Product product) throws MalformedURLException {
        productId.setText(String.valueOf(product.getId()));
        productName.setText(product.getName());
        productInventory.setText(String.valueOf(product.getStock()));
        productPrice.setText(String.valueOf(product.getPrice()));
        productMaxInventory.setText(String.valueOf(product.getMax()));
        productMinInventory.setText(String.valueOf(product.getMin()));
        displayedAssociatedParts = product.getAssociatedParts();
        URL url = new URL("file:/c482performanceassessment/product-form.fxml");
        initialize(url, null);
    }

    /**
     * Responsible for displaying all resulting parts from a search.
     * @throws MalformedURLException if "file:/c482performanceassessment/product-form.fxml" is not found
     */
    @FXML
    private void onPartSearch() throws MalformedURLException {
        displayedParts = partSearch(partSearch);
        URL url = new URL("file:/c482performanceassessment/product-form.fxml");
        initialize(url, null);
    }

    /**
     * This function sets the currentProduct values and either modifies an existing product or adds a new one to the
     * products list.
     * @param event ActionEvent that provides the main stage declared in main
     * @throws IOException if the main-form.fxml file is unreachable
     */
    @FXML
    private void onProductSave(ActionEvent event) throws IOException {
        try {
            saveValidation(productName, productInventory, productPrice, productMaxInventory, productMinInventory);
            currentProduct.setName(productName.getText());
            currentProduct.setStock(Integer.parseInt(productInventory.getText()));
            currentProduct.setPrice(Double.parseDouble(productPrice.getText()));
            currentProduct.setMax(Integer.parseInt(productMaxInventory.getText()));
            currentProduct.setMin(Integer.parseInt(productMinInventory.getText()));

            if (currentProduct.getId() == 0) {
                currentProduct.setId(Inventory.getNextProductId());
                Inventory.addProductsInventory(currentProduct);
            } else {
                Inventory.modifyProductsInventory(Inventory.getProductsInventory().indexOf(currentProduct), currentProduct);
            }
            switchToMainScene(event);
        } catch (NullPointerException | IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.showAndWait();
        }
    }

    /**
     * Removes the values in temporaryAddedParts from the currentProducts associatedParts list and adds the temporaryRemovedParts
     * back to the currentProducts associatedParts list, and switches back to the main scene.
     * @param event ActionEvent that provides the main stage declared in main
     * @throws IOException if the main-form.fxml file is unreachable
     */
    @FXML
    private void onProductCancel(ActionEvent event) throws IOException {
        switchToMainScene(event);
        for (Part part : temporaryAddedParts) {
            currentProduct.deleteAssociatedPart(part);
        }
        for (Part part : temporaryRemovedParts) {
            currentProduct.addAssociatedPart(part);
        }
    }

    /**
     * Adds an associated part to the currentProducts associatedParts list.
     */
    @FXML
    private void addAssociatedPart() {
        try {
            if (partTable.getSelectionModel().getSelectedItem() != null) {
                if (currentProduct.includes(partTable.getSelectionModel().getSelectedItem())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Part already associated with current product");
                    alert.showAndWait();
                } else {
                    currentProduct.addAssociatedPart(partTable.getSelectionModel().getSelectedItem());
                    temporaryAddedParts.add(partTable.getSelectionModel().getSelectedItem());
                }
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.toString());
            alert.showAndWait();
        }
    }

    /**
     * Removes a selected associated part from the currentProducts associatedParts list.
     */
    @FXML
    private void removeAssociatedPart() {
        if (associatedPartTable.getSelectionModel().getSelectedItem() != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this part?");
            Optional<ButtonType> deleteConfirmation = confirmationAlert.showAndWait();
            if (deleteConfirmation.isPresent() && deleteConfirmation.get() == ButtonType.OK) {
                temporaryRemovedParts.add(associatedPartTable.getSelectionModel().getSelectedItem());
                currentProduct.deleteAssociatedPart(associatedPartTable.getSelectionModel().getSelectedItem());
            }
        }
    }

    /**
     * Responsible for displaying all resulting associatedParts from a search.
     * @throws MalformedURLException if "file:/c482performanceassessment/product-form.fxml" is not found
     */
    @FXML
    private void onAssociatedPartSearch() throws MalformedURLException {
        ObservableList<Part> partsRetrieved = SearchParts.searchParts(associatedPartSearch.getText(), currentProduct.getAssociatedParts());
        if (partsRetrieved.size() > 0) {
            displayedAssociatedParts = partsRetrieved;
        } else {
            displayedAssociatedParts = currentProduct.getAssociatedParts();
        }
        URL url = new URL("file:/c482performanceassessment/product-form.fxml");
        initialize(url, null);
    }
}
