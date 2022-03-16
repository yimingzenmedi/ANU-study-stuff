package controllers;

import enums.ActionType;
import models.State;
import models.entities.Action;
import models.entities.MapCell;
import models.entities.NPC;
import models.entities.Protagonist;
import models.entities.actions.ExpAction;
import models.entities.actions.ItemAction;
import models.entities.actions.TaskAction;

public class NPCController {

    public static void interact(int npcId, int actionId) {
        State state = State.getInstance();
        NPC npc = state.getNPCById(npcId);
        Action action = npc.getActionById(actionId);
        interact(npc, action);
    }

    public static void interact(NPC npc, Action action) {
        if (action == null || npc == null) {
            System.out.println("\nInvalid npc or action.");
            return;
        }
        State state = State.getInstance();
        Protagonist protagonist = state.getProtagonist();
        System.out.println("\nYou: " + action.getPreLines());
        System.out.println(npc.getName() + ": " + action.getAfterLines());

        if (action.getType() == ActionType.EXP) {
            ExpAction expAction = (ExpAction) action;
            int exp = expAction.getExp();
            protagonist.gainExp(exp);
        } else if (action.getType() == ActionType.ITEM) {
            ItemAction itemAction = (ItemAction) action;
            if (itemAction.isTriggered() && itemAction.isOneTime()) {
                System.out.println("\nYou have already received the item.");
            } else {
                int itemId = itemAction.getItemId();
                state.getBackpack().putInItem(itemId);
            }
            itemAction.trigger();
        } else if (action.getType() == ActionType.TASK) {
            TaskAction taskAction = (TaskAction) action;
            int taskId = taskAction.getTaskId();
            TaskController.assignTask(taskId);
        }
        for (int mapId : action.getMapsUnlock()) {
            MapCell mapCell = state.getMapCellById(mapId);
            mapCell.setAccessible(true);
            System.out.println("\nUnlock map: " + mapCell.getName());
        }
    }
}
