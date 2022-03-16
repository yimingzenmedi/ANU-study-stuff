package utils;

public class GlobalTaskCounter {
    private static int currentTaskId = 1;

    public static void updateId(int id) {
        if (id > currentTaskId) {
             currentTaskId = id;
        }
        currentTaskId++;
    }

    public static int getId() {
        return currentTaskId++;
    }
}
