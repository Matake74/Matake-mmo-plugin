package com.mmo.core.model;

/**
 * Définition d'une étape de quête
 */
public class QuestStageDef {

    private final String id;
    private final String type;      // TALK / REACH_LOCATION / KILL_BOSS / COLLECT / ...
    private final String npcId;
    private final String waypointId;
    private final String bossId;

    public QuestStageDef(String id, String type, String npcId, String waypointId, String bossId) {
        this.id = id;
        this.type = type;
        this.npcId = npcId;
        this.waypointId = waypointId;
        this.bossId = bossId;
    }

    public String getId() { 
        return id; 
    }
    
    public String getType() { 
        return type; 
    }
    
    public String getNpcId() { 
        return npcId; 
    }
    
    public String getWaypointId() { 
        return waypointId; 
    }
    
    public String getBossId() { 
        return bossId; 
    }
}
