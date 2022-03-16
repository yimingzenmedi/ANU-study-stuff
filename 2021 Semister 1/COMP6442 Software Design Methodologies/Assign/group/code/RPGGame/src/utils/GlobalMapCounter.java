package utils;

public class GlobalMapCounter {
    private static int currentMapId = 1;

    public static void updateId(int id) {
        if (id > currentMapId) {
            currentMapId = id;
        }
        currentMapId++;
    }

    public static int getId() {
        return currentMapId++;
    }
}
