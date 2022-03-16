package models.entities;

import interfaces.Indexable;
import utils.GlobalMonsterCounter;

import java.util.ArrayList;

public class Monster implements Indexable, Comparable {
    private int id;
    private String name;
    private int defence;
    private int attack;
    private int hp;
    private int expReward;
    private ArrayList<Integer> itemRewordIds;
    private ArrayList<Integer> mapsUnlock;

    public Monster(int id, String name, int defence, int attack, int hp, int expReward, ArrayList<Integer> itemRewordIds, ArrayList<Integer> mapsUnlock) {
        this.id = id;
        this.name = name;
        this.defence = defence;
        this.attack = attack;
        this.hp = hp;
        this.expReward = expReward;
        this.itemRewordIds = itemRewordIds;
        this.mapsUnlock = mapsUnlock;
        GlobalMonsterCounter.updateId(id);
    }

    public Monster(String name, int defence, int attack, int hp, int expReward, ArrayList<Integer> itemRewordIds,  ArrayList<Integer> mapsUnlock) {
        this.id = GlobalMonsterCounter.getId();
        this.name = name;
        this.defence = defence;
        this.attack = attack;
        this.hp = hp;
        this.expReward = expReward;
        this.itemRewordIds = itemRewordIds;
        this.mapsUnlock = mapsUnlock;
        GlobalMonsterCounter.updateId(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExpReward() {
        return expReward;
    }

    public void setExpReward(int expReward) {
        this.expReward = expReward;
    }

    public ArrayList<Integer> getItemRewordIds() {
        return itemRewordIds;
    }

    public void setItemRewordIds(ArrayList<Integer> itemRewordIds) {
        this.itemRewordIds = itemRewordIds;
    }

    public ArrayList<Integer> getMapsUnlock() {
        return mapsUnlock;
    }

    public void setMapsUnlock(ArrayList<Integer> mapsUnlock) {
        this.mapsUnlock = mapsUnlock;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    @Override
    public int compareTo(Object o) {
        return id - ((Monster)o).getId();
    }
}
