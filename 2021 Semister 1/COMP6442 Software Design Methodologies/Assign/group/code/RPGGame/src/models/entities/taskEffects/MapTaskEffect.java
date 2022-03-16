package models.entities.taskEffects;

import enums.TaskEffectType;
import models.entities.TaskEffect;

public class MapTaskEffect extends TaskEffect {
    private int mapId;

    public MapTaskEffect(int mapId) {
        super(TaskEffectType.MAP);
        this.mapId = mapId;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }
}
