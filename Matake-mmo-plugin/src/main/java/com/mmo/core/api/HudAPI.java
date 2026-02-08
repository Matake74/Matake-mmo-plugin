package com.mmo.core.api;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.player.Player;
import com.mmo.core.CorePlugin;
import com.mmo.core.managers.HudManager;
import com.mmo.core.model.hud.*;
import com.mmo.core.model.hud.HudLayer;
import java.util.PlayerRef;

/**
 * API publique pour gérer l'affichage HUD (interface utilisateur)
 * Utilise des Player au lieu de Player objects
 */
public class HudAPI {

    // -----------------------------
    //  GESTION DES ÉLÉMENTS HUD
    // -----------------------------

    /**
     * Définit un élément HUD sur un layer spécifique pour un joueur
     * @param Player L'Player du joueur
     * @param layer Le layer HUD (TOP, MIDDLE, BOTTOM, etc.)
     * @param element L'élément à afficher
     */
    public static void setHud(PlayerRef Player, HudLayer layer, HudElement element) {
        HudManager hm = getManager();
        if (hm != null) {
            hm.setHud(Player, layer, element);
        }
    }

    /**
     * Efface un layer HUD pour un joueur
     * @param Player L'Player du joueur
     * @param layer Le layer à effacer
     */
    public static void clearHud(PlayerRef Player, HudLayer layer) {R
        HudManager hm = getManager();
        if (hm != null) {
            hm.clearHud(Player, layer);
        }
    }

    /**
     * Efface tous les layers HUD pour un joueur
     * @param Player L'Player du joueur
     */
    public static void clearAllHud(PlayerRef Player) {
        HudManager hm = getManager();
        if (hm != null) {
            hm.clearAllHud(Player);
        }
    }

    /**
     * Récupère l'élément HUD actuel d'un layer
     * @param Player L'Player du joueur
     * @param layer Le layer
     * @return L'élément HUD ou null
     */
    public static HudElement getHud(PlayerRef Player, HudLayer layer) {
        HudManager hm = getManager();
        return hm != null ? hm.getHud(Player, layer) : null;
    }

    // -----------------------------
    //  HUD RAPIDES (HELPERS)
    // -----------------------------

    /**
     * Affiche un message temporaire en haut de l'écran
     * @param Player L'Player du joueur
     * @param message Le message à afficher
     * @param durationSeconds Durée d'affichage en secondes
     */
    public static void showTopMessage(PlayerRef Player, String message, int durationSeconds) {
        HudManager hm = getManager();
        if (hm != null) {
            hm.showTopMessage(Player, message, durationSeconds);
        }
    }

    /**
     * Affiche un message de combat (dégâts, soins, etc.)
     * @param Player L'Player du joueur
     * @param message Le message de combat
     */
    public static void showCombatText(PlayerRef Player, String message) {
        HudManager hm = getManager();
        if (hm != null) {
            hm.showCombatText(Player, message);
        }
    }

    /**
     * Affiche une barre de progression (quête, chargement, etc.)
     * @param Player L'Player du joueur
     * @param label Le label de la barre
     * @param current Valeur actuelle
     * @param max Valeur maximale
     */
    public static void showProgressBar(PlayerRef Player, String label, int current, int max) {
        HudManager hm = getManager();
        if (hm != null) {
            hm.showProgressBar(Player, label, current, max);
        }
    }

    /**
     * Cache la barre de progression
     * @param Player L'Player du joueur
     */
    public static void hideProgressBar(PlayerRef Player) {
        HudManager hm = getManager();
        if (hm != null) {
            hm.hideProgressBar(Player);
        }
    }

    // -----------------------------
    //  HUD DE GROUPE
    // -----------------------------

    /**
     * Affiche les informations du groupe (membres, santé, etc.)
     * @param Player L'Player du joueur
     */
    public static void showPartyHud(PlayerRef Player) {
        HudManager hm = getManager();
        if (hm != null) {
            hm.showPartyHud(Player);
        }
    }

    /**
     * Cache les informations du groupe
     * @param Player L'Player du joueur
     */
    public static void hidePartyHud(PlayerRef Player) {
        HudManager hm = getManager();
        if (hm != null) {
            hm.hidePartyHud(Player);
        }
    }

    /**
     * Met à jour les informations du groupe
     * @param Player L'Player du joueur
     */
    public static void updatePartyHud(PlayerRef Player) {
        HudManager hm = getManager();
        if (hm != null) {
            hm.updatePartyHud(Player);
        }
    }

    // -----------------------------
    //  HUD DE QUÊTE
    // -----------------------------

    /**
     * Affiche l'objectif de quête actuel
     * @param Player L'Player du joueur
     */
    public static void showQuestObjective(PlayerRef Player) {
        HudManager hm = getManager();
        if (hm != null) {
            hm.showQuestObjective(Player);
        }
    }

    /**
     * Cache l'objectif de quête
     * @param Player L'Player du joueur
     */
    public static void hideQuestObjective(PlayerRef Player) {
        HudManager hm = getManager();
        if (hm != null) {
            hm.hideQuestObjective(Player);
        }
    }

    /**
     * Met à jour l'objectif de quête
     * @param Player L'Player du joueur
     * @param questId L'identifiant de la quête
     */
    public static void updateQuestObjective(PlayerRef Player, String questId) {
        HudManager hm = getManager();
        if (hm != null) {
            hm.updateQuestObjective(Player, questId);
        }
    }

    // -----------------------------
    //  HUD DE BOSS
    // -----------------------------

    /**
     * Affiche la barre de santé d'un boss
     * @param Player L'Player du joueur
     * @param bossName Le nom du boss
     * @param currentHealth Santé actuelle
     * @param maxHealth Santé maximale
     */
    public static void showBossBar(PlayerRef Player, String bossName, double currentHealth, double maxHealth) {
        HudManager hm = getManager();
        if (hm != null) {
            hm.showBossBar(Player, bossName, currentHealth, maxHealth);
        }
    }

    /**
     * Met à jour la barre de santé du boss
     * @param Player L'Player du joueur
     * @param currentHealth Santé actuelle
     * @param maxHealth Santé maximale
     */
    public static void updateBossBar(PlayerRef Player, double currentHealth, double maxHealth) {
        HudManager hm = getManager();
        if (hm != null) {
            hm.updateBossBar(Player, currentHealth, maxHealth);
        }
    }

    /**
     * Cache la barre de santé du boss
     * @param Player L'Player du joueur
     */
    public static void hideBossBar(PlayerRef Player) {
        HudManager hm = getManager();
        if (hm != null) {
            hm.hideBossBar(Player);
        }
    }

    // -----------------------------
    //  NOTIFICATIONS
    // -----------------------------

    /**
     * Affiche une notification dans un coin de l'écran
     * @param Player L'Player du joueur
     * @param title Le titre de la notification
     * @param message Le message
     */
    public static void showNotification(PlayerRef Player, String title, String message) {
        HudManager hm = getManager();
        if (hm != null) {
            hm.showNotification(Player, title, message);
        }
    }

    /**
     * Affiche une notification de succès/achievement
     * @param Player L'Player du joueur
     * @param achievement Le nom du succès
     */
    public static void showAchievement(PlayerRef Player, String achievement) {
        HudManager hm = getManager();
        if (hm != null) {
            hm.showAchievement(Player, achievement);
        }
    }

    // -----------------------------
    //  HELPER
    // -----------------------------

    private static HudManager getManager() {
        CorePlugin plugin = CorePlugin.getInstance();
        return plugin != null ? plugin.getHudManager() : null;
    }
}
