package models.entities.items;

import enums.ItemType;
import models.entities.Item;

public class Prop extends Item {
    public Prop(int id, String name) {
        super(id, name, ItemType.ARMOR, true);
    }
}
