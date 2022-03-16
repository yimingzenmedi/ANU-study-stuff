package models.entities.items;

import enums.ItemType;
import models.entities.Item;

public class Armor extends Item {
    private int defenceBoost;

    public Armor(int id, String name, int defenceBoost) {
        super(id, name, ItemType.ARMOR, true);
        this.defenceBoost = defenceBoost;
    }

    public int getDefenceBoost() {
        return defenceBoost;
    }

    public void setDefenceBoost(int defenceBoost) {
        this.defenceBoost = defenceBoost;
    }
}
