package com.mmo.core.model.profession;

/**
 * Définition d'une ressource récoltable
 */
public class ResourceDef {

    private final String id;
    private final String blockId;
    private final ProfessionType profession;
    private final int minLevel;
    private final double xp;
    private final int respawnSeconds;

    public ResourceDef(String id, String blockId, ProfessionType profession, int minLevel, double xp, int respawnSeconds) {
        this.id = id;
        this.blockId = blockId;
        this.profession = profession;
        this.minLevel = minLevel;
        this.xp = xp;
        this.respawnSeconds = respawnSeconds;
    }

    public String getId() {
        return id;
    }

    public String getBlockId() {
        return blockId;
    }

    public ProfessionType getProfession() {
        return profession;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public double getXp() {
        return xp;
    }

    public int getRespawnSeconds() {
        return respawnSeconds;
    }
}
