package utils;

public class GlobalMonsterCounter {
    private static int currentMonsterId = 1;

    public static void updateId(int id) {
        if (id > currentMonsterId) {
             currentMonsterId = id;
        }
        currentMonsterId++;
    }

    public static int getId() {
        return currentMonsterId++;
    }
}
