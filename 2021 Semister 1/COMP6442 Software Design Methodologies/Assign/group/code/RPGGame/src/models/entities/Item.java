package models.entities;

import enums.ItemType;
import interfaces.Indexable;
import utils.GlobalItemCounter;

public class Item implements Indexable, Comparable {
    private /*@spec_public@*/int id;
    private /*@spec_public@*/String name;
    private /*@spec_public@*/ItemType type;
    private /*@spec_public@*/boolean usable;

    public Item(int id, String name, ItemType type, boolean usable) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.usable = usable;
        GlobalItemCounter.updateId(id);
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

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public boolean isUsable() {
        return usable;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
    }

    @Override
    public int compareTo(Object o) {
        return id - ((Item)o).getId();
    }
}
