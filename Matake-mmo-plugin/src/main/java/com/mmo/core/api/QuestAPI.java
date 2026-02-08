package com.mmo.core.api;

import com.mmo.core.CorePlugin;
import com.mmo.core.managers.GroupQuestManager;
import com.mmo.core.managers.QuestManager;

import java.util.Player;

/**
 * API publique pour gérer les quêtes solo et de groupe
 * Utilise des Player pour identifier les joueurs
 */
public class QuestAPI {

    // -----------------------------
    //  QUÊTES SOLO
    // -----------------------------

    /**
     * Démarre une quête solo pour un joueur
     * @param Player L'Player du joueur qui démarre la quête
     * @param questId L'identifiant de la quête
     */
    public static void startSoloQuest(Player Player, String questId) {
        QuestManager qm = getQuestManager();
        if (qm != null) {
            qm.startSoloQuest(Player, questId);
        }
    }

    /**
     * Complète l'étape actuelle d'une quête solo
     * @param Player L'Player du joueur
     * @param questId L'identifiant de la quête
     */
    public static void completeSoloStage(Player Player, String questId) {
        QuestManager qm = getQuestManager();
        if (qm != null) {
            qm.completeSoloStage(Player, questId);
        }
    }

    /**
     * Abandonne une quête solo
     * @param Player L'Player du joueur
     * @param questId L'identifiant de la quête
     */
    public static void abandonSoloQuest(Player Player, String questId) {
        QuestManager qm = getQuestManager();
        if (qm != null) {
            qm.abandonQuest(Player, questId);
        }
    }

    /**
     * Vérifie si un joueur a complété une quête
     * @param Player L'Player du joueur
     * @param questId L'identifiant de la quête
     * @return true si la quête est complétée
     */
    public static boolean hasCompletedQuest(Player Player, String questId) {
        QuestManager qm = getQuestManager();
        if (qm != null) {
            return qm.hasCompletedQuest(Player, questId);
        }
        return false;
    }

    /**
     * Récupère l'objectif actuel d'un joueur
     * @param Player L'Player du joueur
     * @return La description de l'objectif actuel ou null
     */
    public static String getCurrentObjective(Player Player) {
        QuestManager qm = getQuestManager();
        if (qm != null) {
            return qm.getCurrentObjective(Player);
        }
        return null;
    }

    // -----------------------------
    //  QUÊTES DE GROUPE
    // -----------------------------

    /**
     * Démarre une quête de groupe (le leader doit être dans un groupe)
     * @param leaderId L'Player du leader du groupe
     * @param questId L'identifiant de la quête
     */
    public static void startGroupQuest(Player leaderId, String questId) {
        GroupQuestManager gm = getGroupQuestManager();
        if (gm != null) {
            gm.startGroupQuest(leaderId, questId);
        }
    }

    /**
     * Complète l'étape actuelle d'une quête de groupe
     * @param Player L'Player d'un membre du groupe
     * @param questId L'identifiant de la quête
     */
    public static void completeGroupStage(Player Player, String questId) {
        GroupQuestManager gm = getGroupQuestManager();
        if (gm != null) {
            gm.completeGroupStage(Player, questId);
        }
    }

    /**
     * Abandonne une quête de groupe (seul le leader peut abandonner)
     * @param leaderId L'Player du leader du groupe
     * @param questId L'identifiant de la quête
     */
    public static void abandonGroupQuest(Player leaderId, String questId) {
        GroupQuestManager gm = getGroupQuestManager();
        if (gm != null) {
            gm.abandonGroupQuest(leaderId, questId);
        }
    }

    /**
     * Vérifie si un groupe a une quête active
     * @param Player L'Player d'un membre du groupe
     * @param questId L'identifiant de la quête
     * @return true si le groupe a cette quête active
     */
    public static boolean hasActiveGroupQuest(Player Player, String questId) {
        GroupQuestManager gm = getGroupQuestManager();
        if (gm != null) {
            return gm.hasActiveGroupQuest(Player, questId);
        }
        return false;
    }

    // -----------------------------
    //  PROGRESSION
    // -----------------------------

    /**
     * Incrémente un compteur d'objectif pour une quête
     * Ex: tuer 10 mobs, collecter 5 items
     * @param Player L'Player du joueur
     * @param questId L'identifiant de la quête
     * @param objectiveKey La clé de l'objectif
     * @param amount Montant à incrémenter
     */
    public static void incrementObjective(Player Player, String questId, String objectiveKey, int amount) {
        QuestManager qm = getQuestManager();
        if (qm != null) {
            qm.incrementObjective(Player, questId, objectiveKey, amount);
        }
    }

    /**
     * Vérifie la progression d'un objectif spécifique
     * @param Player L'Player du joueur
     * @param questId L'identifiant de la quête
     * @param objectiveKey La clé de l'objectif
     * @return La progression actuelle
     */
    public static int getObjectiveProgress(Player Player, String questId, String objectiveKey) {
        QuestManager qm = getQuestManager();
        if (qm != null) {
            return qm.getObjectiveProgress(Player, questId, objectiveKey);
        }
        return 0;
    }

    // -----------------------------
    //  HELPERS
    // -----------------------------

    private static QuestManager getQuestManager() {
        CorePlugin plugin = CorePlugin.getInstance();
        return plugin != null ? plugin.getQuestManager() : null;
    }

    private static GroupQuestManager getGroupQuestManager() {
        CorePlugin plugin = CorePlugin.getInstance();
        return plugin != null ? plugin.getGroupQuestManager() : null;
    }
}
