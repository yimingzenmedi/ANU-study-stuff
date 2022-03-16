package utils;

public class GlobalNPCCounter {
    private static int currentNPCId = 1;

    public static void updateId(int id) {
        if (id > currentNPCId) {
             currentNPCId = id;
        }
        currentNPCId++;
    }

    public static int getId() {
        return currentNPCId++;
    }
}
