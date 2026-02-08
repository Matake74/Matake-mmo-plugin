package com.mmo.core.hud;

import com.hypixel.hytale.server.Player.Player;
import com.mmo.core.api.InstanceAPI;
import com.mmo.core.model.hud.HudElement;
import com.mmo.core.model.hud.HudLayer;

/**
 * Gestionnaire de rendu HUD pour les instances
 * Met à jour l'affichage des informations d'instance pour un joueur
 */
public class InstanceHudRenderer {

    /**
     * Met à jour le HUD d'instance pour un joueur
     * Affiche le nom de l'instance si le joueur est dans une instance
     */
    public static void update(Player Player) {
        if (Player == null) return;
        
        String instanceName = InstanceAPI.getInstanceName(Player);
        if (instanceName == null) {
            // Le joueur n'est pas dans une instance, effacer le HUD
            Player.sendHud(HudLayer.INSTANCE, null);
            return;
        }

        // Créer l'élément HUD avec le nom de l'instance (couleur cyan)
        HudElement element = new HudElement("Instance: " + instanceName, 0x00FFFF);
        Player.sendHud(HudLayer.INSTANCE, element);
    }
    
    /**
     * Efface le HUD d'instance pour un joueur
     */
    public static void clear(Player Player) {
        if (Player == null) return;
        Player.sendHud(HudLayer.INSTANCE, null);
    }
}
