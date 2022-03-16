package models.entities.taskEffects;

import enums.TaskEffectType;
import models.entities.TaskEffect;

public class ItemTaskEffect extends TaskEffect {
    private int itemId;

    public ItemTaskEffect(int itemId) {
        super(TaskEffectType.ITEM);
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}
