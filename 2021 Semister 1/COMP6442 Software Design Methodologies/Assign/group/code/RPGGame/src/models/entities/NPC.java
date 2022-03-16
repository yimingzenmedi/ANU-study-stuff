package models.entities;

import interfaces.Indexable;
import utils.GlobalNPCCounter;

import java.util.ArrayList;

public class NPC implements Indexable, Comparable {
    private int id;
    private String name;
    private ArrayList<Action> actions;

    public NPC(int id, String name, ArrayList<Action> actions) {
        this.id = id;
        this.name = name;
        this.actions = actions;
        GlobalNPCCounter.updateId(id);
//        System.out.println("NEW NPC with Id: " + name + "(" + id + ")" + ", actions: " + actions.size());
    }

    public NPC(String name, ArrayList<Action> actions) {
        this.id = GlobalNPCCounter.getId();
        this.name = name;
        this.actions = actions;
//        System.out.println("NEW NPC: " + name + "(" + id + ")" + ", actions: " + actions.size());
    }

    public NPC(String name) {
        this.id = GlobalNPCCounter.getId();
        this.name = name;
        this.actions = new ArrayList<>();
//        System.out.println("NEW NPC: " + name + "(" + id + ")" + ", actions: " + actions.size());
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

    public String print() {
        String str = ">>> NPC: " + this.getName() + " (id: " + this.getId() + "),\n";
        str += "end. <<<";
        return str;
    }

    protected String saySomething(String msg) {
        return this.getName() + ": " + msg;
    }

    public ArrayList<Action> getActions() {
        return this.actions;
    }

    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }

    public Action getActionById(int actionId) {
        for (Action action : this.actions) {
            if (action.getId() == actionId) {
                return action;
            }
        }
        return null;
    }

//    public abstract void interact(int actionId);
    public String bye() {
        return "Bye bye!";
    }

    public void addAction(Action action) {
        if (actions == null) {
            actions = new ArrayList<>();
        }
        actions.add(action);
    }

    @Override
    public int compareTo(Object npc) {
        return id - ((NPC)npc).getId();
    }
}
