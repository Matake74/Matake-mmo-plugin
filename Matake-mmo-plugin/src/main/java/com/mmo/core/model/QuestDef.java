package com.mmo.core.model;

import java.util.List;

/**
 * Définition d'une quête
 */
public class QuestDef {

    private final String id;
    private final String name;
    private final String description;
    private final boolean coop;
    private final List<QuestStageDef> stages;

    public QuestDef(String id, String name, String description, boolean coop, List<QuestStageDef> stages) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.coop = coop;
        this.stages = stages;
    }

    public String getId() { 
        return id; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public String getDescription() { 
        return description; 
    }
    
    public boolean isCoop() { 
        return coop; 
    }
    
    public List<QuestStageDef> getStages() { 
        return stages; 
    }
}
