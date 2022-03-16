package utils;

import interfaces.Indexable;

import java.util.ArrayList;

public class ObjFinder {
    public static Indexable findFromListById(ArrayList<Indexable> list, int id) {
        Indexable indexable = null;
        for (Indexable obj : list) {
            if (obj.getId() == id) {
                indexable = obj;
                break;
            }
        }
        return indexable;
    }
}
