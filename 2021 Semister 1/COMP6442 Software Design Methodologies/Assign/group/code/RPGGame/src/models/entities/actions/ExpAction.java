package models.entities.actions;

import enums.ActionType;
import models.entities.Action;

import java.util.ArrayList;

public class ExpAction extends Action {
    private int exp;

    public ExpAction(int id, String name, boolean oneTime, int exp, String preLines, String afterLines) {
        super(id, name, ActionType.EXP, oneTime, preLines, afterLines);
        this.exp = exp;
    }

    public ExpAction(int id, String name, boolean oneTime, int exp, String preLines, String afterLines, ArrayList<Integer> mapsUnlock) {
        super(id, name, ActionType.EXP, oneTime, preLines, afterLines, mapsUnlock);
        this.exp = exp;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
}
