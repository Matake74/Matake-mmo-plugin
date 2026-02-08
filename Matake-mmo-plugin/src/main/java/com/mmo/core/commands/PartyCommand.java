package com.mmo.core.commands;

import com.hytale.server.command.Command;
import com.hytale.server.command.CommandSender;
import com.hytale.server.Player.Player;
import com.mmo.core.managers.PartyManager;
import com.mmo.core.model.party.Party;

/**
 * Commande /party pour gérer les groupes
 * Intégrée dans CorePlugin
 */
public class PartyCommand implements Command {

    private final PartyManager partyManager;

    public PartyCommand(PartyManager partyManager) {
        this.partyManager = partyManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cCommande réservée aux joueurs.");
            return;
        }

        Player Player = (Player) sender;

        if (args.length == 0) {
            sendHelp(Player);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                handleCreate(Player, args);
                break;

            case "invite":
                handleInvite(Player, args);
                break;

            case "accept":
                handleAccept(Player);
                break;

            case "decline":
            case "deny":
                handleDecline(Player);
                break;

            case "leave":
                handleLeave(Player);
                break;

            case "kick":
                handleKick(Player, args);
                break;

            case "disband":
                handleDisband(Player);
                break;

            case "promote":
                handlePromote(Player, args);
                break;

            case "info":
            case "list":
                handleInfo(Player);
                break;

            default:
                sendHelp(Player);
                break;
        }
    }

    private void handleCreate(Player Player, String[] args) {
        int maxSize = 5; // Défaut : groupe normal
        
        if (args.length > 1) {
            try {
                maxSize = Integer.parseInt(args[1]);
                if (maxSize < 2 || maxSize > 40) {
                    Player.sendMessage("§cLa taille doit être entre 2 et 40.");
                    return;
                }
            } catch (NumberFormatException e) {
                Player.sendMessage("§cTaille invalide : " + args[1]);
                return;
            }
        }

        partyManager.createParty(Player, maxSize);
    }

    private void handleInvite(Player Player, String[] args) {
        if (args.length < 2) {
            Player.sendMessage("§cUsage: /party invite <joueur>");
            return;
        }

        // TODO: Adapter selon l'API Player de Hytale
        // Player target = Player.getServer().getPlayerManager().getPlayer(args[1]);
        Player target = null; // Placeholder
        
        if (target == null) {
            Player.sendMessage("§cJoueur introuvable : " + args[1]);
            return;
        }

        partyManager.invitePlayer(Player, target);
    }

    private void handleAccept(Player Player) {
        partyManager.acceptInvite(Player);
    }

    private void handleDecline(Player Player) {
        partyManager.declineInvite(Player);
    }

    private void handleLeave(Player Player) {
        partyManager.leaveParty(Player);
    }

    private void handleKick(Player Player, String[] args) {
        if (args.length < 2) {
            Player.sendMessage("§cUsage: /party kick <joueur>");
            return;
        }

        // TODO: Adapter selon l'API Player de Hytale
        // Player target = Player.getServer().getPlayerManager().getPlayer(args[1]);
        Player target = null; // Placeholder
        
        if (target == null) {
            Player.sendMessage("§cJoueur introuvable : " + args[1]);
            return;
        }

        partyManager.kickMember(Player, target);
    }

    private void handleDisband(Player Player) {
        partyManager.disbandParty(Player);
    }

    private void handlePromote(Player Player, String[] args) {
        if (args.length < 2) {
            Player.sendMessage("§cUsage: /party promote <joueur>");
            return;
        }

        // TODO: Adapter selon l'API Player de Hytale
        // Player target = Player.getServer().getPlayerManager().getPlayer(args[1]);
        Player target = null; // Placeholder
        
        if (target == null) {
            Player.sendMessage("§cJoueur introuvable : " + args[1]);
            return;
        }

        partyManager.promoteLeader(Player, target);
    }

    private void handleInfo(Player Player) {
        Party party = partyManager.getParty(Player);
        
        if (party == null) {
            Player.sendMessage("§cVous n'êtes dans aucun groupe.");
            return;
        }

        Player.sendMessage("§e§l=== Informations du Groupe ===");
        Player.sendMessage("§eLeader : §f" + party.getLeader().getName());
        Player.sendMessage("§eTaille : §f" + party.getSize() + "/" + party.getMaxSize());
        Player.sendMessage("§eMembres :");
        
        for (Player member : party.getMembers()) {
            String prefix = party.isLeader(member) ? "§6★ " : "§7- ";
            String status = member.isOnline() ? "§a●" : "§c●";
            Player.sendMessage(prefix + status + " §f" + member.getName());
        }
    }

    private void sendHelp(Player Player) {
        Player.sendMessage("§e§l=== Commandes de Groupe ===");
        Player.sendMessage("§e/party create [taille] §7- Créer un groupe");
        Player.sendMessage("§e/party invite <joueur> §7- Inviter un joueur §8(leader)");
        Player.sendMessage("§e/party accept §7- Accepter une invitation");
        Player.sendMessage("§e/party decline §7- Refuser une invitation");
        Player.sendMessage("§e/party leave §7- Quitter le groupe");
        Player.sendMessage("§e/party kick <joueur> §7- Expulser un membre §8(leader)");
        Player.sendMessage("§e/party promote <joueur> §7- Promouvoir leader §8(leader)");
        Player.sendMessage("§e/party disband §7- Dissoudre le groupe §8(leader)");
        Player.sendMessage("§e/party info §7- Informations du groupe");
    }
}
