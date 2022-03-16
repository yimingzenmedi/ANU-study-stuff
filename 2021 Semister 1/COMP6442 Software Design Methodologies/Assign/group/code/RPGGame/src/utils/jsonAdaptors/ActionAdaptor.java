package utils.jsonAdaptors;

import com.google.gson.*;
import enums.ActionType;
import models.entities.Action;
import models.entities.actions.ExpAction;
import models.entities.actions.ItemAction;
import models.entities.actions.NormalAction;
import models.entities.actions.TaskAction;

import java.lang.reflect.Type;

public class ActionAdaptor implements JsonDeserializer<Action> {

    @Override
    public Action deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String objType = jsonObject.get("type").getAsString();

        try {
            String className = "";
            ActionType actionType = ActionType.valueOf(objType);

            if (actionType == ActionType.NORMAL) {
                className = NormalAction.class.getName();
            } else if (actionType == ActionType.EXP) {
                className = ExpAction.class.getName();
            } else if (actionType == ActionType.ITEM) {
                className = ItemAction.class.getName();
            } else if (actionType == ActionType.TASK) {
                className = TaskAction.class.getName();
            }

//            System.out.println(className + ", objType: " + objType);
            return context.deserialize(jsonElement, Class.forName(className));
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException("Unknown element type: " + type, cnfe);
        }
    }
}
