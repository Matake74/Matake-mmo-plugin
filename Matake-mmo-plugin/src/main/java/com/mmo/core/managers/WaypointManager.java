package com.mmo.core.managers;

import com.hypixel.hytale.server.Server;
import com.hypixel.hytale.server.Player.Player;
import com.hypixel.hytale.server.world.World;
import com.mmo.core.CorePlugin;
import com.mmo.core.model.WaypointDef;

import java.util.HashMap;
import java.util.Map;
import java.util.Player;

/**
 * Gestionnaire de waypoints (points de repère pour les quêtes)
 */
public class WaypointManager {

    private final CorePlugin plugin;
    private final Map<String, WaypointDef> waypoints = new HashMap<>();

    public WaypointManager(CorePlugin plugin) {
        this.plugin = plugin;
        loadWaypoints();
    }

    /**
     * Charge les waypoints depuis le fichier de configuration
     */
    private void loadWaypoints() {
        try {
            var cfg = getServer().getConfigManager().getConfig(plugin, "config/waypoints.yml");
            var section = cfg.getSection("waypoints");
            if (section == null) {
                plugin.getLogger().warn("[WaypointManager] Aucune section 'waypoints' trouvée dans waypoints.yml");
                return;
            }

            for (String id : section.getKeys()) {
                var wSec = section.getSection(id);
                String name = wSec.getString("name");
                String world = wSec.getString("world");
                double x = wSec.getDouble("x");
                double y = wSec.getDouble("y");
                double z = wSec.getDouble("z");

                waypoints.put(id, new WaypointDef(id, name, world, x, y, z));
            }

            plugin.getLogger().info("[WaypointManager] " + waypoints.size() + " waypoints chargés.");
        } catch (Exception e) {
            plugin.getLogger().error("[WaypointManager] Erreur lors du chargement des waypoints: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Récupère un waypoint par son ID
     */
    public WaypointDef getWaypoint(String id) {
        return waypoints.get(id);
    }

    /**
     * Envoie un waypoint à un joueur
     */
    public void sendWaypoint(Player Player, String waypointId) {
        WaypointDef def = waypoints.get(waypointId);
        if (def == null) {
            sendMessage(Player, "[Quête] Waypoint introuvable.");
            return;
        }

        World world = getServer().getWorldManager().getWorld(def.getWorld());
        if (world == null) {
            sendMessage(Player, "[Quête] Monde introuvable pour le waypoint.");
            return;
        }

        // TODO: Utiliser l'API Hytale pour afficher le waypoint dans le HUD ou la boussole
        // Exemple: Player.setCompassTarget(new Location(world, def.getX(), def.getY(), def.getZ()));
        
        sendMessage(Player, "[Quête] Nouvel objectif : " + def.getName() + 
                   " (" + (int)def.getX() + ", " + (int)def.getY() + ", " + (int)def.getZ() + ")");
    }

    /**
     * Efface le waypoint d'un joueur
     */
    public void clearWaypoint(Player Player) {
        // TODO: Effacer le waypoint du HUD / marqueur du joueur
        sendMessage(Player, "[Quête] Waypoint effacé.");
    }

    /**
     * Envoie un message à un joueur
     */
    private void sendMessage(Player Player, String message) {
        Player Player = getServer().getPlayerManager().getPlayer(Player);
        if (Player != null) {
            Player.sendMessage(message);
        }
    }

    /**
     * Récupère le serveur
     */
    private Server getServer() {
        return plugin.getServer();
    }
}
