package models.relations;

import java.util.ArrayList;

public class MapNPCs implements Comparable {
    private int mapKey;
    private ArrayList<Integer> npcList;

    public MapNPCs(int mapKey, ArrayList<Integer> npcList) {
        this.mapKey = mapKey;
        this.npcList = npcList;
    }

    public MapNPCs(int mapKey) {
        this.mapKey = mapKey;
        this.npcList = new ArrayList<>();
    }

    public void addNPC(int npcId) {
        this.npcList.add(npcId);
    }

    public Integer removeNPC(int npcId) {
        for (int i = 0; i < npcList.size(); i++) {
            int savedId = npcList.get(i);
            if (savedId == npcId) {
                npcList.remove(i);
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

    public ArrayList<Integer> getNpcList() {
        return npcList;
    }

    public void setNpcList(ArrayList<Integer> npcList) {
        this.npcList = npcList;
    }

    @Override
    public int compareTo(Object o) {
        return mapKey - ((MapNPCs)o).getMapKey();
    }
}
