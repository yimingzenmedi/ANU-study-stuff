package models.entities.actions;

import enums.ActionType;
import models.entities.Action;

public class ItemAction extends Action {
    private int itemId;
    private boolean triggered;

    public ItemAction(int id, String name, boolean oneTime, int itemId, String preLines, String afterLines) {
        super(id, name, ActionType.ITEM, oneTime, preLines, afterLines);
        this.itemId = itemId;
    }

    public ItemAction(int id, String name, boolean oneTime, boolean triggered, int itemId, String preLines, String afterLines) {
        super(id, name, ActionType.ITEM, oneTime, preLines, afterLines);
        this.itemId = itemId;
        this.triggered = triggered;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    public void trigger() {
        this.triggered = true;
    }
}
