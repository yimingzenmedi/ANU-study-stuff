package models.entities.facilities;

import enums.FacilityType;
import models.entities.Facility;
import utils.GlobalFacilityCounter;

import java.util.ArrayList;

public class NormalFacility extends Facility {

    public NormalFacility(int id, String name, ArrayList<Integer> items, String actionLine) {
        super(id, name, FacilityType.NORMAL, actionLine);
        GlobalFacilityCounter.updateId(id);
    }

    public NormalFacility(String name, ArrayList<Integer> items, String actionLine) {
        super(GlobalFacilityCounter.getId(), name, FacilityType.NORMAL, actionLine);
    }

    @Override
    public String print() {
        return ">>> Box (id: " + getId() + ")";
    }

    @Override
    public ArrayList<Integer> interact() {
        return null;
    }

}
