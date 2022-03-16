package utils;

public class GlobalItemCounter {
    private static int currentItemId = 1;

    public static void updateId(int id) {
        if (id > currentItemId) {
            currentItemId = id;
            currentItemId++;
        }
    }

    public static int getId() {
        return currentItemId++;
    }
}
