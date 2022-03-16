package utils;

public class GlobalFacilityCounter {
    private static int currentFacilityId = 1;

    public static void updateId(int id) {
        if (id > currentFacilityId) {
            currentFacilityId = id;
            currentFacilityId++;
        }
    }

    public static int getId() {
        return currentFacilityId++;
    }
}
