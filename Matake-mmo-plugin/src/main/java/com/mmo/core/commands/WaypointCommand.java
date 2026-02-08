package com.mmo.core.commands;

import com.hypixel.hytale.server.command.Command;
import com.hypixel.hytale.server.command.CommandSender;
import com.hypixel.hytale.server.Player.Player;
import com.mmo.core.managers.WaypointManager;

/**
 * Commande /waypoint pour gérer les points de repère
 */
public class WaypointCommand implements Command {

    private final WaypointManager waypointManager;

    public WaypointCommand(WaypointManager waypointManager) {
        this.waypointManager = waypointManager;
    }

    @Override
    public String getName() {
        return "waypoint";
    }

    @Override
    public String getDescription() {
        return "Gère les waypoints et points de repère";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Commande réservée aux joueurs.");
            return;
        }

        Player Player = (Player) sender;

        if (args.length < 1) {
            showUsage(Player);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "goto":
                if (args.length < 2) {
                    Player.sendMessage("Usage: /waypoint goto <id>");
                    return;
                }
                waypointManager.sendWaypoint(Player.getPlayer(), args[1]);
                break;
                
            case "clear":
                waypointManager.clearWaypoint(Player.getPlayer());
                break;
                
            default:
                showUsage(Player);
                break;
        }
    }

    /**
     * Affiche l'utilisation de la commande
     */
    private void showUsage(Player Player) {
        Player.sendMessage("=== Commandes de Waypoint ===");
        Player.sendMessage("/waypoint goto <id> - Définit un waypoint vers une destination");
        Player.sendMessage("/waypoint clear - Efface le waypoint actuel");
    }
}
