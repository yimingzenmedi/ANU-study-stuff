package controllers;

import enums.TokenType;
import models.State;
import models.entities.*;
import models.parser.Token;
import models.parser.Tokenizer;
import utils.JsonHelper;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

public class GameController {
    private static final Scanner sc = new Scanner(System.in);
    private static final Set<String> CONFIRM_INPUTS = Set.of("1", "yes");

    public static void mainLoop(String input) {
        System.out.println("\n*************** Game Start ***************\n");
        // show prestory:
        State state = State.getInstance();
        // System.out.println(state.toString());
        PreStory preStory = state.getPreStory();
        for (String story : preStory.getStories()) {
            System.out.println(story);
        }

        // main loop:
        boolean continueGame = true;
        while (!state.isFinished() && continueGame) {
            MapCell currentMap = state.getMapCellById(state.getPosition());
            System.out.println("\n--------------------------------------------------");
            System.out.println("You are at: " + currentMap.getName());

            System.out.println("\n*** You want to: ");
            System.out.println("    1. Go to somewhere else.");
            System.out.println("    2. Talk to NPC.");
            System.out.println("    3. Interact with facility.");
            System.out.println("    4. Fight with monster.");
            System.out.println("    5. Use props.");
            System.out.println("    6. Show current tasks.");
            System.out.println("    7. Show backpack.");
            System.out.println("    8. Show my status.");
            System.out.println("    9. Check around.");
            System.out.println("    10. EXIT GAME!");
            System.out.println(">");

            String cmd = input == null || input.isEmpty() ? sc.nextLine() : input;
            continueGame = processCmd(cmd);
        }
    }

    public static boolean processCmd(String cmd) {
        switch (cmd.trim()) {
            case "1":
                changeMap();
                break;
            case "2":
                interactWithNPC();
                break;
            case "3":
                interactWithFacility();
                break;
            case "4":
                fightWithMonster();
                break;
            case "5":
                useProps();
                break;
            case "6":
                showCurrentTasks();
                break;
            case "7":
                showBackpack();
                break;
            case "8":
                showMyStatus();
                break;
            case "9":
                checkAround();
                break;
            case "10":
                exitGame();
                return false;
            default:
                parse(cmd);
                break;
        }
        if (State.getInstance().getProtagonist().isDead()) {
            System.out.println("\nOh no, you are dead! :(");
            System.out.println("\n************* Game over ****************");
            return false;
        }
        return true;
    }


    private static void parse(String cmd) {
//        System.out.println("parse: " + cmd);

        Tokenizer tokenizer = new Tokenizer(cmd);
        State state = State.getInstance();
        while (tokenizer.hasNext()) {
//            System.out.println("Start looping...");
            Token token = tokenizer.current();
            if (token.getType() == TokenType.EXIT) {
                exitGame();
                return;
            }
            if (token.getType() == TokenType.CHECK) {
                checkAround();
            }
            if (token.getType() == TokenType.SHOW_BACKPACK) {
                showBackpack();
            }
            if (token.getType() == TokenType.SHOW_STATUS) {
                showMyStatus();
            }
            if (token.getType() == TokenType.SHOW_TASK) {
                showCurrentTasks();
            }
            if (token.getType() == TokenType.GO) {
                MapCell mapCell = state.getConnectedMapByName(token.getValue());
                if (mapCell == null) {
                    System.out.println("\nInvalid map name: " + token.getValue());
                } else {
                    MapController.changeMap(mapCell);
                }
            }
            if (token.getType() == TokenType.TALK) {
                NPC npc = state.getCurrentNPCsByName(token.getValue());
                if (npc == null) {
                    System.out.println("\nInvalid npc name: " + token.getValue());
                } else {
                    getWhatToDoWithNPC(npc);
                }
            }
            if (token.getType() == TokenType.INTERACT) {
                Facility facility = state.getCurrentFacilitiesByName(token.getValue());
                if (facility == null) {
                    System.out.println("\nInvalid facility name: " + token.getValue());
                } else {
                    FacilityController.interact(facility);
                }

            }
            if (token.getType() == TokenType.FIGHT) {
                Monster monster = state.getCurrentMonstersByName(token.getValue());
                if (monster == null || monster.isDead()) {
                    System.out.println("\nInvalid monster name: " + token.getValue());
                } else {
                    printYouVSMonster(monster);
                    getFightConfirm(monster, null);
                }
            }
            if (token.getType() == TokenType.USE) {
                Item item = state.getItemFromBackpackByName(token.getValue());
                if (item == null) {
                    System.out.println("\nInvalid item name: " + token.getValue());
                } else {
                    ItemController.useItem(item);
                }
            }
            if (token.getType() == TokenType.UNKNOWN) {
                System.out.println("\nInvalid command: " + token.getToken());
            }
            tokenizer.next();
        }
    }

    private static void getWhatToDoWithNPC(NPC npc) {
        ArrayList<Action> actions = npc.getActions();
        System.out.println("\n*** You want to: ");

        int actionCounter = 1;
        for (Action action : actions) {
            System.out.println("\t" + actionCounter + ". " + action.getName());
            actionCounter++;
        }
        System.out.println("\t" + actionCounter + ". CANCEL");
        System.out.println(">");
        String actionInput = sc.nextLine();
        if (isNumeric(actionInput)) {
            int actionIndex = Integer.parseInt(actionInput);
            if (actionIndex > 0 && actionIndex < actionCounter) {
                Action action = actions.get(actionIndex - 1);
                NPCController.interact(npc, action);
                // check if any NPC task finished:
                TaskController.checkNPCTasks(npc.getId());
            } else if (actionIndex == actionCounter) {
                System.out.println("\nCancelled.");
            } else {
                Action targetAction = parseAction(actions, actionInput);
                NPCController.interact(npc, targetAction);
            }
        } else {
            if (actionInput.trim().toLowerCase().equals("cancel")) {
                System.out.println("\nCancelled.");
            } else {
                Action targetAction = parseAction(actions, actionInput);
                if (targetAction == null) {
                    System.out.println("\nInvalid action.");
                } else {
                    NPCController.interact(npc, targetAction);
                }
            }
        }
    }

    private static Action parseAction(ArrayList<Action> actions, String cmd) {
        cmd = cmd.trim().toLowerCase();
        for (Action action : actions) {
            if (action.getName().trim().toLowerCase().equals(cmd)) {
                return action;
            }
        }
        return null;
    }

    private static Monster parseMonster(ArrayList<Monster> monsters, String cmd) {
        cmd = cmd.trim().toLowerCase();
        for (Monster monster : monsters) {
            if (monster.getName().trim().toLowerCase().equals(cmd)) {
                return monster;
            }
        }
        return null;
    }

    private static NPC parseNPC(ArrayList<NPC> npcs, String cmd) {
        cmd = cmd.trim().toLowerCase();
        for (NPC npc : npcs) {
            if (npc.getName().trim().toLowerCase().equals(cmd)) {
                return npc;
            }
        }
        return null;
    }

    private static Item parseItem(ArrayList<Item> items, String cmd) {
        cmd = cmd.trim().toLowerCase();
        for (Item item : items) {
            if (item.getName().trim().toLowerCase().equals(cmd)) {
                return item;
            }
        }
        return null;
    }

    private static Facility parseFacility(ArrayList<Facility> facilities, String cmd) {
        cmd = cmd.trim().toLowerCase();
        for (Facility facility : facilities) {
            if (facility.getName().trim().toLowerCase().equals(cmd)) {
                return facility;
            }
        }
        return null;
    }

    private static MapCell parseMap(ArrayList<MapCell> maps, String cmd) {
        cmd = cmd.trim().toLowerCase();
        for (MapCell mapCell : maps) {
            if (mapCell.getName().trim().toLowerCase().equals(cmd)) {
                return mapCell;
            }
        }
        return null;
    }

    private static void changeMap() {
        changeMap(null);
    }

    private static void changeMap(String cmd) {
        State state = State.getInstance();
        ArrayList<Integer> branchKeys = state.getBranchKeysByMapKey(state.getPosition());
        while (true) {
            System.out.println("\n*** Where to go?");
            ArrayList<MapCell> maps = new ArrayList<>();
            int counter = 1;
            if (branchKeys != null) {
                for (Integer branchKey : branchKeys) {
                    MapCell mapCell = state.getMapCellById(branchKey);
                    System.out.println("\t" + counter + ". " + mapCell.getName());
                    maps.add(mapCell);
                    counter++;
                }
            }
            System.out.println("\t" + counter + ". CANCEL");
            System.out.println(">");
            String input = cmd == null || cmd.isEmpty() ? sc.nextLine() : cmd;
            if (isNumeric(input)) {
                int index = Integer.parseInt(input);
                if (index > 0 && index < counter) {
                    MapController.changeMap(branchKeys.get(index - 1));
                    break;
                } else if (index == counter) {
                    System.out.println("\nCancelled.");
                    break;
                } else {
                    MapCell mapCell = parseMap(maps, input);
                    MapController.changeMap(mapCell);
                    break;
                }
            } else {
                if (input.toLowerCase().trim().equals("cancel")) {
                    System.out.println("\nCancelled.");
                } else {
                    MapCell mapCell = parseMap(maps, input);
                    MapController.changeMap(mapCell);
                    break;
                }
            }
        }
    }

    private static void interactWithNPC() {
        interactWithNPC(null, null);
    }

    private static void interactWithNPC(String whomToTalk, String youWantTo) {
        State state = State.getInstance();
        ArrayList<NPC> npcs = state.getNPCsByMapKey(state.getPosition());

        while (!state.isFinished()) {
            System.out.println("\n*** Whom to talk to?");
            int counter = 1;
            for (NPC npc : npcs) {
                System.out.println("\t" + counter + ". " + npc.getName());
                counter++;
            }
            System.out.println("\t" + counter + ". CANCEL");
            System.out.println(">");
            String input = whomToTalk == null || whomToTalk.isEmpty() ? sc.nextLine() : whomToTalk;
            if (isNumeric(input)) {
                int index = isNumeric(input) ? Integer.parseInt(input) : -1;
                if (index > 0 && index < counter) {
                    NPC npc = npcs.get(index - 1);
                    getWhatToDoWithNPC(npc);
                } else if (index == counter) {
                    System.out.println("\nCancelled.");
                    break;
                } else {
                    NPC npc = parseNPC(npcs, input);
                    if (npc == null) {
                        System.out.println("\nInvalid input!");
                    }
                }
            } else {
                if (input.toLowerCase().trim().equals("cancel")) {
                    System.out.println("\nCancelled.");
                } else {
                    NPC npc = parseNPC(npcs, input);
                    if (npc == null) {
                        System.out.println("\nInvalid input!");
                    } else {
                        getWhatToDoWithNPC(npc);
                    }
                }
            }
        }
    }

    private static void fightWithMonster() {
        fightWithMonster(null, null);
    }

    private static void fightWithMonster(String cmd, String confirm) {
        State state = State.getInstance();
        ArrayList<Monster> monsters = state.getMonstersByMapKey(state.getPosition());

        while (!state.isFinished()) {
            System.out.println("\n*** Whom to fight with?");
            int counter = 1;
            if (monsters != null) {
                for (Monster monster : monsters) {
                    if (!monster.isDead()) {
                        System.out.println("\t" + counter + ". " + monster.getName());
                        counter++;
                    }
                }
            }
            System.out.println("\t" + counter + ". CANCEL");
            System.out.println(">");
            String input = cmd == null || cmd.isEmpty() ? sc.nextLine() : cmd;
            if (isNumeric(input)) {
                int index = Integer.parseInt(input);
                if (index > 0 && index < counter) {
                    Monster monster = monsters.get(index - 1);
                    printYouVSMonster(monster);
                    if (getFightConfirm(monster, confirm) != -1) {
                        break;
                    }
                    // MapController.changeMap(branchKeys.get(index - 1));
                } else if (index == counter) {
                    System.out.println("\nCancelled.");
                    break;
                } else {
//                    System.out.println("\nInvalid index!");
                    if (monsters == null) {
                        System.out.println("\nInvalid input!");
                    } else {
                        Monster monster = parseMonster(monsters, input);
                        if (monster != null) {
                            printYouVSMonster(monster);
                            if (getFightConfirm(monster, confirm) != -1) {
                                break;
                            }
                        } else {
                            System.out.println("\nInvalid input!");
                        }
                    }
                }
            } else {
                if (input.toLowerCase().trim().equals("cancel")) {
                    System.out.println("\nCancelled.");
                    break;
                } else if (monsters == null) {
                    System.out.println("\nInvalid input!");
                } else {
                    Monster monster = parseMonster(monsters, input);
                    if (monster != null) {
                        printYouVSMonster(monster);
                        if (getFightConfirm(monster, confirm) != -1) {
                            break;
                        }
                    } else {
                        System.out.println("\nInvalid input!");
                    }
                }

            }
        }

    }

    private static void printYouVSMonster(Monster monster) {
        Protagonist protagonist = State.getInstance().getProtagonist();
        System.out.println("\nYou vs " + monster.getName() + ": ==========================");
        System.out.println("\t*** You ***");
        System.out.println("\tHp: " + protagonist.getHp());
        System.out.println("\tAttack: " + protagonist.getAttack());
        System.out.println("\tDefence: " + protagonist.getDefence());
        System.out.println("\n\t*** Monster: " + monster.getName() + " ***");
        System.out.println("\tHp: " + monster.getHp());
        System.out.println("\tAttack: " + monster.getAttack());
        System.out.println("\tDefence: " + monster.getDefence());
        System.out.println("\n========================================");
    }

    private static int getFightConfirm(Monster monster, String confirm) {
        System.out.println("\nAre you sure to fight with " + monster.getName() + "?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        String confirmInput = confirm == null || confirm.isEmpty() ? sc.nextLine() : confirm;
        confirmInput = confirmInput.trim().toLowerCase();
        if (CONFIRM_INPUTS.contains(confirmInput)) {
            MonsterController.fight(monster);
//            break;
            return 0;
        } else if (confirmInput.equals("2") || confirmInput.equals("no")) {
            System.out.println("\nYou gave up fight with " + monster.getName());
            return 1;
        } else {
            System.out.println("\nInvalid input!");
            return -1;
        }
    }

    private static void useProps() {
        useProps(null);
    }

    private static void useProps(String cmd) {
        State state = State.getInstance();
        ArrayList<Integer> itemIds = state.getBackpack().getItems();

        while (!state.isFinished()) {
            System.out.println("\n*** Which one to use?");
            int counter = 1;
            ArrayList<Item> items = new ArrayList<>();
            if (itemIds != null) {
                for (Integer itemId : itemIds) {
                    Item item = state.getItemById(itemId);
                    items.add(item);
                    System.out.println("\t" + counter++ + ". " + item.getName());
                }
            }
            System.out.println("\t" + counter + ". CANCEL");
            System.out.println(">");
            String input = cmd == null || cmd.isEmpty() ? sc.nextLine() : cmd;
            if (isNumeric(input)) {
                int index = Integer.parseInt(input);
                if (index > 0 && index < counter) {
                    ItemController.useItem(itemIds.get(index - 1));
                    break;
                } else if (index == counter) {
                    System.out.println("\nCancelled.");
                    break;
                } else {
                    Item item = parseItem(items, input);
                    ItemController.useItem(item);
                    break;
//                    System.out.println("\nInvalid input!");
                }
            } else {
                if (input.toLowerCase().trim().equals("cancel")) {
                    System.out.println("\nCancelled.");
                    break;
                } else {
                    Item item = parseItem(items, input);
                    ItemController.useItem(item);
                    break;
                }
            }
        }

    }

    private static void interactWithFacility() {
        interactWithFacility(null);
    }

    private static void interactWithFacility(String cmd) {
        State state = State.getInstance();
        ArrayList<Integer> facilityIDs = state.getFacilityIDsByMapKey(state.getPosition());

        while (!state.isFinished()) {
            System.out.println("\n*** Which one to interact?");
            int counter = 1;
            ArrayList<Facility> facilities = new ArrayList<>();
            if (facilityIDs != null) {
                for (Integer facilityId : facilityIDs) {
                    Facility facility = state.getFacilityById(facilityId);
                    System.out.println("\t" + counter++ + ". " + facility.getName());
                    facilities.add(facility);
                }
            }
            System.out.println("\t" + counter + ". CANCEL");
            System.out.println(">");
            String input = cmd == null || cmd.isEmpty() ? sc.nextLine() : cmd;
            if (isNumeric(input)) {
                int index = Integer.parseInt(input);
                if (index > 0 && index < counter) {
                    FacilityController.interact(facilityIDs.get(index - 1));
                    break;
                } else if (index == counter) {
                    System.out.println("\nCancelled.");
                    break;
                } else {
                    Facility facility = parseFacility(facilities, input);
                    FacilityController.interact(facility);
                    break;
                }
            } else {
                if (input.toLowerCase().trim().equals("cancel")) {
                    System.out.println("\nCancelled.");
                    break;
                } else {
                    Facility facility = parseFacility(facilities, input);
                    FacilityController.interact(facility);
                    break;
                }
            }
        }
    }

    private static void showCurrentTasks() {
        State state = State.getInstance();
        ArrayList<Integer> currentTasks = state.getCurrentTasks();
        System.out.println("\nCurrent tasks: ======================\n");
        if (currentTasks.size() == 0) {
            System.out.println("No task...\n");
            System.out.println("=====================================");
            return;
        }
        int counter = 1;
        for (Task task : state.getTasks()) {
            if (currentTasks.contains(task.getId())) {
                System.out.println(counter++ + ". " + task.getName() + ": \n\t" + task.getDetails() + "\n");
            }
        }
        System.out.println("=====================================");
    }

    private static void showBackpack() {
        State state = State.getInstance();
        ArrayList<Integer> backpack = state.getBackpack().getItems();
        System.out.println("\nBackpack: ======================\n");
        if (backpack.size() == 0) {
            System.out.println("Nothing...\n");
            System.out.println("=====================================");
            return;
        }

        int counter = 1;
        for (Item item : state.getItems()) {
            if (backpack.contains(item.getId())) {
                System.out.println(counter++ + ". " + item.getName() + "\n");
            }
        }
        System.out.println("=====================================");
    }

    private static void showMyStatus() {
        State state = State.getInstance();
        Protagonist protagonist = state.getProtagonist();
        System.out.println("\nCurrent status: ========================\n");
        System.out.println("Level: " + protagonist.getLevel());
        System.out.println("Exp: " + protagonist.getExp() + "/" + protagonist.getExpThreshold());
        System.out.println("Hp: " + protagonist.getHp() + "/" + protagonist.getMaxHp());
        System.out.println("Attack: " + protagonist.getAttack());
        System.out.println("Defence: " + protagonist.getDefence());
        System.out.println("\n=====================================");
    }


    private static void checkAround() {
        State state = State.getInstance();

        System.out.println("\nCheck around: ========================\n");
        System.out.println("You are now at: " + state.getMapCellById(state.getPosition()).getName());
        // branches:
        System.out.print("Adjacent map: ");
        ArrayList<Integer> branchIds = state.getMapBranchIdsById(state.getPosition());
        if (branchIds == null || branchIds.size() == 0) {
            System.out.print("nothing...\n");
        } else {
            int counter = 1;
            for (Integer branchId : branchIds) {
                MapCell mapCell = state.getMapCellById(branchId);
                System.out.print(counter + ". " + mapCell.getName());
                if (counter < branchIds.size()) {
                    System.out.print(", ");
                }
                counter++;
            }
            if (counter == 1) {
                System.out.print("nothing...");
            }
            System.out.print("\n");
        }

        // facilities:
        System.out.print("Facilities: ");
        ArrayList<Integer> facilityIds = state.getFacilityIDsByMapKey(state.getPosition());
        if (facilityIds == null || facilityIds.size() == 0) {
            System.out.print("nothing...\n");
        } else {
            int counter = 1;
            for (Integer facilityId : facilityIds) {
                Facility facility = state.getFacilityById(facilityId);
                System.out.print(counter + ". " + facility.getName());
                if (counter < facilityIds.size()) {
                    System.out.print(", ");
                }
                counter++;
            }
            if (counter == 1) {
                System.out.print("nothing...");
            }
            System.out.print("\n");
        }

        // monsters:
        System.out.print("Monsters: ");
        ArrayList<Monster> monsters = state.getMonstersByMapKey(state.getPosition());
        if (monsters == null || monsters.size() == 0) {
            System.out.print("nothing...\n");
        } else {
            int counter = 1;
            for (Monster monster : monsters) {
                if (!monster.isDead()) {
                    System.out.print(counter + ". " + monster.getName());
                    if (counter < monsters.size()) {
                        System.out.print(", ");
                    }
                    counter++;
                }
            }
            if (counter == 1) {
                System.out.print("nothing...");
            }
            System.out.print("\n");
        }

        // NPCs:
        System.out.print("NPCs: ");
        ArrayList<Integer> npcIds = state.getNPCKeysByMapKey(state.getPosition());
        if (npcIds == null || npcIds.size() == 0) {
            System.out.print("nothing...\n");
        } else {
            int counter = 1;
            for (Integer npcId : npcIds) {
                NPC npc = state.getNPCById(npcId);
                System.out.print(counter + ". " + npc.getName());
                if (counter < npcIds.size()) {
                    System.out.print(", ");
                }
                counter++;
            }
            if (counter == 1) {
                System.out.print("nothing...");
            }
            System.out.print("\n");
        }

        System.out.println("\n=====================================");
    }



    private static void exitGame() {
        while (true) {
            System.out.println("\nDo you want to save game? \n");
            System.out.println("1. Yes");
            System.out.println("2. No");
            String selection = sc.nextLine().trim();

            if (selection.equals("1") || selection.toLowerCase().equals("yes")) {
                System.out.println("\nPlease enter the record name:");
                String path = sc.nextLine();
                JsonHelper.saveGame(path);
                System.exit(0);
            } else if (selection.equals("2") || selection.toLowerCase().equals("no")) {
                System.out.println("\nExit game...");
                System.exit(0);
            } else {
                System.out.println("\nInvalid command.");
            }
        }
    }

    private static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (int i = str.length(); --i >= 0;) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
