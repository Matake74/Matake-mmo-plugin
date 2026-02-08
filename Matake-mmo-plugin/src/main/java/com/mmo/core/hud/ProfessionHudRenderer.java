package com.mmo.core.hud;

import com.hytale.server.Player.Player;
import com.mmo.core.api.HudAPI;
import com.mmo.core.model.hud.HudElement;
import com.mmo.core.model.hud.HudLayer;

/**
 * Renderer pour afficher les informations de profession dans le HUD
 * Compatible avec HudAPI et le système de professions
 */
public class ProfessionHudRenderer {

    /**
     * Met à jour l'affichage des professions pour un joueur
     */
    public static void update(Player Player) {
        // TODO: Récupérer la profession active via ProfessionAPI
        // ProfessionType type = ProfessionAPI.getActiveProfession(Player);
        // if (type == null) {
        //     HudAPI.clearHud(Player, HudLayer.PROFESSION);
        //     return;
        // }

        // Exemple de données (à remplacer par les vraies)
        String professionName = "Mineur"; // Placeholder
        int level = 15; // Placeholder
        double xp = 750.0; // Placeholder
        double xpToNext = 1000.0; // Placeholder

        StringBuilder display = new StringBuilder();
        display.append("§e§l").append(professionName).append("\n");
        display.append("§7Niveau §f").append(level).append("\n");
        
        // Barre de progression XP
        String xpBar = createXpBar(xp, xpToNext, 15);
        display.append(xpBar).append("\n");
        display.append("§7").append((int)xp).append(" / ").append((int)xpToNext).append(" XP");

        HudElement element = new HudElement(display.toString(), 0xFFD700);
        HudAPI.setHud(Player, HudLayer.PROFESSION, element);
    }

    /**
     * Affiche un gain d'XP temporaire
     */
    public static void showXpGain(Player Player, String professionName, double xpGained) {
        String message = "§e+" + (int)xpGained + " XP §7(" + professionName + ")";
        HudAPI.showCombatText(Player, message);
    }

    /**
     * Affiche une notification de montée de niveau
     */
    public static void showLevelUp(Player Player, String professionName, int newLevel) {
        String title = "§6§lMontée de Niveau!";
        String message = "§e" + professionName + " §7est maintenant niveau §f" + newLevel;
        HudAPI.showNotification(Player, title, message);
    }

    /**
     * Affiche l'état du crafting en cours
     */
    public static void showCraftingProgress(Player Player, String itemName, int current, int total) {
        String label = "§eCrafting: §f" + itemName;
        HudAPI.showProgressBar(Player, label, current, total);
    }

    /**
     * Cache la barre de progression du crafting
     */
    public static void hideCraftingProgress(Player Player) {
        HudAPI.hideProgressBar(Player);
    }

    /**
     * Affiche les bonus actifs de profession
     */
    public static void showProfessionBonuses(Player Player) {
        // TODO: Récupérer les bonus via ProfessionAPI
        StringBuilder display = new StringBuilder();
        display.append("§e§lBonus Actifs\n");
        display.append("§7• §aVitesse de minage +15%\n");
        display.append("§7• §aTaux de drop rare +5%\n");
        display.append("§7• §aDouble drop 10%");

        HudElement element = new HudElement(display.toString(), 0xFFFFFF, 5000); // 5 secondes
        HudAPI.setHud(Player, HudLayer.CUSTOM_2, element);
    }

    /**
     * Affiche une recette débloquée
     */
    public static void showRecipeUnlocked(Player Player, String recipeName) {
        String title = "§e§lRecette Débloquée!";
        String message = "§fVous pouvez maintenant créer: §e" + recipeName;
        HudAPI.showNotification(Player, title, message);
    }

    /**
     * Masque le HUD de profession
     */
    public static void hide(Player Player) {
        HudAPI.clearHud(Player, HudLayer.PROFESSION);
    }

    // -----------------------------
    //  UTILITAIRES PRIVÉS
    // -----------------------------

    /**
     * Crée une barre de progression XP
     */
    private static String createXpBar(double current, double max, int length) {
        if (max <= 0) return "§7[§c Error §7]";
        
        int filled = (int) ((current / max) * length);
        StringBuilder bar = new StringBuilder("§7[");
        
        for (int i = 0; i < length; i++) {
            if (i < filled) {
                bar.append("§e■");
            } else {
                bar.append("§8■");
            }
        }
        
        bar.append("§7]");
        
        // Ajouter le pourcentage
        int percent = (int) ((current / max) * 100);
        bar.append(" §f").append(percent).append("%");
        
        return bar.toString();
    }

    /**
     * Formate l'XP pour affichage
     */
    private static String formatXp(double xp) {
        if (xp >= 1000000) {
            return String.format("%.2fM", xp / 1000000);
        } else if (xp >= 1000) {
            return String.format("%.1fk", xp / 1000);
        } else {
            return String.format("%.0f", xp);
        }
    }
}
