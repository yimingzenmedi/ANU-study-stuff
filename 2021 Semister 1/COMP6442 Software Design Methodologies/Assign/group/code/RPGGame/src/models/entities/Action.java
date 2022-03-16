package models.entities;

import enums.ActionType;

import java.util.ArrayList;

public abstract class Action implements Comparable {
    private int id;
    private String name;
    private ActionType type;
    private boolean oneTime;
    private String preLines;
    private String afterLines;
    private ArrayList<Integer> mapsUnlock;

    public Action(int id, String name, ActionType type, boolean oneTime, String preLines, String afterLines) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.oneTime = oneTime;
        this.preLines = preLines;
        this.afterLines = afterLines;
        this.mapsUnlock = new ArrayList<>();
    }

    public Action(int id, String name, ActionType type, boolean oneTime, String preLines, String afterLines, ArrayList<Integer> mapsUnlock) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.oneTime = oneTime;
        this.preLines = preLines;
        this.afterLines = afterLines;
        this.mapsUnlock = mapsUnlock;
    }

    public ArrayList<Integer> getMapsUnlock() {
        return mapsUnlock;
    }

    public void setMapsUnlock(ArrayList<Integer> mapsUnlock) {
        this.mapsUnlock = mapsUnlock;
    }

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

    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    public boolean isOneTime() {
        return oneTime;
    }

    public void setOneTime(boolean oneTime) {
        this.oneTime = oneTime;
    }

    public String getAfterLines() {
        return afterLines;
    }

    public void setAfterLines(String afterLines) {
        this.afterLines = afterLines;
    }

    public String getPreLines() {
        return preLines;
    }

    public void setPreLines(String preLines) {
        this.preLines = preLines;
    }

    public int compareTo(Object o) {
        return id - ((Action)o).getId();
    }
}


