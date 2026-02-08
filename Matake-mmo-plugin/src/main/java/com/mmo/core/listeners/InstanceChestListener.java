package com.mmo.core.listeners;

import com.hypixel.hytale.server.events.Player.PlayerInteractEntityEvent;
import com.hypixel.hytale.server.Player.Player;
import com.mmo.core.managers.InstanceManager;

/**
 * Listener pour les interactions avec les coffres dans les instances
 * Gère l'ouverture des coffres de loot
 */
public class InstanceChestListener {

    private final InstanceManager instanceManager;

    public InstanceChestListener(InstanceManager instanceManager) {
        this.instanceManager = instanceManager;
    }

    /**
     * Appelé quand un joueur interagit avec une entité
     * Détecte les interactions avec les coffres d'instance
     */
    public void onInteract(PlayerInteractEntityEvent event) {
        Player Player = event.getPlayer();
        
        // Récupérer l'ID de l'entité (système de quête ou custom name)
        String questEntityId = event.getEntity().getCustomName();
        if (questEntityId == null) return;

        // Notifier le gestionnaire d'instances
        instanceManager.onChestQuestEntityInteract(Player, questEntityId);
    }
}
