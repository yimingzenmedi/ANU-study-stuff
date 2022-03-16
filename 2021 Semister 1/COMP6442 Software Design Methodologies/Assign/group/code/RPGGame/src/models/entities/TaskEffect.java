package models.entities;

import enums.TaskEffectType;

public abstract class TaskEffect {
    private TaskEffectType type;

    public TaskEffect(TaskEffectType type) {
        this.type = type;
    }

    public TaskEffectType getType() {
        return type;
    }

    public void setType(TaskEffectType type) {
        this.type = type;
    }
}
