package com.mmo.core.managers;

import com.hypixel.hytale.server.Server;
import com.hypixel.hytale.server.Player.Player;
import com.mmo.core.CorePlugin;
import com.mmo.core.model.PlayerQuestState;
import com.mmo.core.model.QuestDef;
import com.mmo.core.model.QuestStageDef;

import java.util.*;

/**
 * Gestionnaire de quêtes individuelles
 */
public class QuestManager {

    private final CorePlugin plugin;
    private final WaypointManager waypointManager;

    private final Map<String, QuestDef> quests = new HashMap<>();
    private final Map<Player, PlayerQuestState> PlayerStates = new HashMap<>();

    public QuestManager(CorePlugin plugin, WaypointManager waypointManager) {
        this.plugin = plugin;
        this.waypointManager = waypointManager;
        loadQuests();
    }

    /**
     * Charge les quêtes depuis le fichier de configuration
     */
    private void loadQuests() {
        try {
            var cfg = getServer().getConfigManager().getConfig(plugin, "config/quests.yml");
            var section = cfg.getSection("quests");
            if (section == null) {
                plugin.getLogger().warn("[QuestManager] Aucune section 'quests' trouvée dans quests.yml");
                return;
            }

            for (String id : section.getKeys()) {
                var qSec = section.getSection(id);
                String name = qSec.getString("name");
                String desc = qSec.getString("description");
                boolean coop = qSec.getBoolean("coop", false);

                List<QuestStageDef> stages = new ArrayList<>();
                for (Object o : qSec.getList("stages")) {
                    var m = (Map<String, Object>) o;
                    String sid = (String) m.get("id");
                    String type = (String) m.get("type");
                    String npcId = (String) m.getOrDefault("npcId", null);
                    String waypointId = (String) m.getOrDefault("waypoint", null);
                    String bossId = (String) m.getOrDefault("bossId", null);
                    stages.add(new QuestStageDef(sid, type, npcId, waypointId, bossId));
                }

                quests.put(id, new QuestDef(id, name, desc, coop, stages));
            }
            
            plugin.getLogger().info("[QuestManager] " + quests.size() + " quêtes chargées.");
        } catch (Exception e) {
            plugin.getLogger().error("[QuestManager] Erreur lors du chargement des quêtes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sauvegarde les données avant l'arrêt
     */
    public void shutdown() {
        // TODO: Sauvegarder les états des joueurs dans une base de données ou fichier
        plugin.getLogger().info("[QuestManager] Arrêt et sauvegarde des données.");
    }

    /**
     * Récupère l'état d'un joueur
     */
    private PlayerQuestState getState(Player Player) {
        return PlayerStates.computeIfAbsent(Player, PlayerQuestState::new);
    }

    /**
     * Récupère une définition de quête
     */
    public QuestDef getQuest(String id) {
        return quests.get(id);
    }

    /**
     * Démarre une quête solo pour un joueur
     */
    public void startSoloQuest(Player Player, String questId) {
        QuestDef def = quests.get(questId);
        if (def == null) {
            sendMessage(Player, "[Quête] Quête introuvable.");
            return;
        }
        if (def.isCoop()) {
            sendMessage(Player, "[Quête] Cette quête est prévue pour un groupe.");
            return;
        }

        PlayerQuestState state = getState(Player);
        state.setStageIndex(questId, 0);

        sendMessage(Player, "[Quête] Quête commencée : " + def.getName());
        updateStage(Player, def, 0);
    }

    /**
     * Complète l'étape actuelle d'une quête
     */
    public void completeSoloStage(Player Player, String questId) {
        QuestDef def = quests.get(questId);
        if (def == null) return;

        PlayerQuestState state = getState(Player);
        int current = state.getStageIndex(questId);
        int next = current + 1;

        if (next >= def.getStages().size()) {
            sendMessage(Player, "[Quête] Quête terminée : " + def.getName());
            waypointManager.clearWaypoint(Player);
            state.markQuestCompleted(questId);
            return;
        }

        state.setStageIndex(questId, next);
        updateStage(Player, def, next);
    }

    /**
     * Abandonne une quête
     */
    public void abandonQuest(Player Player, String questId) {
        PlayerQuestState state = PlayerStates.get(Player);
        if (state == null) return;

        state.removeQuest(questId);
        waypointManager.clearWaypoint(Player);
        sendMessage(Player, "[Quête] Quête abandonnée.");
    }

    /**
     * Vérifie si un joueur a complété une quête
     */
    public boolean hasCompletedQuest(Player Player, String questId) {
        PlayerQuestState state = PlayerStates.get(Player);
        return state != null && state.hasCompletedQuest(questId);
    }

    /**
     * Récupère l'objectif actuel d'un joueur
     */
    public String getCurrentObjective(Player Player) {
        PlayerQuestState state = PlayerStates.get(Player);
        if (state == null) return null;

        // Récupérer la quête active
        String activeQuestId = state.getActiveQuestId();
        if (activeQuestId == null) return null;

        QuestDef def = quests.get(activeQuestId);
        if (def == null) return null;

        int stageIndex = state.getStageIndex(activeQuestId);
        if (stageIndex >= 0 && stageIndex < def.getStages().size()) {
            QuestStageDef stage = def.getStages().get(stageIndex);
            return stage.getId();
        }

        return null;
    }

    /**
     * Incrémente un compteur d'objectif
     */
    public void incrementObjective(Player Player, String questId, String objectiveKey, int amount) {
        PlayerQuestState state = getState(Player);
        state.incrementObjective(questId, objectiveKey, amount);
        
        // Vérifier si l'objectif est atteint
        // TODO: Implémenter la logique de vérification selon le type d'objectif
    }

    /**
     * Récupère la progression d'un objectif
     */
    public int getObjectiveProgress(Player Player, String questId, String objectiveKey) {
        PlayerQuestState state = PlayerStates.get(Player);
        if (state == null) return 0;
        return state.getObjectiveProgress(questId, objectiveKey);
    }

    /**
     * Met à jour l'étape pour un joueur
     */
    private void updateStage(Player Player, QuestDef def, int index) {
        QuestStageDef stage = def.getStages().get(index);
        sendMessage(Player, "[Quête] Objectif : " + stage.getId());

        // Mettre à jour le HUD si disponible
        if (plugin.getHudManager() != null) {
            plugin.getHudManager().updateQuestHud(Player, "Objectif : " + stage.getId());
        }

        // Envoyer le waypoint si défini
        if (stage.getWaypointId() != null) {
            waypointManager.sendWaypoint(Player, stage.getWaypointId());
        }
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
