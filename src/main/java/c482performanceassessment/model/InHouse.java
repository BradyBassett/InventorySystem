package c482performanceassessment.model;

/**
 * This class is responsible for the InHouse data type which inherits from Part.
 * FUTURE ENHANCEMENT - Add additional values for the InHouse datatype such as date created, or a description.
 * @author Brady Bassett
 */
public class InHouse extends Part {
    private int machineId;

    /**
     * LOGIC ERROR - I attempted to initialize all inherited values without using the super keyword.
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * Sets the machineId value.
     * @param machineId value to be assigned to machineId
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * Returns the machineId value.
     * @return machineId value to be returned
     */
    public int getMachineId() {
        return machineId;
    }
}
