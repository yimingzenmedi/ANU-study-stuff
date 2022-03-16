package utils.jsonAdaptors;

import com.google.gson.*;
import enums.TaskType;
import models.entities.Task;
import models.entities.tasks.ItemTask;
import models.entities.tasks.MapTask;
import models.entities.tasks.MonsterTask;
import models.entities.tasks.NPCTask;

import java.lang.reflect.Type;

public class TaskAdaptor implements JsonDeserializer<Task> {

    @Override
    public Task deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String objType = jsonObject.get("taskType").getAsString();

        try {
            String className = "";
            TaskType taskType = TaskType.valueOf(objType);

            if (taskType == TaskType.ITEM) {
                className = ItemTask.class.getName();
            } else if (taskType == TaskType.MONSTER) {
                className = MonsterTask.class.getName();
            } else if (taskType == TaskType.NPC) {
                className = NPCTask.class.getName();
            } else if (taskType == TaskType.MAP) {
                className = MapTask.class.getName();
            }
//            System.out.println(className + ", objType: " + objType);
            return context.deserialize(jsonElement, Class.forName(className));
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException("Unknown element type: " + type, cnfe);
        }
    }
}
