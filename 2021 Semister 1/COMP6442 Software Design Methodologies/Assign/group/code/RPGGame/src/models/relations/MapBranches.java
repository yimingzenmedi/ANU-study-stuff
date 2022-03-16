package models.relations;

import java.util.ArrayList;

public class MapBranches implements Comparable {
    private int mapKey;
    private ArrayList<Integer> branchKeys;

    public MapBranches(int mapKey, ArrayList<Integer> branches) {
        this.mapKey = mapKey;
        this.branchKeys = branches;
    }

    public MapBranches(int mapKey) {
        this.mapKey = mapKey;
        this.branchKeys = new ArrayList<>();
    }

    public void addBranch(Integer branch) {
        this.branchKeys.add(branch);
    }

    public Integer removeBranch(int branchId) {
        for (int i = 0; i < branchKeys.size(); i++) {
            int savedId = branchKeys.get(i);
            if (branchId == savedId) {
                branchKeys.remove(i);
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

    public ArrayList<Integer> getBranchKeys() {
        return branchKeys;
    }

    public void setBranchKeys(ArrayList<Integer> branchKeys) {
        this.branchKeys = branchKeys;
    }

    @Override
    public int compareTo(Object o) {
        return mapKey - ((MapBranches)o).getMapKey();
    }
}
