package com.mmo.core.model;

import java.util.*;

/**
 * État des quêtes pour un joueur
 */
public class PlayerQuestState {

    private final Player Player;
    
    // Index des étapes de quêtes actives
    private final Map<String, Integer> questStageIndex = new HashMap<>();
    
    // Quêtes complétées
    private final Set<String> completedQuests = new HashSet<>();
    
    // Progression des objectifs (questId -> objectiveKey -> count)
    private final Map<String, Map<String, Integer>> objectiveProgress = new HashMap<>();
    
    // Quête actuellement active
    private String activeQuestId;

    public PlayerQuestState(Player Player) {
        this.Player = Player;
    }

    /**
     * Récupère l'Player du joueur
     */
    public Player getPlayer() {
        return Player;
    }

    /**
     * Récupère l'index de l'étape actuelle d'une quête
     */
    public int getStageIndex(String questId) {
        return questStageIndex.getOrDefault(questId, 0);
    }

    /**
     * Définit l'index de l'étape d'une quête
     */
    public void setStageIndex(String questId, int index) {
        questStageIndex.put(questId, index);
        this.activeQuestId = questId;
    }

    /**
     * Marque une quête comme complétée
     */
    public void markQuestCompleted(String questId) {
        completedQuests.add(questId);
        questStageIndex.remove(questId);
        objectiveProgress.remove(questId);
        
        if (questId.equals(activeQuestId)) {
            activeQuestId = null;
        }
    }

    /**
     * Vérifie si une quête est complétée
     */
    public boolean hasCompletedQuest(String questId) {
        return completedQuests.contains(questId);
    }

    /**
     * Retire une quête (abandon)
     */
    public void removeQuest(String questId) {
        questStageIndex.remove(questId);
        objectiveProgress.remove(questId);
        
        if (questId.equals(activeQuestId)) {
            activeQuestId = null;
        }
    }

    /**
     * Récupère l'ID de la quête active
     */
    public String getActiveQuestId() {
        return activeQuestId;
    }

    /**
     * Incrémente la progression d'un objectif
     */
    public void incrementObjective(String questId, String objectiveKey, int amount) {
        Map<String, Integer> questObjectives = objectiveProgress.computeIfAbsent(
            questId, k -> new HashMap<>()
        );
        
        int current = questObjectives.getOrDefault(objectiveKey, 0);
        questObjectives.put(objectiveKey, current + amount);
    }

    /**
     * Récupère la progression d'un objectif
     */
    public int getObjectiveProgress(String questId, String objectiveKey) {
        Map<String, Integer> questObjectives = objectiveProgress.get(questId);
        if (questObjectives == null) return 0;
        return questObjectives.getOrDefault(objectiveKey, 0);
    }

    /**
     * Réinitialise la progression d'un objectif
     */
    public void resetObjective(String questId, String objectiveKey) {
        Map<String, Integer> questObjectives = objectiveProgress.get(questId);
        if (questObjectives != null) {
            questObjectives.remove(objectiveKey);
        }
    }

    /**
     * Récupère toutes les quêtes actives
     */
    public Set<String> getActiveQuests() {
        return new HashSet<>(questStageIndex.keySet());
    }

    /**
     * Récupère toutes les quêtes complétées
     */
    public Set<String> getCompletedQuests() {
        return new HashSet<>(completedQuests);
    }
}
