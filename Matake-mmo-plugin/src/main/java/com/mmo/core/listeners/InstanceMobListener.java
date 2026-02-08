package com.mmo.core.listeners;

import com.hypixel.hytale.server.entity.LivingEntity;
import com.hypixel.hytale.server.events.entity.EntityDeathEvent;
import com.mmo.core.managers.InstanceManager;

/**
 * Listener pour les événements de mort de mobs dans les instances
 * Notifie l'InstanceManager quand un mob meurt pour gérer la progression
 */
public class InstanceMobListener {

    private final InstanceManager instanceManager;

    public InstanceMobListener(InstanceManager instanceManager) {
        this.instanceManager = instanceManager;
    }

    /**
     * Appelé quand une entité meurt
     * Gère la progression des instances (vagues de mobs, boss, etc.)
     */
    public void onDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        
        LivingEntity entity = (LivingEntity) event.getEntity();
        instanceManager.onMobDeath(entity);
    }
}
