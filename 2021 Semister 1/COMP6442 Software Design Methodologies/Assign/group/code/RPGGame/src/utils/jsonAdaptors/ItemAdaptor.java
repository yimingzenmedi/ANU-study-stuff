package utils.jsonAdaptors;

import com.google.gson.*;
import enums.FacilityType;
import enums.ItemType;
import models.entities.Action;
import models.entities.Item;
import models.entities.facilities.Box;
import models.entities.facilities.NormalFacility;
import models.entities.items.Armor;
import models.entities.items.LifePotion;
import models.entities.items.Prop;
import models.entities.items.Weapon;

import java.lang.reflect.Type;

public class ItemAdaptor implements JsonDeserializer<Item> {

    @Override
    public Item deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String objType = jsonObject.get("type").getAsString();
        try {
            String className = "";
            ItemType itemType = ItemType.valueOf(objType);

            if (itemType == ItemType.PROP) {
                className = Prop.class.getName();
            } else if (itemType == ItemType.ARMOR) {
                className = Armor.class.getName();
            } else if (itemType == ItemType.WEAPON) {
                className = Weapon.class.getName();
            } else if (itemType == ItemType.LIFE_POTION) {
                className = LifePotion.class.getName();
            }
//            System.out.println(className + ", objType: " + objType);
            return context.deserialize(jsonElement, Class.forName(className));
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException("Unknown element type: " + type, cnfe);
        }
    }
}
