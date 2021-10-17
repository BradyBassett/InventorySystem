package c482performanceassessment.controllers;

import c482performanceassessment.model.InHouse;
import c482performanceassessment.model.Inventory;
import c482performanceassessment.model.Outsourced;
import c482performanceassessment.model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

/**
 * This class is responsible for controlling all functionality relating to the part form.
 * FUTURE ENHANCEMENT - Could add functionality to add the part directly to a products associatedParts list from add/modify.
 * part form
 * @author Brady Bassett
 */
public class PartFormController extends BaseFormController {
    @FXML
    private Label partFormTitle;
    @FXML
    private RadioButton inHouse;
    @FXML
    private RadioButton outsourced;
    @FXML
    private Label machineIdLabel;
    @FXML
    private Label companyNameLabel;
    @FXML
    private TextField partId;
    @FXML
    private TextField partName;
    @FXML
    private TextField partInventory;
    @FXML
    private TextField partPrice;
    @FXML
    private TextField partMaxInventory;
    @FXML
    private TextField partMinInventory;
    @FXML
    private TextField machineId;
    @FXML
    private TextField companyName;

    private boolean inHouseSelected = true;
    private boolean outsourcedSelected = false;
    private Part workingPart;

    /**
     * Sets partFormTitle text to either "Modify Part" or "Add Part".
     * @param title String containing "Modify" or "Add"
     */
    protected void setTitle(String title) {
        partFormTitle.setText(title + " Part");
    }

    /**
     * Sets the part currently being worked with.
     * @param part part to be assigned to workingPart
     */
    private void setWorkingPart(Part part) {
        this.workingPart = part;
    }

    /**
     * Fills the TextBoxes with the appropriate data from part being modified.
     * @param part part being modified
     */
    @FXML
    protected void loadPartData(Part part) {
        partId.setText(String.valueOf(part.getId()));
        partName.setText(part.getName());
        partInventory.setText(String.valueOf(part.getStock()));
        partPrice.setText(String.valueOf(part.getPrice()));
        partMaxInventory.setText(String.valueOf(part.getMax()));
        partMinInventory.setText(String.valueOf(part.getMin()));

        if (part.getClass().getSimpleName().equals("InHouse")) {
            machineId.setText(String.valueOf(((InHouse) part).getMachineId()));
        } else {
            outsourced.setSelected(true);
            radioButtonSelector();
            companyName.setText(String.valueOf(((Outsourced) part).getCompanyName()));
        }
        setWorkingPart(part);
    }

    /**
     * Sets either machineId or companyName to visible depending on if InHouse or Outsourced is selected on the radio
     * buttons.
     * RUNTIME ERROR - experienced a NullPointerException for inHouse and outsourced. I fixed this by realizing that
     * the @FXML decorator was not above each variable declaration.
     */
    @FXML
    private void radioButtonSelector() {
        if (inHouse.isSelected()) {
            inHouseSelected = true;
            outsourcedSelected = false;
        } else if (outsourced.isSelected()) {
            inHouseSelected = false;
            outsourcedSelected = true;
        }

        machineIdLabel.setVisible(inHouseSelected);
        machineId.setVisible(inHouseSelected);
        companyNameLabel.setVisible(outsourcedSelected);
        companyName.setVisible(outsourcedSelected);
    }

    /**
     * Creates a new InHouse or Outsourced Part and modifies or adds it to the Parts list if there is a workingPart present.
     * @param event ActionEvent that provides the main stage declared in main
     * @throws IOException if the main-form.fxml file is unreachable
     */
    @FXML
    private void onAddSave(ActionEvent event) throws IOException {
        try {
            if (machineId.isVisible() && machineId.getText().isEmpty()) {
                throw new NullPointerException("Machine ID field is empty");
            }
            if (companyName.isVisible() && companyName.getText().isEmpty()) {
                throw new NullPointerException("Company name field is empty");
            }
            saveValidation(partName, partInventory, partPrice, partMaxInventory, partMinInventory);

            if (workingPart != null) {
                if (inHouseSelected) {
                    InHouse newPart = new InHouse(workingPart.getId(), partName.getText(), Double.parseDouble(partPrice.getText()),
                            Integer.parseInt(partInventory.getText()), Integer.parseInt(partMinInventory.getText()),
                            Integer.parseInt(partMaxInventory.getText()), Integer.parseInt(machineId.getText()));
                    Inventory.modifyPartsInventory(Inventory.getPartsInventory().indexOf(workingPart), newPart);
                } else {
                    Outsourced newPart = new Outsourced(workingPart.getId(), partName.getText(), Double.parseDouble(partPrice.getText()),
                            Integer.parseInt(partInventory.getText()), Integer.parseInt(partMinInventory.getText()),
                            Integer.parseInt(partMaxInventory.getText()), companyName.getText());
                    Inventory.modifyPartsInventory(Inventory.getPartsInventory().indexOf(workingPart), newPart);
                }
            } else {
                if (inHouseSelected) {
                    InHouse newPart = new InHouse(Inventory.getNextPartId(), partName.getText(), Double.parseDouble(partPrice.getText()),
                            Integer.parseInt(partInventory.getText()), Integer.parseInt(partMinInventory.getText()),
                            Integer.parseInt(partMaxInventory.getText()), Integer.parseInt(machineId.getText()));
                    Inventory.addPartsInventory(newPart);
                } else {
                    Outsourced newPart = new Outsourced(Inventory.getNextPartId(), partName.getText(), Double.parseDouble(partPrice.getText()),
                            Integer.parseInt(partInventory.getText()), Integer.parseInt(partMinInventory.getText()),
                            Integer.parseInt(partMaxInventory.getText()), companyName.getText());
                    Inventory.addPartsInventory(newPart);
                }
            }
            switchToMainScene(event);
        } catch (NullPointerException | IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.showAndWait();
        }
    }
}
