package controllers;

import models.State;
import models.entities.MapCell;

public class MapController {
    public static void changeMap(int mapKey) {
        State state = State.getInstance();
        MapCell mapCell = state.getMapCellById(mapKey);
        changeMap(mapCell);
    }

    public static void changeMap(MapCell mapCell) {
        State state = State.getInstance();
        if (mapCell == null) {
            System.out.println("\nInvalid map to go!!");
            return;
        }
        int mapKey = mapCell.getId();
        if (!mapCell.isAccessible()) {
            System.out.println("\nSorry, you cannot go there now...");
        } else {
            state.setPosition(mapKey);
            System.out.println("\nGoing to " + mapCell.getName() + ".");
            TaskController.checkMapTasks();
        }
    }
}
