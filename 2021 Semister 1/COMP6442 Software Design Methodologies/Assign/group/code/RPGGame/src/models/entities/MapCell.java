package models.entities;

import interfaces.Indexable;
import utils.GlobalMapCounter;

import java.util.ArrayList;

public class MapCell implements Indexable, Comparable {
    private /*@spec_public@*/int id;
    private /*@spec_public@*/String name;
    private /*@spec_public@*/boolean accessible;

    /*@requires id != null; String != null; accessible != null;
      @modifiable id; name; accessible
     */
    public MapCell(int id, String name, boolean accessible) {
        this.id = id;
        this.name = name;
        this.accessible = accessible;
        GlobalMapCounter.updateId(id);
    }
    /*@requires String != null; accessible != null;
      @assignable this.id;this.name;this.accessible;
     */
    public MapCell(String name, boolean accessible) {
        this.id = GlobalMapCounter.getId();
        this.name = name;
        this.accessible = accessible;
    }
    /*@ pure
    @ensure \result>=0 and =id
    @ */
    public int getId() {
        return id;
    }
    /*@ensures this.id == id;
      @ assignable this.id;
     @*/
    public void setId(int id) {
        this.id = id;
    }
    /*@ensure \result==name;
    @ */
    public /*@ pure @*/String getName() {
        return name;
    }
    /*@ensures this.name == name;
      @ assignable this.name;
     @*/
    public void setName(String name) {
        this.name = name;
    }

    /*@ensure \result==accessible;
    @ */
    public /*@ pure @*/boolean isAccessible() {
        return accessible;
    }

    /*@ensures this.accessible == accessible;
      @ assignable this.accessible;
     @*/
    public void setAccessible(boolean accessible) {
        this.accessible = accessible;
    }

    /*@ ensures \result == \n>>> Here is " + this.name + " (id: " + this.id + ") ;
      @*/
    public /*@ pure @*/String print() {

//        str.append("\n");
//        str.append("end. <<<\n");
        return "\n>>> Here is " + this.name + " (id: " + this.id + ")";
    }

    /*@@ requires object;
      @ ensures \result == id - ((MapCell)o).getId();
      @*/
    @Override
    public int compareTo(Object o) {
        return id - ((MapCell)o).getId();
    }
}
