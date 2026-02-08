package com.mmo.core.model.profession;

/**
 * Définition d'un atelier de craft
 */
public class WorkstationDef {

    private final String id;
    private final String name;
    private final String blockId;

    public WorkstationDef(String id, String name, String blockId) {
        this.id = id;
        this.name = name;
        this.blockId = blockId;
    }

    public String getId() {
        return id;
    }

    public String getBlockId() {
        return blockId;
    }

    public String getName() {
        return name;
    }
}
