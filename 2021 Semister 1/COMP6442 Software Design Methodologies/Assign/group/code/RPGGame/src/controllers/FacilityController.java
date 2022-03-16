package controllers;

import enums.FacilityType;
import models.State;
import models.entities.Facility;
import models.entities.facilities.Box;

import java.util.ArrayList;

public class FacilityController {
    public static void interact(int facilityId) {
        State state = State.getInstance();
        Facility facility = state.getFacilityById(facilityId);
        interact(facility);
    }

    public static void interact(Facility facility) {
        State state = State.getInstance();
        if (facility == null) {
            System.out.println("\nInvalid facility.");
            return;
        }
        System.out.println("\n" + facility.getActionLine());
        if (facility.getType() == FacilityType.BOX) {
            Box box = (Box) facility;
            ArrayList<Integer> itemIds = box.interact();
            if (itemIds.size() == 0) {
                System.out.println("\nEmpty...");
            } else {
                for (Integer itemId : itemIds) {
                    state.getBackpack().putInItem(itemId);
                }
            }
        }
    }
}
