package models.entities.items;

import enums.ItemType;
import models.entities.Item;

public class LifePotion extends Item {
    private int hpRecover;

    public LifePotion(int id, String name, int hpRecover) {
        super(id, name, ItemType.LIFE_POTION, true);
        this.hpRecover = hpRecover;
    }

    public int getHpRecover() {
        return hpRecover;
    }

    public void setHpRecover(int hpRecover) {
        this.hpRecover = hpRecover;
    }
}
