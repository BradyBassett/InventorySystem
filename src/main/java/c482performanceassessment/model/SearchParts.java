package c482performanceassessment.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class is responsible for handling the searchParts logic for all part searches.
 * FUTURE ENHANCEMENTS - Add more values to search on, such as inventory amount or price
 * @author Brady Bassett
 */
public class SearchParts {
    /**
     * Takes a string to compare to either the parts id or parts name and returns a list of all matching parts.
     * LOGIC ERROR - When comparing to the part name, if the provided string does not match the casing, an otherwise
     * matching part would not appear. This was solved by casting both the provided string and the part name to lowercase.
     * @param searchField string to match
     * @return observable list of all matching parts
     */
    public static ObservableList<Part> searchParts(String searchField, ObservableList<Part> parts) {
        ObservableList<Part> matchingParts = FXCollections.observableArrayList();
        if (searchField.length() > 0) {
            Pattern pattern = Pattern.compile("[^0-9]");
            Matcher matcher = pattern.matcher(searchField);     // Checks if the string provided contains numbers
            if (matcher.find()) {
                for (Part part : parts) {
                    if (part.getName().toLowerCase(Locale.ROOT).contains(searchField.toLowerCase(Locale.ROOT))) {
                        matchingParts.add(part);
                    }
                }
            } else {
                for (Part part : parts) {
                    if (part.getId() == Integer.parseInt(searchField)) {
                        matchingParts.add(part);
                        return matchingParts;
                    }
                }
            }
            if (matchingParts.size() == 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "There are no matching IDs or names");
                alert.showAndWait();
            }
        }
        return matchingParts;
    }
}
