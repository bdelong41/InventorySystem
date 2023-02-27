/**
 * Represents individual 'in-house' component used to construct products
 */
public class InHouse extends Part{

    private int machineId = 0;

    /**
     * Default constructor
     * @param id int
     * @param name String
     * @param price double
     * @param stock int
     * @param min int
     * @param max int
     * @param machineId int
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;

    }

    /**
     *
     * @return machineId int
     */
    //Retrieval methods
    public int getMachineID(){
        return machineId;
    }

    /**
     * Takes int arg
     * machineId=arg
     * @param machineId int
     */
    //Alteration methods
    public void setMachineId(int machineId){
        this.machineId = machineId;
    }
}
