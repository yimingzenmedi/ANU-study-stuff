package models.entities.tasks;

import enums.TaskType;
import models.entities.Task;
import models.entities.TaskEffect;

import java.util.ArrayList;

public class NPCTask extends Task {
    private int npcId;

    public NPCTask(int id, String name, String details, boolean finished, ArrayList<TaskEffect> effects, int npcId) {
        super(id, TaskType.NPC, name, details, finished, effects);
        this.npcId = npcId;
    }

    public NPCTask(int id, String name, String details, ArrayList<TaskEffect> effects, int npcId) {
        super(id, TaskType.NPC, name, details, effects);
        this.npcId = npcId;
    }

    public int getNpcId() {
        return npcId;
    }

    public void setNpcId(int npcId) {
        this.npcId = npcId;
    }
}
