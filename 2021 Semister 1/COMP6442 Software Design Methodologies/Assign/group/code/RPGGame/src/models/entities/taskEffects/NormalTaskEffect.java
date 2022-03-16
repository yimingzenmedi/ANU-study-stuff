package models.entities.taskEffects;

import enums.TaskEffectType;
import models.entities.TaskEffect;

public class NormalTaskEffect extends TaskEffect {
    public NormalTaskEffect() {
        super(TaskEffectType.NORMAL);
    }
}
