import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Contains a collection of products and their associated parts
 * has two static members: allParts and allProducts
 */
public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    //Data retrieval methods

    /**
     *
     * @return allParts ObservableList of type Part
     */
    public ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     *
     * @return allProducts ObservableList of type Product
     */
    public ObservableList<Product> getAllProducts(){
        return allProducts;
    }

    /**
     * Takes int arg
     * Returns a Part object with id member equivalent to arg
     * @param partId int
     * @return Part
     */
    public Part lookupPart(int partId){
        for (int index = 0; index < allParts.size(); index++){
            if (allParts.get(index).getID() == partId){
                return allParts.get(index);
            }
        }
        return null;
    }

    /**
     * Takes int arg
     * Returns a Product object with id member equivalent to arg.
     * @param productId int
     * @return Product
     */
    public Product lookupProduct(int productId){
        for (int index = 0; index < allProducts.size(); index++){
            if (allProducts.get(index).getId() == productId){
                return allProducts.get(index);
            }
        }
        return null;
    }

    /**
     * Takes String arg
     * Returns list of Part objects with name member equivalent to arg.
     * @param partName String
     * @return ObservableList of type Part
     */
    public ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> partsList = FXCollections.observableArrayList();
        for(int index = 0; index < this.allParts.size(); index++){
            if (this.allParts.get(index).getName().equals(partName)){
                partsList.add(this.allParts.get(index));
            }
        }
        return partsList;
    }

    /**
     * Takes String arg
     * Returns list of Product objects with name mamber equivalent to arg.
     * @param productName String
     * @return ObservableList of type Product
     */
    public ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> productsList = FXCollections.observableArrayList();
        for(int index = 0; index < this.allProducts.size(); index++){
            if(this.allProducts.get(index).getName().equals(productName)){
                productsList.add(this.allProducts.get(index));
            }
        }
        return productsList;
    }

    //Data alteration methods

    /**
     * Takes int,Part as args
     * Sets indicie of allParts at index arg1 to arg2
     * @param index int
     * @param selectedPart Part
     */
    public void updatePart(int index, Part selectedPart){ allParts.set(index, selectedPart); }

    /**
     * Takes int,Product as args
     * Sets indicie of allProducts at index arg1 to arg2
     * @param index int
     * @param newProduct Part
     */
    public void updateProduct(int index, Product newProduct){ allProducts.set(index, newProduct); }

    /**
     * Takes Part as arg
     * Appends arg to allParts
     * @param newPart Part
     */
    public void addPart(Part newPart){ allParts.add(newPart); }

    /**
     * Takes Product as arg
     * Appends arg to allProducts
     * @param newProduct Product
     */
    public void addProduct(Product newProduct){ allProducts.add(newProduct); }

    /**
     * Takes Part as arg
     * Searches indicies of allParts for Part object with the same corresponding ID data member.
     * Returns true if such Part exists.
     * Returns false if no such Part exists or if arg is associated with any Product;
     * @param selectedPart Part
     * @return boolean
     */
    public Boolean deletePart(Part selectedPart){
        System.out.println("Function called");
        //Checking if selectedPart is associated to a product
        for (int index = 0; index < allProducts.size(); index++) {
            System.out.println(index);
            //Gathering associated parts of current product
            ObservableList<Part> assocParts = this.getAllProducts().get(index).getAllAssociatedParts();
            for(int counter = 0; counter < assocParts.size(); counter++){
                //comparing part id
                if(assocParts.get(counter).getID() == selectedPart.getID()){
                    return false;
                }
            }
        }
        return allParts.remove(selectedPart);
    }

    /**
     * Takes Product as arg
     * Removes arg from allProducts.
     * Returns true if arg exists within allProducts and Returns false if arg doesn't exist.
     * @param selectedProduct Product
     * @return boolean
     */
    public Boolean deleteProduct(Product selectedProduct){
        return allProducts.remove(selectedProduct);
    }

}