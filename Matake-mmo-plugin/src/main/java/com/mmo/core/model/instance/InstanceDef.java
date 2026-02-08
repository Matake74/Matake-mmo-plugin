package com.mmo.core.model.instance;

import java.util.Collections;
import java.util.List;

/**
 * Définition d'une instance (donjon, raid)
 * Configuration immuable chargée depuis les fichiers de config
 * 
 * Une instance définit:
 * - Les paramètres de base (type, nombre de joueurs)
 * - Les positions de spawn et de sortie
 * - Les quêtes associées
 * - Les timings (countdown, phase de loot)
 * - Les mobs, boss et coffres à spawn
 */
public class InstanceDef {

    private final String id;
    private final String name;
    private final InstanceType type;
    private final int minPlayers;
    private final int maxPlayers;

    // Spawn dans l'instance
    private final String world;
    private final double spawnX, spawnY, spawnZ;

    // Sortie de l'instance
    private final String exitWorld;
    private final double exitX, exitY, exitZ;

    // Quêtes liées (optionnel)
    private final String startQuestId;
    private final String endQuestId;

    // Timings
    private final int startCountdownSeconds;
    private final int lootPhaseSeconds;

    // Mobs et boss
    private final List<MobPackDef> mobPacks;
    private final BossDef boss;

    // Coffres de loot
    private final List<ChestDef> chests;

    /**
     * Constructeur complet d'une définition d'instance
     */
    public InstanceDef(String id, String name, InstanceType type,
                       int minPlayers, int maxPlayers,
                       String world, double spawnX, double spawnY, double spawnZ,
                       String exitWorld, double exitX, double exitY, double exitZ,
                       String startQuestId, String endQuestId,
                       int startCountdownSeconds, int lootPhaseSeconds,
                       List<MobPackDef> mobPacks, BossDef boss, List<ChestDef> chests) {
        // Validation basique
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Instance ID cannot be null or empty");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Instance name cannot be null or empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("Instance type cannot be null");
        }
        if (minPlayers < 1 || maxPlayers < minPlayers) {
            throw new IllegalArgumentException("Invalid Player count: min=" + minPlayers + ", max=" + maxPlayers);
        }
        if (world == null || world.isEmpty()) {
            throw new IllegalArgumentException("World name cannot be null or empty");
        }
        if (exitWorld == null || exitWorld.isEmpty()) {
            throw new IllegalArgumentException("Exit world name cannot be null or empty");
        }

        this.id = id;
        this.name = name;
        this.type = type;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.world = world;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.spawnZ = spawnZ;
        this.exitWorld = exitWorld;
        this.exitX = exitX;
        this.exitY = exitY;
        this.exitZ = exitZ;
        this.startQuestId = startQuestId;
        this.endQuestId = endQuestId;
        this.startCountdownSeconds = startCountdownSeconds;
        this.lootPhaseSeconds = lootPhaseSeconds;
        this.mobPacks = mobPacks != null ? mobPacks : Collections.emptyList();
        this.boss = boss;
        this.chests = chests != null ? chests : Collections.emptyList();
    }

    // ==================== Getters ====================

    public String getId() { return id; }
    public String getName() { return name; }
    public InstanceType getType() { return type; }
    public int getMinPlayers() { return minPlayers; }
    public int getMaxPlayers() { return maxPlayers; }
    
    public String getWorld() { return world; }
    public double getSpawnX() { return spawnX; }
    public double getSpawnY() { return spawnY; }
    public double getSpawnZ() { return spawnZ; }
    
    public String getExitWorld() { return exitWorld; }
    public double getExitX() { return exitX; }
    public double getExitY() { return exitY; }
    public double getExitZ() { return exitZ; }
    
    public String getStartQuestId() { return startQuestId; }
    public String getEndQuestId() { return endQuestId; }
    
    public int getStartCountdownSeconds() { return startCountdownSeconds; }
    public int getLootPhaseSeconds() { return lootPhaseSeconds; }
    
    public List<MobPackDef> getMobPacks() { 
        return Collections.unmodifiableList(mobPacks); 
    }
    
    public BossDef getBoss() { return boss; }
    
    public List<ChestDef> getChests() { 
        return Collections.unmodifiableList(chests); 
    }

    // ==================== Utility Methods ====================

    public boolean hasBoss() {
        return boss != null;
    }

    public boolean hasMobs() {
        return mobPacks != null && !mobPacks.isEmpty();
    }

    public boolean hasChests() {
        return chests != null && !chests.isEmpty();
    }

    public int getTotalMobCount() {
        return mobPacks.stream()
                .mapToInt(MobPackDef::getCount)
                .sum();
    }

    public boolean hasStartQuest() {
        return startQuestId != null && !startQuestId.isEmpty();
    }

    public boolean hasEndQuest() {
        return endQuestId != null && !endQuestId.isEmpty();
    }

    public boolean isValidPlayerCount(int PlayerCount) {
        return PlayerCount >= minPlayers && PlayerCount <= maxPlayers;
    }

    public boolean isSolo() {
        return type == InstanceType.SOLO;
    }

    public boolean isGroup() {
        return type == InstanceType.GROUP;
    }

    public boolean isRaid() {
        return type == InstanceType.RAID;
    }

    @Override
    public String toString() {
        return "InstanceDef{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", Players=" + minPlayers + "-" + maxPlayers +
                ", world='" + world + '\'' +
                ", mobs=" + getTotalMobCount() +
                ", hasBoss=" + hasBoss() +
                ", chests=" + (chests != null ? chests.size() : 0) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstanceDef that = (InstanceDef) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
