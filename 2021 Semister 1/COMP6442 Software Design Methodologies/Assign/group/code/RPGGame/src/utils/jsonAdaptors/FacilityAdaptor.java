package utils.jsonAdaptors;

import com.google.gson.*;
import enums.FacilityType;
import models.entities.Facility;
import models.entities.facilities.Box;
import models.entities.facilities.NormalFacility;

import java.lang.reflect.Type;

public class FacilityAdaptor implements JsonDeserializer<Facility> {

    @Override
    public Facility deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String objType = jsonObject.get("type").getAsString();

        try {
            String className = "";
            FacilityType facilityType = FacilityType.valueOf(objType);

            if (facilityType == FacilityType.NORMAL) {
                className = NormalFacility.class.getName();
            } else if (facilityType == FacilityType.BOX) {
                className = Box.class.getName();
            }
//            System.out.println(className + ", objType: " + objType);
            return context.deserialize(jsonElement, Class.forName(className));
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException("Unknown element type: " + type, cnfe);
        }
    }
}
