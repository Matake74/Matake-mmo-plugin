package com.mmo.core.model.profession;

/**
 * Instance d'une ressource récoltée en cours de respawn
 */
public class ResourceInstance {

    private final String resourceId;
    private final String world;
    private final int x;
    private final int y;
    private final int z;
    private final long destroyedAt;
    private final long respawnAt;

    public ResourceInstance(String resourceId, String world, int x, int y, int z, long destroyedAt, long respawnAt) {
        this.resourceId = resourceId;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.destroyedAt = destroyedAt;
        this.respawnAt = respawnAt;
    }

    public String getKey() {
        return world + ":" + x + ":" + y + ":" + z;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public long getDestroyedAt() {
        return destroyedAt;
    }

    public long getRespawnAt() {
        return respawnAt;
    }
}
