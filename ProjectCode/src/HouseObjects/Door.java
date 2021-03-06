package HouseObjects;

/**
 * A class for keeping track of Doors attached to rooms.
 *
 * @author a_richard, d_ruiz-cigana
 */
public class Door {

    /**
     * Unique identifier for door.
     */
    int id;

    /**
     * Whether or not door is open.
     */
    boolean open;
    
    /**
     * whether or not door is locked
     */
    boolean locked;

    /**
     * Name of the door.
     */
    public String name;

    /**
     * Default constructor.
     */
    public Door() {
        id = 0;
        open = false;
        locked = false;
        name = "";
    }

    /**
     * Parameterized constructor.
     *
     * @param id new id of the door
     * @param open if the door is open
     * @param name name of the door
     */
    public Door(int id, boolean open, boolean locked, String name) {
        this.id = id;
        this.open = open;
        this.locked = locked;
        this.name = name;
    }

    /**
     * Get method for ID.
     *
     * @return the door's unique id
     */
    public int getID() {
        return id;
    }

    /**
     * Get method for open status.
     *
     * @return if the door is open
     */
    public boolean getOpen() {
        return open;
    }
    
    /**
     * Get method for locked status.
     * 
     * @return is the door is locked
     */
    public boolean getLocked(){
    	return locked;
    }

    /**
     * Get method for the name.
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Set method for ID.
     *
     * @param id new unique id
     */
    public void setID(int id) {
        this.id = id;
    }

    /**
     * Set method for open status.
     *
     * @param open if the door is open
     */
    public void setOpen(boolean open) {
        this.open = open;
    }
    
    /**
     * Set method for the locked status.
     * 
     * @param locked if door is locked
     */
    public void setLocked(boolean locked){
    	this.locked = locked;
    }

    /**
     * Set method for the name.
     *
     * @param name the name of the door
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * This will print the name of the given door
     */
    public String toString(){
    	return name;
    }

}
