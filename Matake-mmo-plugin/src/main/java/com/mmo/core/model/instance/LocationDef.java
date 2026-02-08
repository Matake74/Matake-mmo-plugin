package com.mmo.core.model.instance;

import com.hypixel.hytale.server.world.Location;
import com.hypixel.hytale.server.world.World;

/**
 * Définition d'une location (monde + coordonnées)
 * Utilisé pour stocker des positions de manière sérialisable
 */
public class LocationDef {

    private final String world;
    private final double x;
    private final double y;
    private final double z;

    public LocationDef(String world, double x, double y, double z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getWorld() {
        return world;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    /**
     * Convertit cette définition en Location Hytale
     * @param w Le monde Hytale
     * @return Une Location utilisable
     */
    public Location toLocation(World w) {
        return new Location(w, x, y, z);
    }

    /**
     * Crée une LocationDef depuis une configuration
     * À adapter selon votre système de configuration
     */
    public static LocationDef fromConfig(Object section) {
        // TODO: Adapter à votre système de config
        // Exemple avec une Map:
        // Map<String, Object> map = (Map<String, Object>) section;
        // return new LocationDef(
        //     (String) map.get("world"),
        //     ((Number) map.get("x")).doubleValue(),
        //     ((Number) map.get("y")).doubleValue(),
        //     ((Number) map.get("z")).doubleValue()
        // );
        return null;
    }
}
