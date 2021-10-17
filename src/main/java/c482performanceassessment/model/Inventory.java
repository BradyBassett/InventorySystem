package c482performanceassessment.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is responsible for holding an observable list for all parts and an observable list for all products that
 * are currently present in the inventory.
 * FUTURE ENHANCEMENT - Implement a more complex id assignment methodology to ensure ids are unique.
 * @author Brady Bassett
 */
public class Inventory {
    private static final ObservableList<Part> parts = FXCollections.observableArrayList();
    private static final ObservableList<Product> products = FXCollections.observableArrayList();
    private static int partIdCounter = 0;
    private static int productIdCounter = 0;

    /**
     * Returns the observable list containing all parts in inventory.
     * @return parts in inventory
     */
    public static ObservableList<Part> getPartsInventory() {
        return parts;
    }

    /**
     * Adds the provided part to the parts observable list.
     * @param part part to be added to the list
     */
    public static void addPartsInventory(Part part) {
        if (parts.contains(part)) {
            throw new IllegalArgumentException("Part is already present in the inventory");
        }
        parts.add(part);
    }

    /**
     * Sets the part in the parts list at the provided index to the provided part.
     * @param index index of the part being modified
     * @param part new part replacing the previous
     */
    public static void modifyPartsInventory(int index, Part part) {
        parts.set(index, part);
    }

    /**
     * Removes the provided part from the parts list.
     * @param part part to be removed
     */
    public static void removePartsInventory(Part part) {
        parts.remove(part);
    }

    /**
     * Checks if the part provided is present in the any of the products associated parts.
     * @param part part to be validated
     * @return returns true if the part is present in an associated parts list and false otherwise
     */
    public static boolean validatePartRemoval(Part part) {
        for (Product product : products) {
            if (product.includes(part)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns an incremented partIdCounter.
     * @return incremented partIdCounter
     */
    public static int getNextPartId() {
        partIdCounter++;
        return partIdCounter;
    }

    /**
     * Returns the observable list containing all products in inventory.
     * @return products in inventory
     */
    public static ObservableList<Product> getProductsInventory() {
        return products;
    }

    /**
     * Adds the provided product to the products observable list.
     * @param product product to be added to the list
     */
    public static void addProductsInventory(Product product) {
        products.add(product);
    }

    /**
     * Sets the product in the products list at the provided index to the provided product.
     * @param index index of the product being modified
     * @param product new product replacing the previous
     */
    public static void modifyProductsInventory(int index, Product product) {
        products.set(index, product);
    }

    /**
     * Removes the provided product from the products list.
     * @param product product to be removed
     */
    public static void removeProductsInventory(Product product) {
        products.remove(product);
    }

    /**
     * Returns an incremented productIdCounter.
     * @return incremented productIdCounter
     */
    public static int getNextProductId() {
        productIdCounter++;
        return productIdCounter;
    }

    /**
     * Takes a string to compare to either the products id or products name and returns a list of all matching products.
     * @param searchField string to match
     * @return observable list of all matching products
     */
    public static ObservableList<Product> searchProducts(String searchField) {
        ObservableList<Product> matchingProducts = FXCollections.observableArrayList();
        if (searchField.length() > 0) {
            Pattern pattern = Pattern.compile("[^0-9]");
            Matcher matcher = pattern.matcher(searchField);
            if (matcher.find()) {
                for (Product product : products) {
                    if (product.getName().toLowerCase(Locale.ROOT).contains(searchField.toLowerCase(Locale.ROOT))) {
                        matchingProducts.add(product);
                    }
                }
            } else {
                for (Product product : products) {
                    if (product.getId() == Integer.parseInt(searchField)) {
                        matchingProducts.add(product);
                        return matchingProducts;
                    }
                }
            }
            if (matchingProducts.size() == 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "There are no matching IDs or names");
                alert.showAndWait();
            }
        }
        return matchingProducts;
    }

    /**
     * Function to initialize some starting values in the parts and products lists.
     */
    public static void initialValues() {
        final Outsourced brakes = new Outsourced(getNextPartId(), "Brakes", 12.99, 5, 1, 10, "Brakes Inc.");
        final InHouse tire = new InHouse(getNextPartId(), "Tire", 14.99, 6, 1, 10, 1);
        final InHouse rim = new InHouse(getNextPartId(), "Rim", 56.99, 3, 1, 10, 2);

        final Product giantBicycle = new Product(getNextProductId(), "Giant Bicycle", 299.99, 2, 1, 15);
        final Product scottBicycle = new Product(getNextProductId(), "Scott Bicycle", 199.99, 6, 1, 20);
        final Product gtBike = new Product(getNextProductId(), "GT Bike", 99.99, 10, 1, 30);

        Inventory.addPartsInventory(brakes);
        Inventory.addPartsInventory(tire);
        Inventory.addPartsInventory(rim);
        Inventory.addProductsInventory(giantBicycle);
        Inventory.addProductsInventory(scottBicycle);
        Inventory.addProductsInventory(gtBike);

        giantBicycle.addAssociatedPart(brakes);
        giantBicycle.addAssociatedPart(tire);
        scottBicycle.addAssociatedPart(brakes);
        scottBicycle.addAssociatedPart(tire);
    }
}
