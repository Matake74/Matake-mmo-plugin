package com.mmo.core.model.instance;

/**
 * Définition d'un boss
 * Représente un boss unique à spawn dans l'instance
 */
public class BossDef {

    private final String id;
    private final String type;
    private final double x, y, z;

    /**
     * Crée une définition de boss
     *
     * @param id Identifiant unique du boss
     * @param type Type d'entité Hytale
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @param z Coordonnée Z
     */
    public BossDef(String id, String type, double x, double y, double z) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Boss ID cannot be null or empty");
        }
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Boss type cannot be null or empty");
        }

        this.id = id;
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // ==================== Getters ====================

    public String getId() { return id; }
    public String getType() { return type; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }

    @Override
    public String toString() {
        return "BossDef{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", pos=(" + x + "," + y + "," + z + ")" +
                '}';
    }
}