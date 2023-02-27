/**
 * Subclass Represents an individual 'outsourced' component of a product.
 */
public class Outsourced extends Part{

    private String companyName;

    /**
     * Default constructor
     * @param id int
     * @param name String
     * @param price double
     * @param stock int
     * @param min int
     * @param max int
     * @param companyName String
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super (id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    //Retrieval methods

    /**
     *
     * @return companyName String
     */
    public String getCompanyName(){ return companyName; }

    //Alteration methods

    /**
     * Takes String arg
     * companyName=arg
     * @param companyName String
     */
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }
}
