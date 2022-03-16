package models.entities;

import enums.FacilityType;
import enums.ItemType;
import interfaces.Indexable;

import java.util.ArrayList;

public abstract class Facility implements Indexable, Comparable {
    private int id;
    private String name;
    private FacilityType type;
    private String actionLine;

    public Facility(int id, String name, FacilityType type, String actionLine) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.actionLine = actionLine;
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

    public abstract String print();

    public abstract ArrayList<Integer> interact();

    public String getActionLine() {
        return actionLine;
    }

    public void setActionLine(String actionLine) {
        this.actionLine = actionLine;
    }

    public FacilityType getType() {
        return type;
    }

    public void setType(FacilityType type) {
        this.type = type;
    }

    public int compareTo(Object o) {
        return id - ((Facility)o).getId();
    }
}
