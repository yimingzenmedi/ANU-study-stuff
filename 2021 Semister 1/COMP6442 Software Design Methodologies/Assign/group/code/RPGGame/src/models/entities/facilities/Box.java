package models.entities.facilities;

import enums.FacilityType;
import models.entities.Facility;
import utils.GlobalFacilityCounter;

import java.util.ArrayList;

public class Box extends Facility {
    private ArrayList<Integer> items;

    public Box(int id, ArrayList<Integer> items, String actionLine) {
        super(id, "Box", FacilityType.BOX, actionLine);
        this.items = items;
        GlobalFacilityCounter.updateId(id);
    }

    public Box(String name, ArrayList<Integer> items, String actionLine) {
        super(GlobalFacilityCounter.getId(), name, FacilityType.BOX, actionLine);
        this.items = items;
    }

    public ArrayList<Integer> getItems() {
        return items;
    }

    public void setItems(ArrayList<Integer> items) {
        this.items = items;
    }

    @Override
    public String print() {
        return ">>> Box (id: " + getId() + ")";
    }

    public ArrayList<Integer> interact() {
        ArrayList<Integer> items = new ArrayList<>(this.items);
        this.items = new ArrayList<>();
        return items;
    }
}
