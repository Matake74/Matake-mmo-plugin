package com.mmo.core.managers;

import com.hytale.server.Player.Player;
import com.mmo.core.CorePlugin;
import com.mmo.core.model.hud.HudElement;
import com.mmo.core.model.hud.HudLayer;
import com.mmo.core.model.hud.HudPosition;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestionnaire du système HUD (interface utilisateur)
 * Intégré dans CorePlugin
 */
public class HudManager {

    private final CorePlugin plugin;

    // Stockage des éléments HUD par joueur et par layer
    private final Map<Player, Map<HudLayer, HudElement>> hudData;
    
    // Positions configurées pour chaque layer
    private final Map<HudLayer, HudPosition> positions;

    // Configuration
    private final int refreshTicks;
    private final boolean debugMode;

    public HudManager(CorePlugin plugin) {
        this.plugin = plugin;
        this.hudData = new ConcurrentHashMap<>();
        this.positions = new EnumMap<>(HudLayer.class);
        
        // Configuration par défaut
        this.refreshTicks = 5; // 5 ticks = 0.25 secondes
        this.debugMode = false;

        // Charger les positions par défaut
        loadDefaultPositions();
        
        plugin.getLogger().info("[HudManager] Initialisé avec succès.");
    }

    /**
     * Initialise le système HUD (appelé après le chargement de tous les plugins)
     */
    public void initialize() {
        startHudRefreshTask();
        plugin.getLogger().info("[HudManager] Système HUD démarré.");
    }

    // -----------------------------
    //  CONFIGURATION
    // -----------------------------

    private void loadDefaultPositions() {
        positions.put(HudLayer.PARTY, HudPosition.TOP_LEFT);
        positions.put(HudLayer.QUEST, HudPosition.TOP_RIGHT);
        positions.put(HudLayer.THREAT, HudPosition.CENTER_BOTTOM);
        positions.put(HudLayer.INSTANCE, HudPosition.TOP_RIGHT);
        positions.put(HudLayer.PROFESSION, HudPosition.BOTTOM_RIGHT);
        positions.put(HudLayer.BOSS_BAR, HudPosition.TOP_CENTER);
        positions.put(HudLayer.NOTIFICATION, HudPosition.BOTTOM_LEFT);
        positions.put(HudLayer.COMBAT_TEXT, HudPosition.CENTER);
    }

    // -----------------------------
    //  GESTION DES ÉLÉMENTS HUD
    // -----------------------------

    public void setHud(Player Player, HudLayer layer, HudElement element) {
        if (Player == null || layer == null || element == null) return;

        hudData.computeIfAbsent(Player.getPlayer(), k -> new EnumMap<>(HudLayer.class))
               .put(layer, element);

        if (debugMode) {
            plugin.getLogger().info("[HUD] Set " + layer + " for " + Player.getName());
        }
    }

    public void clearHud(Player Player, HudLayer layer) {
        if (Player == null || layer == null) return;

        Map<HudLayer, HudElement> PlayerHud = hudData.get(Player.getPlayer());
        if (PlayerHud != null) {
            PlayerHud.remove(layer);
        }
    }

    public void clearAllHud(Player Player) {
        if (Player == null) return;
        hudData.remove(Player.getPlayer());
    }

    public HudElement getHud(Player Player, HudLayer layer) {
        if (Player == null || layer == null) return null;
        
        Map<HudLayer, HudElement> PlayerHud = hudData.get(Player.getPlayer());
        return PlayerHud != null ? PlayerHud.get(layer) : null;
    }

    // -----------------------------
    //  HUD RAPIDES (HELPERS)
    // -----------------------------

    public void showTopMessage(Player Player, String message, int durationSeconds) {
        HudElement element = new HudElement(message, 0xFFFFFF, durationSeconds * 1000);
        setHud(Player, HudLayer.NOTIFICATION, element);

        // Planifier la suppression après la durée
        // TODO: Adapter selon l'API Scheduler de Hytale
        // plugin.getScheduler().runTaskLater(() -> {
        //     clearHud(Player, HudLayer.NOTIFICATION);
        // }, durationSeconds * 20L);
    }

    public void showCombatText(Player Player, String message) {
        HudElement element = new HudElement(message, 0xFF0000, 2000); // 2 secondes
        setHud(Player, HudLayer.COMBAT_TEXT, element);
    }

    public void showProgressBar(Player Player, String label, int current, int max) {
        String bar = createProgressBar(current, max, 20);
        String text = label + " " + bar + " " + current + "/" + max;
        HudElement element = new HudElement(text, 0x00FF00);
        setHud(Player, HudLayer.PROGRESS_BAR, element);
    }

    public void hideProgressBar(Player Player) {
        clearHud(Player, HudLayer.PROGRESS_BAR);
    }

    // -----------------------------
    //  HUD DE GROUPE
    // -----------------------------

    public void showPartyHud(Player Player) {
        // La mise à jour réelle est gérée par PartyHudRenderer
        // On marque juste que le HUD party doit être affiché
        setHud(Player, HudLayer.PARTY, new HudElement("Party HUD Active", 0x00FF00));
    }

    public void hidePartyHud(Player Player) {
        clearHud(Player, HudLayer.PARTY);
    }

    public void updatePartyHud(Player Player) {
        // Notifie que le HUD party doit être mis à jour
        // L'actualisation se fait dans la boucle de rendu
    }

    // -----------------------------
    //  HUD DE QUÊTE
    // -----------------------------

    public void showQuestObjective(Player Player) {
        setHud(Player, HudLayer.QUEST, new HudElement("Quest HUD Active", 0xFFFF00));
    }

    public void hideQuestObjective(Player Player) {
        clearHud(Player, HudLayer.QUEST);
    }

    public void updateQuestObjective(Player Player, String questId) {
        // TODO: Récupérer les détails de la quête et mettre à jour
    }

    // -----------------------------
    //  HUD DE BOSS
    // -----------------------------

    public void showBossBar(Player Player, String bossName, double currentHealth, double maxHealth) {
        String healthBar = createHealthBar(currentHealth, maxHealth, 30);
        String text = "§c§l" + bossName + "\n" + healthBar;
        HudElement element = new HudElement(text, 0xFF0000);
        setHud(Player, HudLayer.BOSS_BAR, element);
    }

    public void updateBossBar(Player Player, double currentHealth, double maxHealth) {
        HudElement current = getHud(Player, HudLayer.BOSS_BAR);
        if (current != null) {
            // Extraire le nom du boss de l'élément actuel
            String[] lines = current.getText().split("\n");
            if (lines.length > 0) {
                String bossName = lines[0].replace("§c§l", "");
                showBossBar(Player, bossName, currentHealth, maxHealth);
            }
        }
    }

    public void hideBossBar(Player Player) {
        clearHud(Player, HudLayer.BOSS_BAR);
    }

    // -----------------------------
    //  NOTIFICATIONS
    // -----------------------------

    public void showNotification(Player Player, String title, String message) {
        String text = "§e§l" + title + "\n§7" + message;
        HudElement element = new HudElement(text, 0xFFFFFF, 5000); // 5 secondes
        setHud(Player, HudLayer.NOTIFICATION, element);
    }

    public void showAchievement(Player Player, String achievement) {
        String text = "§6§l✦ Achievement Unlocked!\n§e" + achievement;
        HudElement element = new HudElement(text, 0xFFD700, 7000); // 7 secondes
        setHud(Player, HudLayer.NOTIFICATION, element);
    }

    // -----------------------------
    //  RENDU
    // -----------------------------

    private void startHudRefreshTask() {
        // TODO: Adapter selon l'API Scheduler de Hytale
        // plugin.getScheduler().runTaskTimer(() -> {
        //     for (Player Player : plugin.getServer().getOnlinePlayers()) {
        //         renderHud(Player);
        //     }
        // }, refreshTicks, refreshTicks);
    }

    private void renderHud(Player Player) {
        Map<HudLayer, HudElement> PlayerHud = hudData.get(Player.getPlayer());
        if (PlayerHud == null || PlayerHud.isEmpty()) return;

        // Nettoyer les éléments expirés
        PlayerHud.entrySet().removeIf(entry -> entry.getValue().isExpired());

        // Envoyer chaque élément au client
        for (Map.Entry<HudLayer, HudElement> entry : PlayerHud.entrySet()) {
            HudLayer layer = entry.getKey();
            HudElement element = entry.getValue();
            HudPosition position = positions.getOrDefault(layer, HudPosition.TOP_LEFT);

            // TODO: Adapter selon l'API HUD de Hytale
            // Player.sendHudText(position.name(), element.getText(), element.getColor());
        }
    }

    // -----------------------------
    //  UTILITAIRES
    // -----------------------------

    private String createProgressBar(int current, int max, int length) {
        if (max <= 0) return "§7[§c Error §7]";
        
        int filled = (int) ((double) current / max * length);
        StringBuilder bar = new StringBuilder("§7[");
        
        for (int i = 0; i < length; i++) {
            if (i < filled) {
                bar.append("§a■");
            } else {
                bar.append("§7■");
            }
        }
        
        bar.append("§7]");
        return bar.toString();
    }

    private String createHealthBar(double current, double max, int length) {
        if (max <= 0) return "§7[§c Error §7]";
        
        int filled = (int) ((current / max) * length);
        StringBuilder bar = new StringBuilder("§7[");
        
        for (int i = 0; i < length; i++) {
            if (i < filled) {
                bar.append("§c█");
            } else {
                bar.append("§8█");
            }
        }
        
        bar.append("§7]");
        
        // Ajouter le pourcentage
        int percent = (int) ((current / max) * 100);
        bar.append(" §f").append(percent).append("%");
        
        return bar.toString();
    }

    // -----------------------------
    //  ÉVÉNEMENTS
    // -----------------------------

    /**
     * Appelé quand un joueur se connecte
     */
    public void onPlayerJoin(Player Player) {
        // Initialiser le HUD du joueur
        hudData.put(Player.getPlayer(), new EnumMap<>(HudLayer.class));
    }

    /**
     * Appelé quand un joueur se déconnecte
     */
    public void onPlayerLeave(Player Player) {
        // Nettoyer les données HUD
        hudData.remove(Player.getPlayer());
    }

    /**
     * Arrêt propre du manager
     */
    public void shutdown() {
        hudData.clear();
        plugin.getLogger().info("[HudManager] Arrêté.");
    }
}
