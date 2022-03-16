package models.entities.tasks;

import enums.TaskType;
import models.entities.Task;
import models.entities.TaskEffect;

import java.util.ArrayList;

public class ItemTask extends Task {
    private int itemId;

    public ItemTask(int id, String name, String details, boolean finished, ArrayList<TaskEffect> effects, int itemId) {
        super(id, TaskType.ITEM, name, details, finished, effects);
        this.itemId = itemId;
    }

    public ItemTask(int id, String name, String details, ArrayList<TaskEffect> effects, int itemId) {
        super(id, TaskType.ITEM, name, details, effects);
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}
