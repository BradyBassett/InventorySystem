package c482performanceassessment.controllers;

import c482performanceassessment.model.Inventory;
import c482performanceassessment.model.Part;
import c482performanceassessment.model.Product;
import c482performanceassessment.model.SearchParts;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * This class is responsible for reducing redundancy between the three main controllers by abstracting commonly used
 * functions.
 * FUTURE ENHANCEMENT - Make certain functions such as deleting parts or products more generic across controllers.
 * to reduce the verbosity of controllers
 * @author Brady Bassett
 */
public abstract class BaseFormController {
    protected Stage stage;
    protected Scene scene;
    protected Parent root;

    /**
     * This will switch the current scene to the main form.
     * @param event ActionEvent that provides the main stage declared in main
     * @throws IOException if the main-form.fxml file is unreachable
     */
    @FXML
    protected void switchToMainScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/c482performanceassessment/main-form.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Search bar functionality for all Part tables.
     * LOGIC ERROR - when partSearch was called, I would only call searchParts from the inventory class, but when
     * associatedParts were being searched, I would return the parts list from the inventory class. I fixed this by
     * creating a static class SearchParts which only includes the searchParts function, and returned the appropriate
     * list based on if it was a parts search or an associatedParts search.
     * @param partSearch String user is searching for
     * @return returns a list of all parts matching the provided string if there is at least one part in partsRetrieved,
     * or returns the entire parts list if no characters are present in partSearch
     */
    protected ObservableList<Part> partSearch(TextField partSearch) {
        ObservableList<Part> partsRetrieved = SearchParts.searchParts(partSearch.getText(), Inventory.getPartsInventory());

        if (partsRetrieved.size() > 0) {
            return partsRetrieved;
        }
        return Inventory.getPartsInventory();
    }

    /**
     * Search bar functionality for all Product tables.
     * @param productSearch String user is searching for
     * @return returns a list of all products matching the provided string if there is at least one product in productsRetrieved,
     * or returns the entire products list if no characters are present in productSearch
     */
    protected ObservableList<Product> productSearch(TextField productSearch) {
        ObservableList<Product> productsRetrieved = Inventory.searchProducts(productSearch.getText());

        if (productsRetrieved.size() > 0) {
            return productsRetrieved;
        }
        return Inventory.getProductsInventory();
    }

    /**
     * Initializes the parts tables using the provided displayedParts list and its displayed values.
     * @param partTable TableView of parts that will be displayed
     * @param partId TableColumn for partId
     * @param partName TableColumn for partName
     * @param partInventoryLevel TableColumn for partInventoryLevel
     * @param partPrice TableColumn for partPrice
     * @param displayedParts Part observable list of all parts that are to be displayed
     */
    protected void partTableInit(TableView<Part> partTable, TableColumn<Part, Integer> partId, TableColumn<Part, String> partName,
                                 TableColumn<Part, Integer> partInventoryLevel, TableColumn<Part, Double> partPrice,
                                 ObservableList<Part> displayedParts) {
        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        partTable.setItems(displayedParts);
    }

    /**
     * Initializes the products tables using the provided displayedProducts list and its displayed values.
     * @param productTable TableView of products that will be displayed
     * @param productId TableColumn for productId
     * @param productName TableColumn for productName
     * @param productInventoryLevel TableColumn for productInventoryLevel
     * @param productPrice TableColumn for productPrice
     * @param displayedProducts Product observable list of all products that are to be displayed
     */
    protected void productTableInit(TableView<Product> productTable, TableColumn<Product, Integer> productId,
                                    TableColumn<Product, String> productName, TableColumn<Product, Integer> productInventoryLevel,
                                    TableColumn<Product, Double> productPrice, ObservableList<Product> displayedProducts) {
        productId.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        productTable.setItems(displayedProducts);
    }

    /**
     * Function to validate if it is possible to save an added or modified item. If any of the provided TextFields are empty
     * throw a NullPointerException with an error message describing which TextField is empty. And if the min inventory
     * value is less than or equal to the max inventory value, or if the inventory value is less than the min inventory
     * value or greater than the max inventory value, throw an IllegalArgumentException with a message depending on the case.
     * @param name TextField for item name
     * @param inventory TextField for item inventory
     * @param price TextField for item price
     * @param maxInventory TextField for max item inventory
     * @param minInventory TextField for min item inventory
     */
    protected void saveValidation(TextField name, TextField inventory, TextField price, TextField maxInventory,
                                  TextField minInventory) {
        if (name.getText().isEmpty()) {
            throw new NullPointerException("Name field is empty");
        }
        if (inventory.getText().isEmpty()) {
            throw new NullPointerException("Inventory field is empty");
        }
        if (price.getText().isEmpty()) {
            throw new NullPointerException("Price field is empty");
        }
        if (maxInventory.getText().isEmpty()) {
            throw new NullPointerException("Max inventory field is empty");
        }
        if (minInventory.getText().isEmpty()) {
            throw new NullPointerException("Min inventory field is empty");
        }
        if (Integer.parseInt(minInventory.getText()) >= Integer.parseInt(maxInventory.getText())) {
            throw new IllegalArgumentException("Min inventory must be less than max inventory");
        }
        if (Integer.parseInt(inventory.getText()) > Integer.parseInt(maxInventory.getText()) ||
                Integer.parseInt(inventory.getText()) < Integer.parseInt(minInventory.getText())) {
            throw new IllegalArgumentException("Inventory amount must be in between min and max inventory");
        }
    }
}