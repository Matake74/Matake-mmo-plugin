package com.mmo.core.model;

/**
 * Définition d'un waypoint (point de repère)
 */
public class WaypointDef {

    private final String id;
    private final String name;
    private final String world;
    private final double x;
    private final double y;
    private final double z;

    public WaypointDef(String id, String name, String world, double x, double y, double z) {
        this.id = id;
        this.name = name;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getId() { 
        return id; 
    }
    
    public String getName() { 
        return name; 
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
}
