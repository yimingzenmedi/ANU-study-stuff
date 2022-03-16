package controllers;

import enums.ItemType;
import models.State;
import models.entities.items.Armor;
import models.entities.items.LifePotion;
import models.entities.items.Weapon;
import models.entities.Item;
import models.entities.Protagonist;

public class ItemController {
    public static void useItem(int itemId) {
        State state = State.getInstance();
        Item item = state.getItemById(itemId);
        useItem(item);
    }

    public static void useItem(Item item) {
        State state = State.getInstance();
        Protagonist protagonist = state.getProtagonist();
        if (item == null) {
            System.out.println("\nInvalid item.");
            return;
        }
        if (item.getType() == ItemType.ARMOR) {
            Armor armor = (Armor) item;
            Integer currentArmor = protagonist.getArmor();
            if (currentArmor != null) {
                state.getBackpack().putInItem(currentArmor);
            }
            state.getBackpack().takeOutItem(armor.getId());
            protagonist.equipArmor(armor);
            System.out.println("\nEquipped armor: " + armor.getName());

        } else if (item.getType() == ItemType.WEAPON) {
            Weapon weapon = (Weapon) item;
            Integer currentWeapon = protagonist.getWeapon();
            if (currentWeapon != null) {
                state.getBackpack().putInItem(currentWeapon);
            }
            state.getBackpack().takeOutItem(weapon.getId());
            protagonist.equipWeapon(weapon);
            System.out.println("\nEquipped weapon: " + weapon.getName());

        } else if (item.getType() == ItemType.LIFE_POTION) {
            LifePotion lifePotion = (LifePotion) item;
            int hpRecover = lifePotion.getHpRecover();
            protagonist.recoverHp(hpRecover);
            state.getBackpack().takeOutItem(lifePotion.getId());
            System.out.println("\nHp recovered: " + lifePotion.getHpRecover());
            System.out.println("Your hp: " + protagonist.getHp());
        }
    }
}
