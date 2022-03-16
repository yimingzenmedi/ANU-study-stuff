package controllers;

import enums.ItemType;
import enums.TaskEffectType;
import enums.TaskType;
import models.State;
import models.entities.*;
import models.entities.taskEffects.ExpTaskEffect;
import models.entities.taskEffects.FinishTaskEffect;
import models.entities.taskEffects.ItemTaskEffect;
import models.entities.taskEffects.MapTaskEffect;
import models.entities.tasks.ItemTask;
import models.entities.tasks.MapTask;
import models.entities.tasks.MonsterTask;
import models.entities.tasks.NPCTask;

import java.util.ArrayList;

public class TaskController {
    public static void assignTask(int taskId) {
        State state = State.getInstance();
        System.out.println("\nNew task: " + state.getTaskById(taskId).getName());
        state.assignNewTask(taskId);
    }

    // check whether task conditions are met:
    public static void checkMapTasks() {
        State state = State.getInstance();
        for (Integer taskId : state.getCurrentTasks()) {
            Task task = state.getTaskById(taskId);
            if (task != null && task.getTaskType() == TaskType.MAP) {
                MapTask mapTask = (MapTask) task;
                int mapKey = mapTask.getMapKey();
                MapCell mapCell = state.getMapCellById(mapKey);
                if (mapCell != null && mapCell.isAccessible()) {
                    finishTask(taskId);
                }
            }
        }
    }

    public static void checkNPCTasks(int interactingNPCKey) {
//        System.out.println("checkNPCTasks: " + interactingNPCKey);
        State state = State.getInstance();
        for (Integer taskId : state.getCurrentTasks()) {
            Task task = state.getTaskById(taskId);
            if (task != null && task.getTaskType() == TaskType.NPC) {
                NPCTask npcTask = (NPCTask) task;
                int npcKey = npcTask.getNpcId();
//                System.out.println("task: " + task.getName() + "npcKey: " + npcKey + ", interactingNPCKey: " + interactingNPCKey);
                if (npcKey == interactingNPCKey) {
//                    System.out.println("get");
                    finishTask(taskId);
                    break;
                }
            }
        }
    }

    public static void checkItemTasks(int interactingNPCKey) {
        State state = State.getInstance();
        for (Integer taskId : state.getCurrentTasks()) {
            Task task = state.getTaskById(taskId);
            if (task != null && task.getTaskType() == TaskType.ITEM) {
                ItemTask itemTask = (ItemTask) task;
                int itemKey = itemTask.getItemId();
                if (state.getBackpack().getItems().contains(itemKey)) {
                    finishTask(taskId);
                }
            }
        }
    }

    public static void checkMonsterTasks() {
        State state = State.getInstance();
        for (Integer taskId : state.getCurrentTasks()) {
            Task task = state.getTaskById(taskId);
            if (task != null && task.getTaskType() == TaskType.MONSTER) {
                MonsterTask monsterTask = (MonsterTask) task;
                int monsterId = monsterTask.getMonsterId();
                if (state.getMonsterById(monsterId).getHp() <= 0) {
                    finishTask(taskId);
                }
            }
        }
    }

    public static void finishTask(int taskId) {
        State state = State.getInstance();
        Protagonist protagonist = state.getProtagonist();
        Task task = state.getTaskById(taskId);
        if (task == null) return;
        task.setFinished(true);
        state.finishCurrentTask(taskId);
        System.out.println("\nTask finished: " + task.getName());
        ArrayList<TaskEffect> effects = task.getEffects();
        for (TaskEffect taskEffect : effects) {
            TaskEffectType type = taskEffect.getType();
            if (type == TaskEffectType.EXP) {
                ExpTaskEffect expTaskEffect = (ExpTaskEffect) taskEffect;
                protagonist.gainExp(expTaskEffect.getExp());
                System.out.println("Gain exp: " + expTaskEffect.getExp());
            } else if (type == TaskEffectType.ITEM) {
                ItemTaskEffect itemTaskEffect = (ItemTaskEffect) taskEffect;
                state.getBackpack().putInItem(itemTaskEffect.getItemId());
                System.out.println("Get items: " + itemTaskEffect.getItemId());
            } else if (type == TaskEffectType.MAP) {
                MapTaskEffect mapTaskEffect = (MapTaskEffect) taskEffect;
                state.getMapCellById(mapTaskEffect.getMapId()).setAccessible(true);
                System.out.println("Unlock map: " + state.getMapCellById(mapTaskEffect.getMapId()).getName());
            } else if (type == TaskEffectType.FINISH) {
                FinishTaskEffect finishTaskEffect = (FinishTaskEffect) taskEffect;
                System.out.println("\n" + finishTaskEffect.getLines());
                System.out.println("\n************* Game finish *************");
                state.setFinished(true);
            }
        }
    }
}
