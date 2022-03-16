package models.entities.taskEffects;

import enums.TaskEffectType;
import models.entities.TaskEffect;

public class FinishTaskEffect extends TaskEffect {
    private String lines;
    public FinishTaskEffect(String lines) {
        super(TaskEffectType.FINISH);
        this.lines = lines;
    }

    public String getLines() {
        return lines;
    }

    public void setLines(String lines) {
        this.lines = lines;
    }
}
