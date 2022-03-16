package models.entities.actions;

import enums.ActionType;
import models.entities.Action;

import java.util.ArrayList;

public class NormalAction extends Action {
    public NormalAction(int id, String name, boolean oneTime, String preLines, String afterLines) {
        super(id, name, ActionType.NORMAL, oneTime, preLines, afterLines);
    }

    public NormalAction(int id, String name, boolean oneTime, String preLines, String afterLines, ArrayList<Integer> mapsUnlock) {
        super(id, name, ActionType.NORMAL, oneTime, preLines, afterLines, mapsUnlock);
    }
}
