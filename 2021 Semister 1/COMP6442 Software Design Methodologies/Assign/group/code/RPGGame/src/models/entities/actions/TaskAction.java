package models.entities.actions;

import enums.ActionType;
import models.entities.Action;

import java.util.ArrayList;

public class TaskAction extends Action {
    private int taskId;


    public TaskAction(int id, String name, boolean oneTime, int taskId, String preLines, String afterLines) {
        super(id, name, ActionType.TASK, oneTime, preLines, afterLines);
        this.taskId = taskId;
    }

    public TaskAction(int id, String name, boolean oneTime, int taskId, String preLines, String afterLines, ArrayList<Integer> mapsUnlock) {
        super(id, name, ActionType.TASK, oneTime, preLines, afterLines, mapsUnlock);
        this.taskId = taskId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}


