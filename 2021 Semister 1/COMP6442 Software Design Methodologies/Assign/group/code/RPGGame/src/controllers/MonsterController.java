package controllers;

import models.State;
import models.entities.MapCell;
import models.entities.Monster;
import models.entities.Protagonist;

public class MonsterController {

    public static boolean fight(Monster monster) {
        if (monster == null) {
            System.out.println("\nInvalid monster.");
            return false;
        }
        State state = State.getInstance();
        Protagonist protagonist = state.getProtagonist();
        int monsterAttack = monster.getAttack();
        int monsterDefence = monster.getDefence();
        int protagonistAttack = protagonist.getAttack();
        int protagonistDefence = protagonist.getDefence();

        int monsterHPDecreased = calculateHP(protagonistAttack, monsterDefence);
        monster.setHp(monster.getHp() - monsterHPDecreased);

        System.out.println("\nMonster " + monster.getName() + ": hp -" + monsterHPDecreased
                + ", left: " + monster.getHp());
        if (monster.getHp() <= 0) {
            System.out.println("\nYou beat the monster: " + monster.getName() + "!");
            protagonist.gainExp(monster.getExpReward());
            for (int itemId : monster.getItemRewordIds()) {
                state.getBackpack().putInItem(itemId);
            }
            for (int mapId : monster.getMapsUnlock()) {
                MapCell mapCell = state.getMapCellById(mapId);
                System.out.println("\nUnlock map: " + mapCell.getName());
                mapCell.setAccessible(true);
            }
            return true;
        }
        int protagonistHPDecreased = calculateHP(monsterAttack, protagonistDefence);
        protagonist.setHp(protagonist.getHp() - protagonistHPDecreased);
        System.out.println("\nYou: hp -" + protagonistHPDecreased + ", left: " + protagonist.getHp());
        return false;
    }

    public static int calculateHP(int attack, int defence) {
        return Math.max(1, attack * attack / (attack + defence));
    }
}
