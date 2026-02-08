package com.mmo.core.commands;

import com.hypixel.hytale.server.command.Command;
import com.hypixel.hytale.server.command.CommandSender;
import com.hypixel.hytale.server.Player.Player;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.mmo.core.managers.InstanceManager;

/**
 * Commande /instance pour gérer les instances
 * Usage:
 *   /instance solo <id>     - Lancer une instance solo
 *   /instance group <id>    - Lancer une instance de groupe
 *   /instance raid <id>     - Lancer une instance de raid
 *   /instance leave         - Quitter l'instance actuelle
 */
public class InstanceCommand extends AbstractPlayerCommand {

    private final InstanceManager instanceManager;

    public InstanceCommand(InstanceManager instanceManager) {
        this.instanceManager = instanceManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("[Instance] Cette commande est réservée aux joueurs.");
            return;
        }
        Player Player = (Player) sender;

        if (args.length < 1) {
            sendUsage(Player);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "solo":
                if (args.length < 2) {
                    Player.sendMessage("Usage: /instance solo <id>");
                    return;
                }
                instanceManager.startSoloInstance(Player, args[1]);
                break;

            case "group":
            case "raid":
                if (args.length < 2) {
                    Player.sendMessage("Usage: /instance " + args[0] + " <id>");
                    return;
                }
                instanceManager.startGroupOrRaidInstance(Player, args[1]);
                break;

            case "leave":
                instanceManager.leaveInstance(Player);
                break;

            default:
                sendUsage(Player);
                break;
        }
    }

    private void sendUsage(Player Player) {
        Player.sendMessage("=== Commandes Instance ===");
        Player.sendMessage("/instance solo <id>     §7- Lancer une instance solo");
        Player.sendMessage("/instance group <id>    §7- Lancer une instance de groupe");
        Player.sendMessage("/instance raid <id>     §7- Lancer une instance de raid");
        Player.sendMessage("/instance leave         §7- Quitter l'instance actuelle");
    }
}
