package com.mmo.core.commands;

import com.mmo.core.managers.ProfessionManager;
import com.mmo.core.model.profession.ProfessionDef;
import com.mmo.core.model.profession.ProfessionType;
import java.util.Player;

/**
 * Commande pour gérer les professions
 * Note: La structure exacte dépend de l'API Command de Hytale
 */
public class ProfessionCommand {

    private final ProfessionManager professionManager;

    public ProfessionCommand(ProfessionManager professionManager) {
        this.professionManager = professionManager;
    }

    /**
     * Exécute la commande
     * TODO: Adapter selon l'API Command réelle de Hytale
     */
    public void execute(Player senderId, String[] args) {
        if (args.length == 0) {
            // Afficher toutes les professions du joueur
            sendMessage(senderId, "/metier | /metier info <nom> | /metier set <nom> <niveau>");
            sendMessage(senderId, "Métiers :");
            
            for (ProfessionType type : ProfessionType.values()) {
                ProfessionDef def = professionManager.getProfession(type);
                int lvl = professionManager.getLevel(senderId, type);
                double xp = professionManager.getXp(senderId, type);
                sendMessage(senderId, "- " + def.getName() + " : niveau " + lvl + " (" + (int)xp + " XP)");
            }
            return;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "info":
                handleInfo(senderId, args);
                break;
            case "set":
                handleSet(senderId, args);
                break;
            case "stats":
                handleStats(senderId, args);
                break;
            default:
                sendMessage(senderId, "Sous-commande inconnue: " + subCommand);
                sendMessage(senderId, "/metier | /metier info <nom> | /metier set <nom> <niveau>");
        }
    }

    /**
     * Affiche les informations détaillées d'une profession
     */
    private void handleInfo(Player senderId, String[] args) {
        if (args.length < 2) {
            sendMessage(senderId, "Usage: /metier info <nom>");
            return;
        }

        String professionName = args[1].toUpperCase();
        try {
            ProfessionType type = ProfessionType.valueOf(professionName);
            ProfessionDef def = professionManager.getProfession(type);
            
            int level = professionManager.getLevel(senderId, type);
            double xp = professionManager.getXp(senderId, type);
            int crafts = professionManager.getTotalCrafts(senderId, type);
            int gathers = professionManager.getTotalGathers(senderId, type);
            double gatherBonus = professionManager.getGatheringBonus(senderId, type);
            double craftBonus = professionManager.getCraftingQualityBonus(senderId, type);

            sendMessage(senderId, "=== " + def.getName() + " ===");
            sendMessage(senderId, "Niveau: " + level + "/" + def.getMaxLevel());
            sendMessage(senderId, "XP: " + (int)xp);
            sendMessage(senderId, "Crafts totaux: " + crafts);
            sendMessage(senderId, "Récoltes totales: " + gathers);
            sendMessage(senderId, "Bonus récolte: +" + (int)((gatherBonus - 1.0) * 100) + "%");
            sendMessage(senderId, "Bonus qualité: +" + (int)((craftBonus - 1.0) * 100) + "%");
            
        } catch (IllegalArgumentException e) {
            sendMessage(senderId, "Profession introuvable: " + professionName);
            sendMessage(senderId, "Professions disponibles: " + String.join(", ", 
                java.util.Arrays.stream(ProfessionType.values())
                    .map(Enum::name)
                    .toArray(String[]::new)));
        }
    }

    /**
     * Définit le niveau d'une profession (admin)
     */
    private void handleSet(Player senderId, String[] args) {
        if (args.length < 3) {
            sendMessage(senderId, "Usage: /metier set <nom> <niveau>");
            return;
        }

        // TODO: Vérifier les permissions admin

        String professionName = args[1].toUpperCase();
        try {
            ProfessionType type = ProfessionType.valueOf(professionName);
            int level = Integer.parseInt(args[2]);

            ProfessionDef def = professionManager.getProfession(type);
            if (level < 1 || level > def.getMaxLevel()) {
                sendMessage(senderId, "Niveau invalide. Doit être entre 1 et " + def.getMaxLevel());
                return;
            }

            professionManager.setLevel(senderId, type, level);
            sendMessage(senderId, "Votre niveau en " + def.getName() + " a été défini à " + level);
            
        } catch (IllegalArgumentException e) {
            sendMessage(senderId, "Profession ou niveau invalide");
        }
    }

    /**
     * Affiche les statistiques globales
     */
    private void handleStats(Player senderId, String[] args) {
        sendMessage(senderId, "=== Statistiques de Professions ===");
        
        int totalCrafts = 0;
        int totalGathers = 0;
        int totalLevels = 0;

        for (ProfessionType type : ProfessionType.values()) {
            totalCrafts += professionManager.getTotalCrafts(senderId, type);
            totalGathers += professionManager.getTotalGathers(senderId, type);
            totalLevels += professionManager.getLevel(senderId, type);
        }

        sendMessage(senderId, "Crafts totaux: " + totalCrafts);
        sendMessage(senderId, "Récoltes totales: " + totalGathers);
        sendMessage(senderId, "Niveaux cumulés: " + totalLevels);
    }

    /**
     * Envoie un message au joueur
     * TODO: Adapter selon l'API Player/Message de Hytale
     */
    private void sendMessage(Player Player, String message) {
        // TODO: Récupérer le Player depuis l'Player et envoyer le message
        // Player Player = server.getPlayerManager().getPlayer(Player);
        // if (Player != null) {
        //     Player.sendMessage(message);
        // }
    }
}
