package com.mmo.core.model.instance;

/**
 * Définition d'un coffre de loot dans une instance
 * Contient toutes les informations nécessaires pour spawner et gérer un coffre
 */
public class ChestDef {
    
    private final String id;
    private final String questEntityId;
    private final String lootTableId;
    private final double x, y, z;
    
    /**
     * Constructeur complet d'un coffre
     * @param id Identifiant unique du coffre
     * @param questEntityId ID de l'entité quest (pour l'interaction)
     * @param lootTableId ID de la table de loot associée
     * @param x Position X du coffre
     * @param y Position Y du coffre
     * @param z Position Z du coffre
     */
    public ChestDef(String id, String questEntityId, String lootTableId, double x, double y, double z) {
        this.id = id;
        this.questEntityId = questEntityId;
        this.lootTableId = lootTableId;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    // Getters
    public String getId() { return id; }
    public String getQuestEntityId() { return questEntityId; }
    public String getLootTableId() { return lootTableId; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    
    @Override
    public String toString() {
        return "ChestDef{" +
                "id='" + id + '\'' +
                ", questEntityId='" + questEntityId + '\'' +
                ", lootTableId='" + lootTableId + '\'' +
                ", position=(" + x + ", " + y + ", " + z + ")" +
                '}';
    }
}
