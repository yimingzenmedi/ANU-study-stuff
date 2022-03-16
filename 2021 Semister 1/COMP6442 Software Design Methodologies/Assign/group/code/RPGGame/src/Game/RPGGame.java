package Game;

import controllers.GameController;
import models.State;
import models.entities.*;
import models.entities.actions.ItemAction;
import models.entities.actions.NormalAction;
import models.entities.actions.TaskAction;
import models.entities.facilities.Box;
import models.entities.items.Armor;
import models.entities.items.LifePotion;
import models.entities.items.Weapon;
import models.entities.taskEffects.FinishTaskEffect;
import models.entities.tasks.NPCTask;
import models.relations.MapBranches;
import models.relations.MapFacilities;
import models.relations.MapMonsters;
import models.relations.MapNPCs;
import utils.AutomaticTester;
import utils.GlobalActionCounter;
import utils.GlobalItemCounter;
import utils.GlobalTaskCounter;
import utils.JsonHelper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.simple.parser.ParseException;

public class RPGGame {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {

        System.out.println("\n**** Welcome! ****\n");
//        System.out.println("\nNOTICE: Enter the INDEX when select!!!\n");
        start(null);
    }

    private static void start(String cmd) throws FileNotFoundException, IOException, ParseException {
        while (true) {
            System.out.println("\nSelect to start:");
            System.out.println("(NOTICE: Enter the INDEX when select.)\n");
            System.out.println("1. Start demo game");
            System.out.println("2. Load game");
            System.out.println("3. Exit");
            System.out.println("4. Test the game");
            System.out.println("");
            String gameOption = cmd == null || cmd.isEmpty() ? sc.nextLine() : cmd;

            switch (gameOption) {
                case "1":
                    loadDemoGame();
                    GameController.mainLoop(null);
                    break;
                case "2":
                    System.out.println("Enter the record path:");
                    String path = sc.nextLine();
                    if (JsonHelper.loadGame(path)) {
                        GameController.mainLoop(null);
                    }
                    break;
                case "3":
                    System.out.println("Exit game...");
                    System.exit(0);
                    break;
                case "4":
                    AutomaticTester.test();
                    break;    
                default:
                    System.out.println("\nInvalid command!");
                    break;
            }
        }
    }

    // =============================================================================
    // demo game:
    public static void loadDemoGame() {
        State state = State.getInstance();

        // Protagonist:
        Protagonist protagonist =
                Protagonist.getInstance("Warrior", 1, 0, 10, 10, 10, 5, 5, 0, 0, null, null);
        state.setProtagonist(protagonist);

        // maps:
        MapCell palace = new MapCell("Palace", true);
        MapCell forest = new MapCell("Forest", false);
        MapCell valley = new MapCell("Valley", false);
        MapCell cave = new MapCell("Cave", true);
        MapCell dragonNest = new MapCell("Dragon Nest", true);
        MapCell cage = new MapCell("Cage", false);
        state.addMapCell(palace).addMapCell(forest).addMapCell(valley).addMapCell(cave)
                .addMapCell(dragonNest).addMapCell(cage);

        // items:
        Item sword = new Weapon(GlobalItemCounter.getId(), "Ancient Sword", 3);
        Item armor = new Armor(GlobalItemCounter.getId(), "Guard's Armor", 5);
        Item lifePotion = new LifePotion(GlobalItemCounter.getId(), "Life Potion", 8);
        state.addItem(sword).addItem(armor).addItem(lifePotion);

        // Fixed Items:
        ArrayList<Integer> caveBoxItems = new ArrayList<>();
        caveBoxItems.add(sword.getId());
        Box caveBox = new Box("Old Treasure Chest", caveBoxItems, "You opened the treasure chest.");
        state.addFacility(caveBox);

        // Map Facility relation:
        ArrayList<Integer> caveFacilityIds = new ArrayList<>();
        caveFacilityIds.add(caveBox.getId());
        MapFacilities caveFacilities = new MapFacilities(cave.getId(), caveFacilityIds);
        state.addMapFacilitiesInfo(caveFacilities);

        // map branches:
        ArrayList<Integer> forestBranchIds = new ArrayList<>();
        forestBranchIds.add(palace.getId());
        forestBranchIds.add(valley.getId());
        MapBranches forestBranches = new MapBranches(forest.getId(), forestBranchIds);

        ArrayList<Integer> palaceBranchIds = new ArrayList<>();
        palaceBranchIds.add(forest.getId());
        MapBranches palaceBranches = new MapBranches(palace.getId(), palaceBranchIds);

        ArrayList<Integer> valleyBranchIds = new ArrayList<>();
        valleyBranchIds.add(forest.getId());
        valleyBranchIds.add(cave.getId());
        valleyBranchIds.add(dragonNest.getId());
        MapBranches valleyBranches = new MapBranches(valley.getId(), valleyBranchIds);

        ArrayList<Integer> dragonNestBranchIds = new ArrayList<>();
        dragonNestBranchIds.add(valley.getId());
        dragonNestBranchIds.add(cage.getId());
        MapBranches dragonNestBranches = new MapBranches(dragonNest.getId(), dragonNestBranchIds);

        ArrayList<Integer> caveBranchIds = new ArrayList<>();
        caveBranchIds.add(valley.getId());
        MapBranches caveBranches = new MapBranches(cave.getId(), caveBranchIds);

        ArrayList<Integer> cageBranchIds = new ArrayList<>();
        cageBranchIds.add(dragonNest.getId());
        MapBranches cageBranches = new MapBranches(cage.getId(), cageBranchIds);

        state.addMapBranchesInfo(forestBranches).addMapBranchesInfo(palaceBranches)
                .addMapBranchesInfo(valleyBranches).addMapBranchesInfo(dragonNestBranches)
                .addMapBranchesInfo(caveBranches).addMapBranchesInfo(cageBranches);


        // NPCs:
        NPC princess = new NPC("Princess");
        NPC king = new NPC("King");
        NPC elf = new NPC("Forest Elf");

        state.addNPC(princess).addNPC(king).addNPC(elf);

        // Tasks and task effects:
        TaskEffect finalTaskEffect = new FinishTaskEffect(
                "You took princess back to the country, married the princess and became the new king of the country.");
        ArrayList<TaskEffect> finalTaskEffects = new ArrayList<>();
        finalTaskEffects.add(finalTaskEffect);
        Task finalTask = new NPCTask(GlobalTaskCounter.getId(), "Save the princess",
                "Save the princess from the dragon", finalTaskEffects, princess.getId());

        ArrayList<TaskEffect> initialTaskEffects = new ArrayList<>();
        // TaskEffect initialTaskUnlockForest = new MapTaskEffect(forest.getId());
        // initialTaskEffects.add(initialTaskUnlockForest);
        Task initialTask = new NPCTask(GlobalTaskCounter.getId(), "Talk to the king",
                "Talk to the king in the palace", initialTaskEffects, king.getId());

        state.addTask(initialTask).addTask(finalTask);

        // actions:
        Action talkToPrincess = new NormalAction(GlobalActionCounter.getId(), "Talk to princess", true,
                "It's safe now, my princess.", "Thank you for saving me, my hero!");
        princess.addAction(talkToPrincess);

        ArrayList<Integer> kingActionMapsUnlock = new ArrayList<>();
        kingActionMapsUnlock.add(forest.getId());
        Action kingAction = new TaskAction(GlobalActionCounter.getId(), "Accept task", true,
                finalTask.getId(),
                "Yes, my lord. It is my pleasure to accept your request. I will definitely save princess from despair.",
                "Thank you, warrior. I promise, if you can save the princess, I will formally marry my daughter to you and you will become the next king.",
                kingActionMapsUnlock);
        king.addAction(kingAction);

        Action getPotion = new ItemAction(GlobalActionCounter.getId(), "Talk to the elf", true,
                lifePotion.getId(), "Hi, are you an elf?",
                "Hi my warrior, yes it's me. It is dangerous in the forest, this potion may be able to help you.");
        elf.addAction(getPotion);

        // current tasks:
        state.assignNewTask(initialTask.getId());


        // Monsters:
        ArrayList<Integer> valleyGuardRewardItemIds = new ArrayList<>();
        valleyGuardRewardItemIds.add(armor.getId());
        ArrayList<Integer> valleyGuardMapsUnlock = new ArrayList<>();
        valleyGuardMapsUnlock.add(valley.getId());
        Monster valleyGuard =
                new Monster("Valley Guard", 5, 3, 6, 10, valleyGuardRewardItemIds, valleyGuardMapsUnlock);

        ArrayList<Integer> dragonRewardItemIds = new ArrayList<>();
        ArrayList<Integer> dragonMapsUnlock = new ArrayList<>();
        dragonMapsUnlock.add(cage.getId());
        Monster dragon = new Monster("Dragon", 10, 8, 18, 45, dragonRewardItemIds, dragonMapsUnlock);

        state.addMonster(valleyGuard).addMonster(dragon);



        // map monster relations:
        ArrayList<Integer> forestMonsterIds = new ArrayList<>();
        forestMonsterIds.add(valleyGuard.getId());
        MapMonsters forestMonsters = new MapMonsters(forest.getId(), forestMonsterIds);

        ArrayList<Integer> dragonNestMonsterIds = new ArrayList<>();
        dragonNestMonsterIds.add(dragon.getId());
        MapMonsters dragonNestMonsters = new MapMonsters(dragonNest.getId(), dragonNestMonsterIds);

        state.addMapMonstersInfo(forestMonsters).addMapMonstersInfo(dragonNestMonsters);

        // map npc relations:
        MapNPCs palaceMapNPCs = new MapNPCs(palace.getId());
        palaceMapNPCs.addNPC(king.getId());

        MapNPCs forestMapNPCs = new MapNPCs(forest.getId());
        forestMapNPCs.addNPC(elf.getId());

        MapNPCs cageMapNPCs = new MapNPCs(cage.getId());
        cageMapNPCs.addNPC(princess.getId());

        state.addMapNPCsInfo(palaceMapNPCs).addMapNPCsInfo(forestMapNPCs).addMapNPCsInfo(cageMapNPCs);

        // current position:
        state.setPosition(palace.getId());

        // pre story:
        PreStory preStory_ = new PreStory();
        preStory_
                .addStory("One day, the king summoned the bravest warrior in the country to his palace.");
        preStory_.addStory("The warrior kneel to the king and listen to the king.");
        preStory_.addStory(
                "\"My sweetest little princess has been seized by the dragon, the whole nation is sink into sadness. Would you, "
                        + "\nmy bravest warriour over the world, become the hero to beat the dragon and save my daughter? I promise, \n"
                        + "if you can save the princess, I will formally marry my daughter to you and you will become the next king.\"");
        state.setPreStory(preStory_);
    }

}
