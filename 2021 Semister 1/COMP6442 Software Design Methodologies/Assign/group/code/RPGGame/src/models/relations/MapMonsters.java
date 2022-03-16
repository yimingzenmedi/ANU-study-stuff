package models.relations;

import java.util.ArrayList;

public class MapMonsters implements Comparable {
    private int mapKey;
    private ArrayList<Integer> monsterIds;

    public MapMonsters(int mapKey, ArrayList<Integer> monsterIds) {
        this.mapKey = mapKey;
        this.monsterIds = monsterIds;
    }

    public MapMonsters(int mapKey) {
        this.mapKey = mapKey;
        this.monsterIds = new ArrayList<>();
    }

    public void addMonsterId(int monsterId) {
        this.monsterIds.add(monsterId);
    }

    public Integer removeMonster(int monsterId) {
        for (int i = 0; i < monsterIds.size(); i++) {
            int savedId = monsterIds.get(i);
            if (monsterId == savedId) {
                monsterIds.remove(i);
                return savedId;
            }
        }
        return null;
    }

    public int getMapKey() {
        return mapKey;
    }

    public void setMapKey(int mapKey) {
        this.mapKey = mapKey;
    }

    public ArrayList<Integer> getMonsterIds() {
        return monsterIds;
    }

    public void setMonsterIds(ArrayList<Integer> monsterIds) {
        this.monsterIds = monsterIds;
    }

    @Override
    public int compareTo(Object o) {
        return mapKey - ((MapMonsters)o).getMapKey();
    }
}
