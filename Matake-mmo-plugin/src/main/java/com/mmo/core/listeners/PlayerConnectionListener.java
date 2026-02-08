package com.mmo.core.listeners;

import com.hytale.server.events.Player.PlayerJoinEvent;
import com.hytale.server.events.Player.PlayerLeaveEvent;
import com.hytale.server.Player.Player;
import com.mmo.core.CorePlugin;
import com.mmo.core.managers.HudManager;
import com.mmo.core.managers.PartyManager;

/**
 * Listener pour les événements de connexion/déconnexion
 * Gère l'initialisation et le nettoyage du HUD et des groupes
 */
public class PlayerConnectionListener {

    private final CorePlugin plugin;
    private final HudManager hudManager;
    private final PartyManager partyManager;

    public PlayerConnectionListener(CorePlugin plugin) {
        this.plugin = plugin;
        this.hudManager = plugin.getHudManager();
        this.partyManager = plugin.getPartyManager();
    }

    /**
     * Gère la connexion d'un joueur
     */
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player Player = event.getPlayer();

        // Initialiser le HUD
        if (hudManager != null) {
            hudManager.onPlayerJoin(Player);
        }

        // Afficher un message de bienvenue avec le système HUD
        // HudAPI.showNotification(Player, "Bienvenue", "Connecté au serveur MMO!");

        plugin.getLogger().info("[PlayerConnection] " + Player.getName() + " connecté.");
    }

    /**
     * Gère la déconnexion d'un joueur
     */
    public void onPlayerLeave(PlayerLeaveEvent event) {
        Player Player = event.getPlayer();

        // Nettoyer le HUD
        if (hudManager != null) {
            hudManager.onPlayerLeave(Player);
        }

        // Gérer la déconnexion du groupe
        if (partyManager != null) {
            partyManager.handlePlayerDisconnect(Player);
        }

        plugin.getLogger().info("[PlayerConnection] " + Player.getName() + " déconnecté.");
    }
}
