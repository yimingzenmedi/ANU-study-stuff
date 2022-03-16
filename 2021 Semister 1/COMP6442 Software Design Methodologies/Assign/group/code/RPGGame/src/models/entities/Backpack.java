package models.entities;

import models.State;

import java.util.ArrayList;

public class Backpack {
    private ArrayList<Integer> items;

    private static final Backpack instance = new Backpack();

    private Backpack() {
        items = new ArrayList<>();
    }

//    private Backpack(ArrayList<Integer> items) {
//        this.items = items;
//    }

    public static Backpack getInstance() {
        return instance;
    }

    public ArrayList<Integer> getItems() {
        return items;
    }

    public void setItems(ArrayList<Integer> items) {
        this.items = items;
    }

    public void putInItem(int itemId) {
        this.items.add(itemId);
        Item item = State.getInstance().getItemById(itemId);
        System.out.println("\nGet new item: " + item.getName() + ".");
    }

    public Integer takeOutItem(int itemId) {
        for (int i = 0; i < items.size(); i++) {
            int id = items.get(i);
            if (id == itemId) {
                this.items.remove(i);
                System.out.println("Remove item from the backpack.");
                return id;
            }
        }
        System.out.println("Item not found in the backpack.");
        return null;
    }
}
