package models;

import models.entities.*;
import models.relations.MapBranches;
import models.relations.MapFacilities;
import models.relations.MapMonsters;
import models.relations.MapNPCs;

import java.util.ArrayList;

public class StateBuilder {
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

    public StateBuilder() {}

    public StateBuilder(Protagonist protagonist, int position, boolean finished, PreStory preStory, Backpack backpack, ArrayList<MapCell> maps, ArrayList<NPC> npcs, ArrayList<Monster> monsters, ArrayList<Item> items, ArrayList<Facility> facilities, ArrayList<Task> tasks, ArrayList<MapBranches> mapBranchesInfo, ArrayList<MapFacilities> mapFacilitiesInfo, ArrayList<MapNPCs> mapNPCsInfo, ArrayList<MapMonsters> mapMonstersInfo, ArrayList<Integer> currentTasks) {
        this.protagonist = protagonist;
        this.position = position;
        this.finished = finished;
        this.preStory = preStory;
        this.backpack = backpack;
        this.maps = maps;
        this.npcs = npcs;
        this.monsters = monsters;
        this.items = items;
        this.facilities = facilities;
        this.tasks = tasks;
        this.mapBranchesInfo = mapBranchesInfo;
        this.mapFacilitiesInfo = mapFacilitiesInfo;
        this.mapNPCsInfo = mapNPCsInfo;
        this.mapMonstersInfo = mapMonstersInfo;
        this.currentTasks = currentTasks;
    }

    public Protagonist getProtagonist() {
        return protagonist;
    }

    public void setProtagonist(Protagonist protagonist) {
        this.protagonist = protagonist;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public PreStory getPreStory() {
        return preStory;
    }

    public void setPreStory(PreStory preStory) {
        this.preStory = preStory;
    }

    public Backpack getBackpack() {
        return backpack;
    }

    public void setBackpack(Backpack backpack) {
        this.backpack = backpack;
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

    public void setNpcs(ArrayList<NPC> npcs) {
        this.npcs = npcs;
    }

    public ArrayList<Monster> getMonsters() {
        return monsters;
    }

    public void setMonsters(ArrayList<Monster> monsters) {
        this.monsters = monsters;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public ArrayList<Facility> getFacilities() {
        return facilities;
    }

    public void setFacilities(ArrayList<Facility> facilities) {
        this.facilities = facilities;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<MapBranches> getMapBranchesInfo() {
        return mapBranchesInfo;
    }

    public void setMapBranchesInfo(ArrayList<MapBranches> mapBranchesInfo) {
        this.mapBranchesInfo = mapBranchesInfo;
    }

    public ArrayList<MapFacilities> getMapFacilitiesInfo() {
        return mapFacilitiesInfo;
    }

    public void setMapFacilitiesInfo(ArrayList<MapFacilities> mapFacilitiesInfo) {
        this.mapFacilitiesInfo = mapFacilitiesInfo;
    }

    public ArrayList<MapNPCs> getMapNPCsInfo() {
        return mapNPCsInfo;
    }

    public void setMapNPCsInfo(ArrayList<MapNPCs> mapNPCsInfo) {
        this.mapNPCsInfo = mapNPCsInfo;
    }

    public ArrayList<MapMonsters> getMapMonstersInfo() {
        return mapMonstersInfo;
    }

    public void setMapMonstersInfo(ArrayList<MapMonsters> mapMonstersInfo) {
        this.mapMonstersInfo = mapMonstersInfo;
    }

    public ArrayList<Integer> getCurrentTasks() {
        return currentTasks;
    }

    public void setCurrentTasks(ArrayList<Integer> currentTasks) {
        this.currentTasks = currentTasks;
    }
}
