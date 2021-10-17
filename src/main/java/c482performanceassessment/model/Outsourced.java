package c482performanceassessment.model;

/**
 * This class is responsible for the Outsourced data type which inherits from Part.
 * FUTURE ENHANCEMENT - Add additional values for the Outsourced datatype such as date created, or a description of the
 * company that created the part.
 * @author Brady Bassett
 */
public class Outsourced extends Part {
    private String companyName;

    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Sets the companyName value.
     * @param companyName value to be assigned to companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Returns the companyName.
     * @return companyName to be returned
     */
    public String getCompanyName() {
        return companyName;
    }
}
