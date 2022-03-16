package utils.jsonAdaptors;

import com.google.gson.*;
import enums.TaskEffectType;
import models.entities.TaskEffect;
import models.entities.taskEffects.*;

import java.lang.reflect.Type;

public class TaskEffectAdaptor implements JsonDeserializer<TaskEffect> {

    @Override
    public TaskEffect deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String objType = jsonObject.get("type").getAsString();

        try {
            String className = "";
            TaskEffectType taskEffectType = TaskEffectType.valueOf(objType);

            if (taskEffectType == TaskEffectType.NORMAL) {
                className = NormalTaskEffect.class.getName();
            } else if (taskEffectType == TaskEffectType.EXP) {
                className = ExpTaskEffect.class.getName();
            } else if (taskEffectType == TaskEffectType.FINISH) {
                className = FinishTaskEffect.class.getName();
            } else if (taskEffectType == TaskEffectType.ITEM) {
                className = ItemTaskEffect.class.getName();
            } else if (taskEffectType == TaskEffectType.MAP) {
                className = MapTaskEffect.class.getName();
            }
//            System.out.println(className + ", objType: " + objType);
            return context.deserialize(jsonElement, Class.forName(className));
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException("Unknown element type: " + type, cnfe);
        }
    }
}
