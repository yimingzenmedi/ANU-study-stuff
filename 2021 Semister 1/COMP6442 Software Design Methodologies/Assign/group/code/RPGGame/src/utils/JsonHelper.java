package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.State;
import models.StateBuilder;
import models.entities.*;
import utils.jsonAdaptors.*;

import java.io.*;

public class JsonHelper {
    public static void saveGame(String path) {
        State state = State.getInstance();
        File file;
        String suffix = ".json";

        if (path == null || path.trim().isEmpty()) {
            String basePath = "archives\\record";
            path = basePath + suffix;
            file = new File(path);
            int counter = 1;
            while (file.exists()) {
                path = basePath + counter + suffix;
                file = new File(path);
                counter++;
            }
        } else {
            path = path.trim();
            if (path.endsWith(suffix)) {
                file = new File(path);
            } else {
                file = new File(path + suffix);
            }
        }

        file.delete();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        Gson gson = new Gson();
        String jsonStr = gson.toJson(state);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(jsonStr);
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("File IO exception: " + e);
            e.printStackTrace();
        }
        System.out.println("\nSave complete to: " + file.getAbsolutePath() + ".\n");
    }

    public static boolean loadGame(String path) {
        File file;
        String suffix = ".json";
        String basePath = "archives\\record";

        if (path == null || path.isEmpty()) {
            path = basePath + suffix;
            file = new File(path);
            int counter = 1;
            while (!file.exists()) {
                path = basePath + counter + suffix;
                file = new File(path);
                counter++;
            }
        } else {
            path = path.trim();
            if (path.endsWith(suffix)) {
                file = new File(path);
            } else {
                file = new File(path + suffix);
            }
        }

        System.out.println("\nReading from: " + file.getAbsolutePath());

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder text = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line);
            }
            reader.close();
            if (!text.toString().isEmpty()) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Item.class, new ItemAdaptor())
                        .registerTypeAdapter(Task.class, new TaskAdaptor())
                        .registerTypeAdapter(Action.class, new ActionAdaptor())
                        .registerTypeAdapter(TaskEffect.class, new TaskEffectAdaptor())
                        .registerTypeAdapter(Facility.class, new FacilityAdaptor())
                        .create();
                StateBuilder stateBuilder = gson.fromJson(text.toString(), StateBuilder.class);
                State.getInstance().setState(stateBuilder);
                return true;
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e);
        } catch (IOException e) {
            System.err.println("File io exception: " + e);
        }

        return false;
    }
}
