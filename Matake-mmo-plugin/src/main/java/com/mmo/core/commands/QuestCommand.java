package com.mmo.core.commands;

import com.hypixel.hytale.server.command.Command;
import com.hypixel.hytale.server.command.CommandSender;
import com.hypixel.hytale.server.Player.Player;
import com.mmo.core.managers.GroupQuestManager;
import com.mmo.core.managers.QuestManager;

/**
 * Commande /quest pour gérer les quêtes solo et de groupe
 */
public class QuestCommand implements Command {

    private final QuestManager questManager;
    private final GroupQuestManager groupQuestManager;

    public QuestCommand(QuestManager questManager, GroupQuestManager groupQuestManager) {
        this.questManager = questManager;
        this.groupQuestManager = groupQuestManager;
    }

    @Override
    public String getName() {
        return "quest";
    }

    @Override
    public String getDescription() {
        return "Gère les quêtes solo et de groupe";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Commande réservée aux joueurs.");
            return;
        }

        Player Player = (Player) sender;

        if (args.length < 2) {
            showUsage(Player);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "solo":
                handleSoloQuest(Player, args);
                break;

            case "group":
                handleGroupQuest(Player, args);
                break;

            default:
                showUsage(Player);
                break;
        }
    }

    /**
     * Gère les commandes de quêtes solo
     */
    private void handleSoloQuest(Player Player, String[] args) {
        if (args.length < 3) {
            Player.sendMessage("/quest solo start <id> | solo complete <id> | solo abandon <id>");
            return;
        }

        String action = args[1].toLowerCase();
        String questId = args[2];

        switch (action) {
            case "start":
                questManager.startSoloQuest(Player.getPlayer(), questId);
                break;
            case "complete":
                questManager.completeSoloStage(Player.getPlayer(), questId);
                break;
            case "abandon":
                questManager.abandonQuest(Player.getPlayer(), questId);
                break;
            default:
                Player.sendMessage("/quest solo start <id> | solo complete <id> | solo abandon <id>");
                break;
        }
    }

    /**
     * Gère les commandes de quêtes de groupe
     */
    private void handleGroupQuest(Player Player, String[] args) {
        if (args.length < 3) {
            Player.sendMessage("/quest group start <id> | group complete <id> | group abandon <id>");
            return;
        }

        String action = args[1].toLowerCase();
        String questId = args[2];

        switch (action) {
            case "start":
                groupQuestManager.startGroupQuest(Player.getPlayer(), questId);
                break;
            case "complete":
                groupQuestManager.completeGroupStage(Player.getPlayer(), questId);
                break;
            case "abandon":
                groupQuestManager.abandonGroupQuest(Player.getPlayer(), questId);
                break;
            default:
                Player.sendMessage("/quest group start <id> | group complete <id> | group abandon <id>");
                break;
        }
    }

    /**
     * Affiche l'utilisation de la commande
     */
    private void showUsage(Player Player) {
        Player.sendMessage("=== Commandes de Quêtes ===");
        Player.sendMessage("/quest solo start <id> - Démarre une quête solo");
        Player.sendMessage("/quest solo complete <id> - Complète l'étape actuelle");
        Player.sendMessage("/quest solo abandon <id> - Abandonne la quête");
        Player.sendMessage("/quest group start <id> - Démarre une quête de groupe (leader seulement)");
        Player.sendMessage("/quest group complete <id> - Complète l'étape du groupe");
        Player.sendMessage("/quest group abandon <id> - Abandonne la quête de groupe (leader seulement)");
    }
}
