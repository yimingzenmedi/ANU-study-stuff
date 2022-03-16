package models.entities;

import enums.TaskType;
import interfaces.Indexable;

import java.util.ArrayList;


public abstract class Task implements Indexable, Comparable {
    private int id;
    private TaskType taskType;
    private String name;
    private String details;
    private boolean finished;
    private ArrayList<TaskEffect> effects;

    public Task(int id, TaskType taskType, String name, String details, boolean finished, ArrayList<TaskEffect> effects) {
        this.id = id;
        this.taskType = taskType;
        this.name = name;
        this.details = details;
        this.finished = finished;
        this.effects = effects;
    }

    public Task(int id, TaskType taskType, String name, String details, ArrayList<TaskEffect> effects) {
        this.id = id;
        this.taskType = taskType;
        this.name = name;
        this.details = details;
        this.finished = false;
        this.effects = effects;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public ArrayList<TaskEffect> getEffects() {
        return effects;
    }

    public void setEffects(ArrayList<TaskEffect> effects) {
        this.effects = effects;
    }

//    public abstract boolean checkConditionMeets();

    public String print() {
        String s = ">>> Task: " + name + "(id: " + id + "),\n";
        s += "\tdetails: \n" + this.details;
        if (finished) {
            s += "\n\tFINISHED!";
        }
        return s;
    }

    public int compareTo(Object o) {
        return id - ((Task)o).getId();
    }
}
