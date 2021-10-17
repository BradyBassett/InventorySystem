package c482performanceassessment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import c482performanceassessment.model.Inventory;

import java.util.*;

/**
 * This class is responsible for starting the application and displaying the main scene.
 * FUTURE ENHANCEMENT - I would implement some sort of way to save data to eiter some database, or on a users machine
 * so that data could be saved between application uses.
 * @author Brady Bassett
 */
public class Main extends Application {

    /**
     * displays the main scene on startup.
     * RUNTIME ERROR - declared root as FXMLLoader instead of Parent, resulting in the root being unable to locate the
     * main form. Fixed simply by switching the FXMLLoader declaration of root to Parent.
     * @param stage primary stage that the application is built upon
     */
    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/c482performanceassessment/main-form.fxml")));
            Scene mainScene = new Scene(root);
            stage.setTitle("Inventory Management System");
            stage.setResizable(false);
            stage.setScene(mainScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * initializes initial values and launches the stage.
     * Javadoc folder is in Javadocs folder located in directory root
     * @param args contains command line arguments
     */
    public static void main(String[] args) {
        Inventory.initialValues();
        launch();
    }
}