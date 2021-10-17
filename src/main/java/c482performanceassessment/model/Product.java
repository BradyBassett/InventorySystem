package c482performanceassessment.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class is responsible for creating the Product data type.
 * FUTURE ENHANCEMENT - Add a function to increment the stock of the product.
 * @author Brady Bassett
 */
public class Product extends SearchParts {
    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product() {
        this.id = 0;
        this.name = "";
        this.price = 0;
        this.stock = 0;
        this.min = -1;
        this.max = 1;
    }

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Sets the product ID.
     * @param id product id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the product name.
      * @param name product name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the product price.
     * @param price product price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Sets the product stock.
     * @param stock product stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Sets the minimum product stock.
     * @param min minimum product stock to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Sets the maximum product stock.
     * @param max maximum product stock to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Returns the product ID.
     * @return product id returned
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the product name.
     * @return product name returned
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the product price.
     * @return product price returned
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns the product stock.
     * @return product stock returned
     */
    public int getStock() {
        return stock;
    }

    /**
     * Returns the minimum product stock.
     * @return minimum product stock returned
     */
    public int getMin() {
        return min;
    }

    /**
     * Returns the maximum product stock.
     * @return maximum product stock returned
     */
    public int getMax() {
        return max;
    }

    /**
     * Returns the associatedParts observable list.
     * @return associatedParts observable list returned
     */
    public ObservableList<Part> getAssociatedParts() {
        return associatedParts;
    }

    /**
     * Adds an associated part to the associatedParts observable list.
     * @param part part to be added to associatedParts
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * Deletes an associated part from the associatedParts observable list.
     * @param part part to be removed from associatedParts
     */
    public void deleteAssociatedPart(Part part) {
        associatedParts.remove(part);
    }

    /**
     * Checks associatedParts to see if the part provided is already present.
     * LOGIC ERROR - used == to compare part to partInList. Fixed by using .equals instead of == because we were comparing
     * Objects instead of primitives.
     * @param part part to be checked
     * @return returns true if part is present and false otherwise
     */
    public boolean includes(Part part) {
        for (Part partInList : associatedParts) {
            if (part.equals(partInList)) {
                return true;
            }
        }
        return false;
    }
}
