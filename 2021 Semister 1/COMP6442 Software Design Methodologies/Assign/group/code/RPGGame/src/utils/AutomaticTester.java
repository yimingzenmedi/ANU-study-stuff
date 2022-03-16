package utils;

import models.State;
import models.entities.Item;
import models.entities.Monster;
import models.entities.Protagonist;
import models.entities.items.Armor;
import models.entities.items.LifePotion;
import models.entities.items.Weapon;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.*;

import controllers.GameController;
import controllers.MonsterController;



public class AutomaticTester {
	//There are three main functionalities:
	//1. test whether the configuration file is complete;
	//2. test whether the configuration setting is playable;
	//3. test whether the configuration setting is enjoyable
	
	//Need a scanner for user input
	private static final Scanner sc = new Scanner(System.in);
	
	//First, gather some constant features of our game setting
	final static String[] PROTAGONISTFEATURE= {"name", "level", "exp", "baseExp", "expThresholdFactor",
			   "hp", "baseHp", "hpThresholdFactor", "basicAttack", "basicDefence",
			   "attackBoost", "defenceBoost", "expThreshold", "maxHp"};
	
	final static int totalMapNum = 6;
	final static int totalNPCNum = 3;
	final static int totalMonsterNum = 2;
	final static int totalItemNum = 3;
	final static int totalTaskNum = 2;

	//Testing the three points one by one, through corresponding method
	public static void test() throws FileNotFoundException, IOException, ParseException {
		//manually input your configuration file path here
		System.out.println("Please input your configuration file name here: (should be xxx.json)");
		String fileName = sc.nextLine();
		Path currentRelativePath = Paths.get("");
		String path = currentRelativePath.toAbsolutePath().toString() + "\\" + fileName;
		
		//create a json object of the configuration file
		Object obj = new JSONParser().parse(new FileReader(path));
		JSONObject json = (JSONObject) obj;
		
		
		if (JsonHelper.loadGame(path)) {
			State state = State.getInstance();
			
            testCompleteness(json);  //first point, whether configuration file contains all specifications needed
            testEnjoyable(state);         //second point, whether the player can enjoy the game
            testPlayable(state);
            
            System.exit(0); //job's done, finished
        }
	}
	
	
	public static void testCompleteness(JSONObject json) {
		System.out.println("==============Start checking completeness ===================\n");
		//make objects that have more that one pair of data into hashmap
		HashMap<?, ?> protagonist = (HashMap<?, ?>) json.get("protagonist");
		
		//check whether the important and necessary features are complete
		checkObject(PROTAGONISTFEATURE, protagonist, "protagonist"); 
		checkProperty(json, "position");
		checkProperty(json, "preStory");
		checkProperty(json, "backpack");
		checkProperty(json, "maps");
		checkProperty(json, "npcs");
		checkProperty(json, "monsters");
		checkProperty(json, "items");
		checkProperty(json, "facilities");
		checkProperty(json, "tasks");
		System.out.println();
		
		//Further check on more detailed things
		System.out.println("For more detailed checking below:");
		
		//Map
		if (checkProperty(json, "maps")) {
			checkDetails(json, "maps", totalMapNum);
			System.out.println();
		}
		
		//NPC
		if (checkProperty(json, "npcs")) {
			checkDetails(json, "npcs", totalNPCNum);
			System.out.println();
		}
		
		//Monsters
		if (checkProperty(json, "monsters")) {
			checkDetails(json, "monsters", totalMonsterNum);
			System.out.println();
		}
		
		//Items
		if (checkProperty(json, "items")) {
			checkDetails(json, "items", totalItemNum);
			System.out.println();
		}
		
		//Tasks
		if (checkProperty(json, "tasks")) {
			checkDetails(json, "tasks", totalTaskNum);
			System.out.println("==============The end of Completeness check===================\n");
		}
	}
	
	public static void testEnjoyable(State state) {
		System.out.println("==============Start checking enjoyability ====================");
		System.out.println("Warning: if you didn't pass completeness check, the following might not be accurate.\n");

		//Everything that related to difficulty and hence enjoybility of the game
		Protagonist protagonist = state.getProtagonist();
		Monster gurad = getMonster(state, 1);
		Monster dragon = getMonster(state, 3);
		Weapon sword = (Weapon) getItem(state, 1);
		Armor armor = (Armor) getItem(state, 2);
		Item potion = getItem(state, 3);
		
		//If nothing is deleted, then check for the numbers!
		if (gurad != null && dragon != null && sword != null && armor != null && potion != null) {
			System.out.println("Following, the level of difficulty will be consider, please combine all comments to make decision!");
			checkDifficulty(protagonist, gurad, dragon, sword, armor, potion);
		}
		System.out.println("==============The end of enjoyability check===================\n");
	}
	
	public static void testPlayable(State state) {
		System.out.println("==============Start checking playability ====================");
		//User to choose whether to test playability
		System.out.println("Are you ready to run the game with your configuration? (yes/no)");
		String answer = sc.nextLine();
		while (!answer.equals("yes") && !answer.equals("no")) {
			System.out.println("Sorry what did you say? I didn't hear you clearly.");
			answer = sc.nextLine();
		}
		
		//User to choose whether to play it or get a summary based on user's setting
		if (answer.equals("no")) {
			System.out.println("Ok, bye bye then.");
			System.exit(0);
		} else {
			System.out.println("Good! Then do you want the summary of your setting's performance?)"
					+ "Or do you want to play? (summary/play)");
			answer = sc.nextLine();
			while (!answer.equals("summary") && !answer.equals("play")) {
				System.out.println("Sorry what did you say? I didn't hear you clearly.");
				answer = sc.nextLine();
			}
			if (answer.equals("play")) {
				System.out.println("Here you go! Enjoy!");
				GameController.mainLoop(null);
			} else {
				summary(state);
			}
		}
		System.out.print("==============The end of playability check===================");
	}
	
	
	
//===================================Helper function for check completeness============================================//
	
	public static boolean checkObject(String[] features, HashMap<?, ?> object, String name) {
		boolean complete = true;
		for (String s: features) {
			if (!object.containsKey(s)) {
				complete = false;
				System.out.println("Oops, it seems your " + name + " is missing " + s + "!");
			}
		}
		
		if (complete) {
			System.out.println(name + " is checked. You got it!");
		}
		return complete;
		
	}
	
	
	public static boolean checkProperty(JSONObject json, String name) {
		boolean complete = true;
		if (json.containsKey(name)) {
			System.out.println(name + " is checked. You got it!");
		} else {
			complete = false;
			System.out.println("Oops, it seems your " + name + " is missing!");
		}
		
		return complete;
	}
	
	public static void checkDetails(JSONObject json, String name, int totalNum) {
		JSONArray array = (JSONArray) json.get(name);
		if (array.size() == 0) {
			System.out.println("However, there are no " + name + " in this game, are you sure you want to continue?");
		} else if (array.size() > totalNum) {
			System.out.println("However, it seems you add some extra " + name + " into the game, we are not sure what's going to happened!");
		} else if (array.size() < totalNum) {
			System.out.println("However, it seems there are some important " + name + " that not presented, we are not sure what's going to happened!");
		} else if (array.size() == totalNum) {
			//Using the fact that hash set does not allow duplicate to check for duplication
			HashSet<Long> objectId = new HashSet<Long>();
			for (int i = 0; i < totalNum; i ++) {
				JSONObject jsonObject = (JSONObject) array.get(i);
				if (objectId.add((Long) jsonObject.get("id")) == false) {
					System.out.println("However, you might have duplicate " + name + ", please check again!");
				}
			}
		}
	}
	
	//===================================Helper function for check enjoybility============================================//

	public static Monster getMonster(State state, int id) {
		Monster monster = null;
		try {
			monster = state.getMonsterById(id);
			monster.getId();
		} catch (NullPointerException e) {
			System.out.println("It seems you delete some monsters, the game will be easier for you!");
			System.out.println("Warning: if you delete dragon, the game might be crashed!");
		}
		return monster;
	}
	
	public static Item getItem(State state, int id) {
		Item item = null;
		try {
			item = state.getItemById(id);
			item.getId();
		} catch (NullPointerException e) {
			System.out.println("It seems you delete some items, the game will be harder for you!");
		}
		return item;
	}
	
	//declaration is too long, using initial of each variable
	public static void checkDifficulty(Protagonist pro, Monster g, Monster d, Weapon s, Armor a, Item p) {
		int att = pro.getAttack();
		int def = pro.getDefence();
		int hp = pro.getHp();
		//Just list a few to demonstrate, can not have all possibilities, just too many of them
		if (MonsterController.calculateHP(att, g.getDefence()) >= g.getHp()) {
			System.out.println("Game difficulty: Easy");
			System.out.println("Reason: you can kill the first monster in one hit!\n");
		} 
		if (MonsterController.calculateHP(att, d.getDefence()) >= d.getHp()) {
			System.out.println("Game difficulty: Easy");
			System.out.println("Reason: the dragon is nothing to you! One shot one kill!\n");
		} 
		if (att == 5 && MonsterController.calculateHP(att + s.getAttackBoost(), d.getDefence()) >= g.getHp()) {
			System.out.println("Game difficulty: Easy");
			System.out.println("Reason: you make the sword too strong!\n");
		} 
		if (def > 30) {
			System.out.println("Game difficulty: Easy");
			System.out.println("Reason: you are too strong in defence!\n");
		} 
		if (MonsterController.calculateHP(g.getAttack(), def) >= hp) {
			System.out.println("Game difficulty: Impossible");
			System.out.println("Reason: you will die in one hit from first monster!\n");
		} 
		if (hp < 5) {
			System.out.println("Game difficulty: Hard");
			System.out.println("Do you know how much hp you start with?\n");
		} 
		if (att == 5 && def == 5 && hp == 10) {
			System.out.println("Game difficulty: Normal");
			System.out.println("Normal start up\n");
		} else {
			System.out.println("Game difficulty: Unknown");
			System.out.println("Hey, I'm sure you changed something, is it good? is it bad? Play the game to see!");
		}
		

	}
	
	
	//===================================Helper function for check playability============================================//

	public static void summary(State state) {
		System.out.println("Assume you have all the maps ready, and you start at the palace. Here's what's going to happen:");
		//check validity of items first
		boolean hasPotion = false, hasArmor = false, hasSword = false;
		boolean hasGuard = false, hasDragon = false;
		int counter = 0;
		int stepFightGuard, stepFightDragon;
		Weapon sword = null;
		Armor armor = null;
		LifePotion potion = null;
		Monster guard = null, dragon = null;
		ArrayList<Item> items = state.getItems();
		ArrayList<Monster> monsters = state.getMonsters();
		for (Item item: items) {
			if (item.getName().equals("Ancient Sword")) {
				hasSword = true;
				sword = (Weapon) item;
			} else if (item.getName().equals("Guard's Armor")) {
				hasArmor = true;
				armor = (Armor) item;
			} else if (item.getName().equals("Life Potion")) {
				hasPotion = true;
				potion = (LifePotion) item;
			}
		}
		
		for (Monster monster: monsters) {
			if (monster.getName().equals("Valley Guard")) {
				hasGuard = true;
				guard = monster;
			} else if (monster.getName().equals("Dragon")) {
				hasDragon = true;
				dragon = monster;
			} 
		}
		
		if (!hasDragon) {
			System.out.println("Hey, where is the dragon? This game has no goal if there's no dragon!");
			System.exit(0);
		}
		
		//Start! First set
		System.out.println("You took 6 steps: accepting task to save the princess and going to forest.");
		counter += 6;
		
		//Second set
		if (hasPotion) {
			counter += 4;
			System.out.println("You took 4 steps: gain potion from the elf in the forest.");
		} else {
			System.out.println("As you delete potion, there will be no potion gained here");
		}
		
		//third set
		if (hasGuard) {
			stepFightGuard = fightMonster(state.getProtagonist(), guard);
			counter += stepFightGuard;
			System.out.println("You took " + stepFightGuard + " steps: you beat The Valley Guard!");
			state.getProtagonist().setLevel(2);
			if (hasArmor) {
				counter += 2;
				System.out.println("You took 2 steps: you wear the armor");
				state.getProtagonist().setDefenceBoost(armor.getDefenceBoost());
			} else {
				System.out.println("As you delete armor, there will be no armor gained here");
			}
		} else {
			System.out.println("As you delete guard, there will be no guard here, hence no armor for you");
		}
		
		//fourth set
		System.out.println("You took 4 steps: keep going and arriving at vally.");
		counter += 4;
		
		//fifth set
		if (hasSword) {
			counter += 2;
			System.out.println("You took 2 steps: you find and wear the sword");
			state.getProtagonist().setAttackBoost(sword.getAttackBoost());
		} else {
			System.out.println("As you delete sword, there will be no sword gained here");
		}
		
		//sixth set
		System.out.println("You took 2 steps: you arrive at dragon nest.");
		counter += 2;
		
		//final set
		if (hasPotion) {
			state.getProtagonist().setMaxHp(state.getProtagonist().getMaxHp() * 2);
			state.getProtagonist().recoverHp(state.getProtagonist().getMaxHp());
		}
		stepFightDragon = fightMonster(state.getProtagonist(), dragon);
		counter += stepFightDragon;
		System.out.println("You took " + stepFightDragon + " steps: you beat The Dragon!");
		System.out.println("Mission accomplished! You took " + counter + " steps in total!");
	}
	
	public static int fightMonster(Protagonist protagonist, Monster monster) {
		int counter = 0;
		int proAttack = protagonist.getAttack();
		int proDefence = protagonist.getDefence();
		int proHp = protagonist.getHp();
		
		int monsterAttack = monster.getAttack();
		int monsterDefence = monster.getDefence();
		int monsterHp = monster.getHp();
		int proHitMonster = MonsterController.calculateHP(proAttack, monsterDefence);
		int monsterHitPro = MonsterController.calculateHP(monsterAttack, proDefence);
		
		while (proHp > 0 && monsterHp > 0) {
			counter+=3;
			proHp -= monsterHitPro;
			monsterHp -=proHitMonster;

		}
		
		if (proHp <= 0) {
			System.out.print("Bad luck, you are dead fighting with " + monster.getName());
			System.exit(0);
		} else {
			protagonist.setHp(proHp);
			return counter;
		}
		return counter;
	}
}
