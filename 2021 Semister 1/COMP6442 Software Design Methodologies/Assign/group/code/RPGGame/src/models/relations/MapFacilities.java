package models.relations;

import java.util.ArrayList;

public class MapFacilities implements Comparable {
    private int mapKey;
    private ArrayList<Integer> facilityIds;

    public MapFacilities(int mapKey, ArrayList<Integer> facilityIds) {
        this.mapKey = mapKey;
        this.facilityIds = facilityIds;
    }

    public MapFacilities(int mapKey) {
        this.mapKey = mapKey;
        this.facilityIds = new ArrayList<>();
    }

    public void addFacilityId(int facilityId) {
        this.facilityIds.add(facilityId);
    }

    public Integer removeFacility(int facilityId) {
        for (int i = 0; i < facilityIds.size(); i++) {
            int savedId = facilityIds.get(i);
            if (facilityId == savedId) {
                facilityIds.remove(i);
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

    public ArrayList<Integer> getFacilityIds() {
        return facilityIds;
    }

    public void setFacilityIds(ArrayList<Integer> facilityIds) {
        this.facilityIds = facilityIds;
    }

    @Override
    public int compareTo(Object o) {
        return mapKey - ((MapFacilities)o).getMapKey();
    }
}
