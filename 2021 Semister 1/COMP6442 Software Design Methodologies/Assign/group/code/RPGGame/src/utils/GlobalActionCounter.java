package utils;

public class GlobalActionCounter {
    private static int currentActionId = 1;

    public static void updateId(int id) {
        if (id > currentActionId) {
             currentActionId = id;
        }
        currentActionId++;
    }

    public static int getId() {
        return currentActionId++;
    }
}
