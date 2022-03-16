package models;

import models.entities.*;
import models.relations.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class State {
    private Protagonist protagonist;
    private int position;
    private boolean finished;
    private PreStory preStory;
    private Backpack backpack;
    private ArrayList<MapCell> maps;
    private ArrayList<NPC> npcs;
    private ArrayList<Monster> monsters;
    private ArrayList<Item> items;
    private ArrayList<Facility> facilities;
    private ArrayList<Task> tasks;
    private ArrayList<MapBranches> mapBranchesInfo;
    private ArrayList<MapFacilities> mapFacilitiesInfo;
    private ArrayList<MapNPCs> mapNPCsInfo;
    private ArrayList<MapMonsters> mapMonstersInfo;
    private ArrayList<Integer> currentTasks;

    private static final State instance = new State();

    private State() {
        protagonist = null;
        preStory = new PreStory();
        monsters = new ArrayList<>();
        maps = new ArrayList<>();
        npcs = new ArrayList<>();
        items = new ArrayList<>();
        facilities = new ArrayList<>();
        tasks = new ArrayList<>();
        mapNPCsInfo = new ArrayList<>();
        mapBranchesInfo = new ArrayList<>();
        mapFacilitiesInfo = new ArrayList<>();
        mapMonstersInfo = new ArrayList<>();
        currentTasks = new ArrayList<>();
        backpack = Backpack.getInstance();
        finished = false;
    }

    public void setState(StateBuilder stateBuilder) {
        protagonist = stateBuilder.getProtagonist();
        position = stateBuilder.getPosition();
        preStory = stateBuilder.getPreStory();
        monsters = stateBuilder.getMonsters();
        maps = stateBuilder.getMaps();
        npcs = stateBuilder.getNpcs();
        items = stateBuilder.getItems();
        facilities = stateBuilder.getFacilities();
        tasks = stateBuilder.getTasks();
        mapNPCsInfo = stateBuilder.getMapNPCsInfo();
        mapBranchesInfo = stateBuilder.getMapBranchesInfo();
        mapFacilitiesInfo = stateBuilder.getMapFacilitiesInfo();
        mapMonstersInfo = stateBuilder.getMapMonstersInfo();
        currentTasks = stateBuilder.getCurrentTasks();
        backpack = stateBuilder.getBackpack();
        finished = stateBuilder.isFinished();
    }

    public static State getInstance() {
        return instance;
    }

    public Protagonist getProtagonist() {
        return protagonist;
    }

    public void setProtagonist(Protagonist protagonist) {
        this.protagonist = protagonist;
    }

    public ArrayList<Monster> getMonsters() {
        return monsters;
    }

    public void setMonsters(ArrayList<Monster> monsters) {
        this.monsters = monsters;
    }

    public PreStory getPreStory() {
        return preStory;
    }

    public void setPreStory(PreStory preStory) {
        this.preStory = preStory;
    }

    public ArrayList<MapFacilities> getMapFacilitiesInfo() {
        return mapFacilitiesInfo;
    }

    public void setMapFacilitiesInfo(ArrayList<MapFacilities> mapFacilitiesInfo) {
        this.mapFacilitiesInfo = mapFacilitiesInfo;
    }

    public ArrayList<MapMonsters> getMapMonstersInfo() {
        return mapMonstersInfo;
    }

    public void setMapMonstersInfo(ArrayList<MapMonsters> mapMonstersInfo) {
        this.mapMonstersInfo = mapMonstersInfo;
    }

    public ArrayList<MapCell> getMaps() {
        return maps;
    }

    public void setMaps(ArrayList<MapCell> maps) {
        this.maps = maps;
    }

    public ArrayList<NPC> getNpcs() {
        return npcs;
    }

    public ArrayList<Facility> getFacilities() {
        return facilities;
    }

    public void setFacilities(ArrayList<Facility> fixedItems) {
        this.facilities = fixedItems;
    }

    public ArrayList<Integer> getCurrentTasks() {
        return currentTasks;
    }

    public void setCurrentTasks(ArrayList<Integer> currentTasks) {
        this.currentTasks = currentTasks;
    }

    public void setNpcs(ArrayList<NPC> npcs) {
        this.npcs = npcs;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Backpack getBackpack() {
        return backpack;
    }

    public void setBackpack(Backpack backpack) {
        this.backpack = backpack;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public MapCell currentPosition() {
        for (MapCell mapCell : maps) {
            if (mapCell.getId() == position) {
                return mapCell;
            }
        }
        return null;
    }

    public ArrayList<MapBranches> getMapBranchesInfo() {
        return mapBranchesInfo;
    }

    public void setMapBranchesInfo(ArrayList<MapBranches> mapBranchesInfo) {
        this.mapBranchesInfo = mapBranchesInfo;
    }

    public ArrayList<MapFacilities> getMapItemsInfo() {
        return mapFacilitiesInfo;
    }

    public void setMapItemsInfo(ArrayList<MapFacilities> mapFacilitiesInfo) {
        this.mapFacilitiesInfo = mapFacilitiesInfo;
    }

    public ArrayList<MapNPCs> getMapNPCsInfo() {
        return mapNPCsInfo;
    }

    public void setMapNPCsInfo(ArrayList<MapNPCs> mapNPCsInfo) {
        this.mapNPCsInfo = mapNPCsInfo;
    }

    public ArrayList<Integer> getBranchKeysByMapKey(int mapKey) {
        MapBranches mapBranches = findMapBranchesByMapKey(mapBranchesInfo, mapKey);
        if (mapBranches != null) {
            return mapBranches.getBranchKeys();
        }
        return null;
//        for (MapBranches mapBranches : mapBranchesInfo) {
//            if (mapBranches.getMapKey() == mapKey) {
//                return mapBranches.getBranchKeys();
//            }
//        }
//        return null;
    }

    private MapBranches findMapBranchesByMapKey(List<MapBranches> mapBranchesList, int mapKey) {
        if (mapBranchesList == null || mapBranchesList.size() == 0) {
            return null;
        }
        mapBranchesList.sort(MapBranches::compareTo);

        int len = mapBranchesList.size();
        int midLen = len / 2;
        MapBranches midObj = mapBranchesList.get(midLen);

        if (midObj.getMapKey() == mapKey) {
            return midObj;
        } else if (mapKey < midObj.getMapKey()) {
            return findMapBranchesByMapKey(mapBranchesList.subList(0, midLen), mapKey);
        } else {
            return findMapBranchesByMapKey(mapBranchesList.subList(midLen + 1, len), mapKey);
        }
    }

    private MapCell findMapCellByMapKey(List<MapCell> mapCells, int mapKey) {
//        System.out.println("\n>>> List: " + mapCells + ", key: " + mapKey);
        if (mapCells == null || mapCells.size() == 0) {
            return null;
        }
        mapCells.sort(MapCell::compareTo);
//        for (MapCell mapCell: mapCells) {
//            System.out.println("Map Id: " + mapCell.getId() + ", name: " + mapCell.getName());
//        }

        int len = mapCells.size();
        int midLen = len / 2;
        MapCell midMapCell = mapCells.get(midLen);
//        System.out.println("Length: " + len);
//        System.out.println("mid index: " + midLen);
//        System.out.println("mid obj: " + midMapCell.getId());
        if (midMapCell.getId() == mapKey) {
            return midMapCell;
        } else if (mapKey < midMapCell.getId()) {
            return findMapCellByMapKey(mapCells.subList(0, midLen), mapKey);
        } else {
            return findMapCellByMapKey(mapCells.subList(midLen + 1, len), mapKey);
        }
    }

    public ArrayList<Integer> getFacilityIDsByMapKey(int mapKey) {
        MapFacilities mapFacilities = findFacilityIDsByMapKey(mapFacilitiesInfo, mapKey);
        if (mapFacilities == null) {
            return null;
        }
        return mapFacilities.getFacilityIds();

//        for (MapFacilities mapFacilities : mapFacilitiesInfo) {
//            if (mapFacilities.getMapKey() == mapKey) {
//                return mapFacilities.getFacilityIds();
//            }
//        }
//        return null;
    }

    private MapFacilities findFacilityIDsByMapKey(List<MapFacilities> mapFacilities, int mapKey) {
        if (mapFacilities == null || mapFacilities.size() == 0) {
            return null;
        }
        mapFacilities.sort(MapFacilities::compareTo);

        int len = mapFacilities.size();
        int midLen = len / 2;
        MapFacilities midObj = mapFacilities.get(midLen);

        if (midObj.getMapKey() == mapKey) {
            return midObj;
        } else if (mapKey < midObj.getMapKey()) {
            return findFacilityIDsByMapKey(mapFacilities.subList(0, midLen), mapKey);
        } else {
            return findFacilityIDsByMapKey(mapFacilities.subList(midLen + 1, len), mapKey);
        }
    }

    public Item getItemById(int itemId) {
        return findItemById(items, itemId);
//        for (Item item : this.items) {
//            if (item.getId() == itemId) {
//                return item;
//            }
//        }
//        return null;
    }

    private Item findItemById(List<Item> items, int id) {
        if (items == null || items.size() == 0) {
            return null;
        }
        items.sort(Item::compareTo);

        int len = items.size();
        int midLen = len / 2;
        Item midObj = items.get(midLen);

        if (midObj.getId() == id) {
            return midObj;
        } else if (id < midObj.getId()) {
            return findItemById(items.subList(0, midLen), id);
        } else {
            return findItemById(items.subList(midLen + 1, len), id);
        }
    }

    public MapCell getMapCellById(int id) {
        return findMapCellByMapKey(maps, id);
//        for (MapCell mapCell : this.maps) {
//            if (mapCell.getId() == id) {
//                return mapCell;
//            }
//        }
//        return null;
    }

    public NPC getNPCById(int id) {
        return findNPCById(npcs, id);
//        for (NPC npc : this.npcs) {
//            if (npc.getId() == id) {
//                return npc;
//            }
//        }
//        return null;
    }

    private NPC findNPCById(List<NPC> npcs, int id) {
        if (npcs == null || npcs.size() == 0) {
            return null;
        }
        npcs.sort(NPC::compareTo);

        int len = npcs.size();
        int midLen = len / 2;
        NPC midObj = npcs.get(midLen);

        if (midObj.getId() == id) {
            return midObj;
        } else if (id < midObj.getId()) {
            return findNPCById(npcs.subList(0, midLen), id);
        } else {
            return findNPCById(npcs.subList(midLen + 1, len), id);
        }
    }

    public ArrayList<Integer> getNPCKeysByMapKey(int mapKey) {
        MapNPCs mapNPCs = findMapNPCsByMapKey(mapNPCsInfo, mapKey);
        if (mapNPCs != null) {
            return mapNPCs.getNpcList();
        }
        return null;
//        for (MapNPCs mapNPCs : mapNPCsInfo) {
//            if (mapNPCs.getMapKey() == mapKey) {
//                return mapNPCs.getNpcList();
//            }
//        }
//        return null;
    }

    private MapNPCs findMapNPCsByMapKey(List<MapNPCs> mapNPCsList, int mapKey) {
        if (mapNPCsList == null || mapNPCsList.size() == 0) {
            return null;
        }
        mapNPCsList.sort(MapNPCs::compareTo);

        int len = mapNPCsList.size();
        int midLen = len / 2;
        MapNPCs midObj = mapNPCsList.get(midLen);

        if (midObj.getMapKey() == mapKey) {
            return midObj;
        } else if (mapKey < midObj.getMapKey()) {
            return findMapNPCsByMapKey(mapNPCsList.subList(0, midLen), mapKey);
        } else {
            return findMapNPCsByMapKey(mapNPCsList.subList(midLen + 1, len), mapKey);
        }
    }

    public ArrayList<NPC> getNPCsByMapKey(int mapKey) {
        ArrayList<NPC> res = new ArrayList<>();
        MapNPCs mapNPCs = findMapNPCsByMapKey(mapNPCsInfo, mapKey);
        if (mapNPCs != null) {
            for (Integer npcId : mapNPCs.getNpcList()) {
                NPC npc = findNPCById(npcs, npcId);
                if (npc != null) {
                    res.add(npc);
                }
            }
        }
        return res;
//        for (MapNPCs mapNPCs : mapNPCsInfo) {
//            if (mapNPCs.getMapKey() == mapKey) {
//                for (Integer npcId : mapNPCs.getNpcList()) {
//                    NPC npc = getNPCById(npcId);
//                    res.add(npc);
//                }
//            }
//        }
//        return res;
    }

    public ArrayList<Monster> getMonstersByMapKey(int mapKey) {
        ArrayList<Monster> res = new ArrayList<>();

        MapMonsters mapMonsters = findMapMonstersByMapKey(mapMonstersInfo, mapKey);
        if (mapMonsters != null) {
            for (Integer monsterId : mapMonsters.getMonsterIds()) {
                Monster monster = getMonsterById(monsterId);
                if (monster != null) {
                    res.add(monster);
                }
            }
        }
//        for (MapMonsters mapMonsters : mapMonstersInfo) {
//            if (mapMonsters.getMapKey() == mapKey) {
//                for (Integer monsterId : mapMonsters.getMonsterIds()) {
//                    Monster monster = getMonsterById(monsterId);
//                    res.add(monster);
//                }
//            }
//        }
        return res;
    }

    private MapMonsters findMapMonstersByMapKey(List<MapMonsters> mapMonstersList, int mapKey) {
        if (mapMonstersList == null || mapMonstersList.size() == 0) {
            return null;
        }
        mapMonstersList.sort(MapMonsters::compareTo);

        int len = mapMonstersList.size();
        int midLen = len / 2;
        MapMonsters midObj = mapMonstersList.get(midLen);

        if (midObj.getMapKey() == mapKey) {
            return midObj;
        } else if (mapKey < midObj.getMapKey()) {
            return findMapMonstersByMapKey(mapMonstersList.subList(0, midLen), mapKey);
        } else {
            return findMapMonstersByMapKey(mapMonstersList.subList(midLen + 1, len), mapKey);
        }
    }


    private Monster findMonsterById(List<Monster> monsters, int id) {
        if (monsters == null || monsters.size() == 0) {
            return null;
        }
        monsters.sort(Monster::compareTo);

        int len = monsters.size();
        int midLen = len / 2;
        Monster midObj = monsters.get(midLen);

        if (midObj.getId() == id) {
            return midObj;
        } else if (id < midObj.getId()) {
            return findMonsterById(monsters.subList(0, midLen), id);
        } else {
            return findMonsterById(monsters.subList(midLen + 1, len), id);
        }
    }

    public State addMapBranchesInfo(MapBranches mapBranches) {
        if (mapBranchesInfo == null) {
            mapBranchesInfo = new ArrayList<>();
        }
        this.mapBranchesInfo.add(mapBranches);
        return instance;
    }

    public State addMapNPCsInfo(MapNPCs mapNPCs) {
        if (mapNPCsInfo == null) {
            mapNPCsInfo = new ArrayList<>();
        }
        this.mapNPCsInfo.add(mapNPCs);
        return instance;
    }

    public State addMapFacilitiesInfo(MapFacilities mapFacilities) {
        if (mapFacilitiesInfo == null) {
            mapFacilitiesInfo = new ArrayList<>();
        }
        this.mapFacilitiesInfo.add(mapFacilities);
        return instance;
    }

    public State addMapMonstersInfo(MapMonsters mapMonsters) {
        if (mapMonstersInfo == null) {
            mapMonstersInfo = new ArrayList<>();
        }
        this.mapMonstersInfo.add(mapMonsters);
        return instance;
    }

    public void assignNewTask(int taskId) {
//        System.out.println(currentTasks.contains(taskId));
//        System.out.println(currentTasks);
//        System.out.println(taskId);
        if (currentTasks.contains(taskId)) {
            System.out.println("\nYou have received this task.");
            return;
        }
        for (Task task : tasks) {
            if (task.getId() == taskId) {
                currentTasks.add(taskId);
//                System.out.println("Added: " + taskId);
                return;
            }
        }
        System.out.println("Task not exists.");
    }

//    public Integer removeTask(int taskId) {
//        for (int i = 0; i < tasks.size(); i++) {
//            Task task = tasks.get(i);
//            if (task.getId() == taskId) {
//                tasks.remove(i);
//                return taskId;
//            }
//        }
//        return null;
//    }

    public Task getTaskById(int id) {
        return findTaskById(tasks, id);
//        for (Task task : tasks) {
//            if (task.getId() == id) {
//                return task;
//            }
//        }
//        return null;
    }

    private Task findTaskById(List<Task> tasks, int id) {
        if (tasks == null || tasks.size() == 0) {
            return null;
        }
        tasks.sort(Task::compareTo);

        int len = tasks.size();
        int midLen = len / 2;
        Task midObj = tasks.get(midLen);

        if (midObj.getId() == id) {
            return midObj;
        } else if (id < midObj.getId()) {
            return findTaskById(tasks.subList(0, midLen), id);
        } else {
            return findTaskById(tasks.subList(midLen + 1, len), id);
        }
    }

    public Facility getFacilityById(int id) {
        for (Facility fixedItem : facilities) {
            if (fixedItem.getId() == id) {
                return fixedItem;
            }
        }
        return null;
    }

    public Monster getMonsterById(int id) {
        return findMonsterById(monsters, id);
//        for (Monster monster : monsters) {
//            if (monster.getId() == id) {
//                return monster;
//            }
//        }
//        return null;
    }

    public void finishCurrentTask(int taskId) {
        for (int i = 0; i < currentTasks.size(); i++) {
            int currentTaskId = currentTasks.get(i);
            if (currentTaskId == taskId) {
                currentTasks.remove(i);
                return;
            }
        }
    }

    public State addNPC(NPC npc) {
        this.npcs.add(npc);
        return instance;
    }

    public State addMapCell(MapCell mapCell) {
        this.maps.add(mapCell);
        return instance;
    }

    public State addTask(Task task) {
        this.tasks.add(task);
        return instance;
    }

    public ArrayList<Integer> getMapBranchIdsById(int mapKey) {
        for (MapBranches mapBranches : this.mapBranchesInfo) {
            if (mapBranches.getMapKey() == mapKey) {
                return mapBranches.getBranchKeys();
            }
        }
        return new ArrayList<>();
    }

    public State addItem(Item item) {
        this.items.add(item);
        return instance;
    }

    public State addFacility(Facility fixedItem) {
        this.facilities.add(fixedItem);
        return instance;
    }

    public State addMonster(Monster monster) {
        this.monsters.add(monster);
        return instance;
    }

    public MapCell getConnectedMapByName(String mapName) {
        ArrayList<Integer> mapBranchIds = getMapBranchIdsById(position);
        if (mapBranchIds == null) {
            return null;
        }
        for (int mapBranchId : mapBranchIds) {
            MapCell mapCell = getMapCellById(mapBranchId);
            if (mapCell.getName().trim().toLowerCase().equals(mapName)) {
                return mapCell;
            }
        }
        return null;
    }

    public NPC getCurrentNPCsByName(String npcName) {
        ArrayList<NPC> npcs = getNPCsByMapKey(position);
        if (npcs == null) {
            return null;
        }
        for (NPC npc : npcs) {
            if (npc.getName().trim().toLowerCase().equals(npcName)) {
                return npc;
            }
        }
        return null;
    }

    public Facility getCurrentFacilitiesByName(String name) {
        ArrayList<Integer> mapFacilityIds = getFacilityIDsByMapKey(position);
        if (mapFacilityIds == null) {
            return null;
        }
        for (Integer facilityId : mapFacilityIds) {
            Facility facility = getFacilityById(facilityId);
            if (facility.getName().toLowerCase().trim().equals(name)) {
                return facility;
            }
        }
        return null;
    }

    public Monster getCurrentMonstersByName(String name) {
        ArrayList<Monster> monsters = getMonstersByMapKey(position);
        if (monsters == null) {
            return null;
        }
        for (Monster monster : monsters) {
            if (monster.getName().toLowerCase().trim().equals(name)) {
                return monster;
            }
        }
        return null;
    }

    public Item getItemFromBackpackByName(String name) {
        for (int itemId : backpack.getItems()) {
            Item item = getItemById(itemId);
            if (item.getName().toLowerCase().trim().equals(name)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "State{" +
                "protagonist=" + protagonist.toString() +
                ", position=" + position +
                ", finished=" + finished +
                ", preStory=" + preStory +
                ", backpack=" + backpack +
                ", maps=" + maps +
                ", npcs=" + npcs +
                ", monsters=" + monsters +
                ", items=" + items +
                ", facilities=" + facilities +
                ", tasks=" + tasks +
                ", mapBranchesInfo=" + mapBranchesInfo +
                ", mapFacilitiesInfo=" + mapFacilitiesInfo +
                ", mapNPCsInfo=" + mapNPCsInfo +
                ", mapMonstersInfo=" + mapMonstersInfo +
                ", currentTasks=" + currentTasks +
                '}';
    }
}
