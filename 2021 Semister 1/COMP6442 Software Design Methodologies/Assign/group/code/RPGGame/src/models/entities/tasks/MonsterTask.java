package models.entities.tasks;

import enums.TaskType;
import models.State;
import models.entities.Monster;
import models.entities.Task;
import models.entities.TaskEffect;

import java.util.ArrayList;

public class MonsterTask extends Task {
    private int monsterId;

    public MonsterTask(int id, String name, String details, boolean finished, ArrayList<TaskEffect> effects, int monsterId) {
        super(id, TaskType.MONSTER, name, details, finished, effects);
        this.monsterId = monsterId;
    }

    public MonsterTask(int id, String name, String details, ArrayList<TaskEffect> effects, int monsterId) {
        super(id, TaskType.MONSTER, name, details, effects);
        this.monsterId = monsterId;
    }

    public int getMonsterId() {
        return monsterId;
    }

    public void setMonsterId(int monsterId) {
        this.monsterId = monsterId;
    }
//    @Override
//    public boolean checkConditionMeets() {
//        State state = State.getInstance();
//        Monster monster = state.getMonsterById(monsterId);
//        if (monster == null) return false;
//        return monster.getHp() <= 0;
//    }
}
