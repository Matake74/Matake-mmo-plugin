package com.mmo.core.model.instance;

/**
 * Définition d'un pack de mobs à spawn
 * Représente un groupe de mobs identiques à une position donnée
 */
public class MobPackDef {

    private final String id;
    private final String type;
    private final int count;
    private final double x, y, z;

    /**
     * Crée un pack de mobs
     *
     * @param id Identifiant unique du pack
     * @param type Type d'entité Hytale
     * @param count Nombre de mobs à spawn
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @param z Coordonnée Z
     */
    public MobPackDef(String id, String type, int count, double x, double y, double z) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Mob pack ID cannot be null or empty");
        }
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Mob type cannot be null or empty");
        }
        if (count < 1) {
            throw new IllegalArgumentException("Mob count must be at least 1");
        }

        this.id = id;
        this.type = type;
        this.count = count;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // ==================== Getters ====================

    public String getId() { return id; }
    public String getType() { return type; }
    public int getCount() { return count; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }

    @Override
    public String toString() {
        return "MobPackDef{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", count=" + count +
                ", pos=(" + x + "," + y + "," + z + ")" +
                '}';
    }
}