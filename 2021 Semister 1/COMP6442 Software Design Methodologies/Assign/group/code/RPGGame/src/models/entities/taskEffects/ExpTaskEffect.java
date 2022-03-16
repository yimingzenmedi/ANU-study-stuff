package models.entities.taskEffects;

import enums.TaskEffectType;
import models.entities.TaskEffect;

public class ExpTaskEffect extends TaskEffect {
    private int exp;

    public ExpTaskEffect(int exp) {
        super(TaskEffectType.EXP);
        this.exp = exp;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
}
