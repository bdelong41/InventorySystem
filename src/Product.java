import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents a Product made up of individual parts.
 */
public class Product {
    //Tracks in house parts
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    //Tracks outsourced parts
    private Integer id;
    private String name;
    private double price;
    private Integer stock;
    private int min;
    private int max;

    /**
     * Default Constructor, takes six args.
     * @param id int
     * @param name String
     * @param price double
     * @param stock int
     * @param min int
     * @param max int
     */
    public Product(int id, String name, double price, int stock, int min, int max){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    //Retrieval methods

    /**
     *
     * @return id int
     */
    public int getId(){ return id; }

    /**
     *
     * @return name String
     */
    public String getName() { return name; }

    /**
     *
     * @return price Double
     */
    public double getPrice(){ return price; }

    /**
     *
     * @return stock int
     */
    public int getStock() { return stock; }

    /**
     *
     * @return min int
     */
    public int getMin(){ return min; }

    /**
     *
     * @return max int
     */
    public int getMax() { return max; }

    /**
     *
     * @return ObservableList of type Part
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }

    //Alteration methods

    /**
     * Takes int arg
     * id=arg
     * @param id int
     */
    public void setId(int id){ this.id = id; }

    /**
     * Takes String arg
     * name=arg
     * @param name String
     */
    public void setName(String name) { this.name = name; }

    /**
     * Takes double arg
     * price=arg
     * @param price double
     */
    public void setPrice(double price){ this.price = price; }

    /**
     * Takes int arg
     * stock=arg
     * @param stock int
     */
    public void setStock(int stock) { this.stock = stock; }

    /**
     * Takes int arg
     * min=arg
     * @param min int
     */
    public void setMin(int min){ this.min = min; }

    /**
     * Takes int arg
     * min=arg
     * @param max int
     */
    public void setMax(int max) { this.max = max; }

    /**
     * Takes Part arg
     * associatedParts.add(arg)
     * @param part Part
     */
    public void addAssociatedPart(Part part){ associatedParts.add(part); }

    /**
     * Takes a Part object as arg.
     * Compares arg's ID member against indicies within associatedParts.
     * Returns true when an indicie with the associated ID is removed, returns false when no such indicie exists.
     * @param selectedPart Part
     * @return boolean
     */
    public boolean deleteAssociatedPart(Part selectedPart){
        for(int index = 0; index < associatedParts.size(); index++){
            if(associatedParts.get(index).getID() == selectedPart.getID()){
               if (associatedParts.remove(index) != null) {
                   return true;
               }
            }
        }
        return false;
    }
}
