package com.mmo.core.hud;

import com.hytale.server.Player.Player;
import com.mmo.core.api.PartyAPI;
import com.mmo.core.api.HudAPI;
import com.mmo.core.model.party.Party;
import com.mmo.core.model.hud.HudElement;
import com.mmo.core.model.hud.HudLayer;

/**
 * Renderer pour afficher les informations du groupe dans le HUD
 * Compatible avec PartyAPI et HudAPI
 */
public class PartyHudRenderer {

    /**
     * Met à jour l'affichage du groupe pour un joueur
     * Affiche les membres et leurs statistiques vitales
     */
    public static void update(Player Player) {
        Party party = PartyAPI.getParty(Player);
        
        // Si le joueur n'est pas dans un groupe, masquer le HUD
        if (party == null) {
            HudAPI.clearHud(Player, HudLayer.PARTY);
            return;
        }

        // Construire l'affichage
        StringBuilder display = new StringBuilder();
        display.append("§e§l=== Groupe ===\n");
        display.append("§7").append(party.getSize()).append("/").append(party.getMaxSize()).append("\n\n");

        // Afficher chaque membre
        for (Player member : party.getMembers()) {
            // Icône de leader
            String icon = party.isLeader(member) ? "§6★ " : "§7• ";
            
            // Statut en ligne/hors ligne
            String status = member.isOnline() ? "§a●" : "§c●";
            
            // Nom du joueur
            String name = member.getName();
            if (member.equals(Player)) {
                name = "§b" + name; // Mettre en évidence le joueur actuel
            } else {
                name = "§f" + name;
            }

            // Barre de vie (si en ligne)
            String healthBar = "";
            if (member.isOnline()) {
                // TODO: Récupérer la santé réelle du joueur
                double health = 100.0; // Placeholder
                double maxHealth = 100.0; // Placeholder
                healthBar = " " + createHealthBar(health, maxHealth, 10);
            }

            display.append(icon).append(status).append(" ").append(name).append(healthBar).append("\n");
        }

        // Créer et envoyer l'élément HUD
        HudElement element = new HudElement(display.toString(), 0xFFFFFF);
        HudAPI.setHud(Player, HudLayer.PARTY, element);
    }

    /**
     * Affiche les informations détaillées d'un membre
     * Utile pour le survol de souris ou ciblage
     */
    public static void updateMemberDetails(Player Player, Player target) {
        if (!PartyAPI.isInParty(Player)) return;
        
        Party party = PartyAPI.getParty(Player);
        if (party == null || !party.isMember(target)) return;

        StringBuilder display = new StringBuilder();
        display.append("§e").append(target.getName()).append("\n");
        
        // TODO: Récupérer les vraies stats
        double health = 100.0;
        double maxHealth = 100.0;
        double mana = 50.0;
        double maxMana = 100.0;
        
        display.append("§c❤ ").append((int)health).append("/").append((int)maxHealth).append("\n");
        display.append("§b✦ ").append((int)mana).append("/").append((int)maxMana).append("\n");
        
        // Afficher les buffs actifs (exemple)
        display.append("§7Buffs: §aShield, §eHaste");

        HudElement element = new HudElement(display.toString(), 0xFFFFFF, 3000); // 3 secondes
        HudAPI.setHud(Player, HudLayer.CUSTOM_1, element);
    }

    /**
     * Masque le HUD du groupe
     */
    public static void hide(Player Player) {
        HudAPI.clearHud(Player, HudLayer.PARTY);
    }

    /**
     * Affiche une notification de groupe
     */
    public static void showPartyNotification(Player Player, String message) {
        HudAPI.showNotification(Player, "Groupe", message);
    }

    /**
     * Affiche un avertissement au groupe (par exemple, joueur faible)
     */
    public static void showPartyWarning(Player Player, String warning) {
        String text = "§c§l⚠ ATTENTION\n§e" + warning;
        HudElement element = new HudElement(text, 0xFF0000, 4000); // 4 secondes
        HudAPI.setHud(Player, HudLayer.NOTIFICATION, element);
    }

    // -----------------------------
    //  UTILITAIRES PRIVÉS
    // -----------------------------

    /**
     * Crée une barre de vie visuelle
     */
    private static String createHealthBar(double current, double max, int length) {
        if (max <= 0) return "";
        
        int filled = (int) ((current / max) * length);
        StringBuilder bar = new StringBuilder("§7[");
        
        for (int i = 0; i < length; i++) {
            if (i < filled) {
                // Couleur basée sur le pourcentage
                double percent = current / max;
                if (percent > 0.6) {
                    bar.append("§a■");
                } else if (percent > 0.3) {
                    bar.append("§e■");
                } else {
                    bar.append("§c■");
                }
            } else {
                bar.append("§8■");
            }
        }
        
        bar.append("§7]");
        return bar.toString();
    }

    /**
     * Formate un nombre avec séparateurs
     */
    private static String formatNumber(double number) {
        if (number >= 1000000) {
            return String.format("%.1fM", number / 1000000);
        } else if (number >= 1000) {
            return String.format("%.1fk", number / 1000);
        } else {
            return String.format("%.0f", number);
        }
    }
}
