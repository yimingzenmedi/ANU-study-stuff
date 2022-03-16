package models.entities.tasks;

import enums.TaskType;
import models.State;
import models.entities.MapCell;
import models.entities.Task;
import models.entities.TaskEffect;

import java.util.ArrayList;

public class MapTask extends Task {
    private int mapKey;

    public MapTask(int id, String name, String details, boolean finished, ArrayList<TaskEffect> effects, int mapKey) {
        super(id, TaskType.MAP, name, details, finished, effects);
        this.mapKey = mapKey;
    }

    public MapTask(int id, String name, String details, ArrayList<TaskEffect> effects, int mapKey) {
        super(id, TaskType.MAP, name, details, effects);
        this.mapKey = mapKey;
    }

    public int getMapKey() {
        return mapKey;
    }

    public void setMapKey(int mapKey) {
        this.mapKey = mapKey;
    }

    //    @Override
//    public boolean checkConditionMeets() {
//        State state = State.getInstance();
//        MapCell mapCell = state.getMapCellById(mapKey);
//        if (mapCell == null) return false;
//        return mapCell.isAccessible();
//    }
}
