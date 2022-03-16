package models.entities.items;

import enums.ItemType;
import models.entities.Item;

public class Weapon extends Item {
    private int attackBoost;

    public Weapon(int id, String name, int attackBoost) {
        super(id, name, ItemType.WEAPON, true);
        this.attackBoost = attackBoost;
    }

    public int getAttackBoost() {
        return attackBoost;
    }

    public void setAttackBoost(int attackBoost) {
        this.attackBoost = attackBoost;
    }
}
