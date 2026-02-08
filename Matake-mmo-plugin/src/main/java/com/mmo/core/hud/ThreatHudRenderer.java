package com.mmo.core.hud;

import com.hytale.server.entity.LivingEntity;
import com.hytale.server.Player.Player;
import com.mmo.core.api.ThreatAPI;

/**
 * Renderer pour afficher les informations de menace dans le HUD
 * Compatible avec ThreatAPI et le système HUD de CorePlugin
 */
public class ThreatHudRenderer {

    /**
     * Met à jour l'affichage de la menace pour un joueur
     * Affiche la menace totale du joueur sur sa cible actuelle
     */
    public static void updateThreatDisplay(Player Player) {
        // Récupérer la cible actuelle du joueur
        if (Player.getTarget() == null) {
            clearThreatDisplay(Player);
            return;
        }

        // Vérifier que la cible est un LivingEntity
        if (!(Player.getTarget() instanceof LivingEntity)) {
            clearThreatDisplay(Player);
            return;
        }

        LivingEntity mob = (LivingEntity) Player.getTarget();
        
        // Récupérer la menace via l'API
        double threat = ThreatAPI.getThreat(mob, Player);
        
        // Si pas de menace, ne rien afficher
        if (threat <= 0) {
            clearThreatDisplay(Player);
            return;
        }

        // Récupérer le joueur avec le plus de menace
        Player topThreat = ThreatAPI.getTopThreat(mob);
        boolean hasAggro = topThreat != null && topThreat.equals(Player);

        // Formater l'affichage
        String displayText = formatThreatDisplay(threat, hasAggro);
        int color = hasAggro ? 0xFF0000 : 0xFFAA00; // Rouge si aggro, orange sinon

        // Envoyer au HUD
        // TODO: Adapter selon l'API HUD de Hytale
        // Player.sendHud(HudLayer.THREAT, new HudElement(displayText, color));
        
        Player.sendMessage(displayText); // Temporaire pour les tests
    }

    /**
     * Affiche la menace de tous les joueurs d'un groupe sur un mob
     * Utile pour les tanks qui veulent surveiller la menace du groupe
     */
    public static void updateGroupThreatDisplay(Player Player, LivingEntity mob) {
        // TODO: Récupérer les membres du groupe via PartyManager
        // Party party = CorePlugin.getInstance().getPartyManager().getParty(Player);
        // if (party == null) return;

        // StringBuilder display = new StringBuilder("Menaces:\n");
        // for (Player member : party.getMembers()) {
        //     double threat = ThreatAPI.getThreat(mob, member);
        //     if (threat > 0) {
        //         display.append(String.format("  %s: %d\n", member.getName(), (int)threat));
        //     }
        // }

        // Player.sendHud(HudLayer.THREAT_GROUP, new HudElement(display.toString(), 0xFFFFFF));
    }

    /**
     * Affiche un indicateur d'aggro (vous avez l'attention du mob)
     */
    public static void showAggroIndicator(Player Player, boolean hasAggro) {
        if (hasAggro) {
            // TODO: Afficher un indicateur visuel d'aggro
            // Player.sendHud(HudLayer.AGGRO_INDICATOR, new HudElement("⚠ AGGRO", 0xFF0000));
            Player.sendMessage("§c⚠ AGGRO");
        } else {
            // clearAggroIndicator(Player);
        }
    }

    /**
     * Efface l'affichage de menace
     */
    private static void clearThreatDisplay(Player Player) {
        // TODO: Adapter selon l'API HUD de Hytale
        // Player.clearHud(HudLayer.THREAT);
    }

    /**
     * Formate l'affichage de la menace
     */
    private static String formatThreatDisplay(double threat, boolean hasAggro) {
        String aggroIndicator = hasAggro ? "⚠ " : "";
        return String.format("%sMenace: %d", aggroIndicator, (int)threat);
    }

    /**
     * Calcule un pourcentage de menace par rapport au top threat
     * Utile pour afficher des barres de progression
     */
    public static int getThreatPercentage(Player Player, LivingEntity mob) {
        double PlayerThreat = ThreatAPI.getThreat(mob, Player);
        if (PlayerThreat <= 0) return 0;

        Player topPlayer = ThreatAPI.getTopThreat(mob);
        if (topPlayer == null) return 0;

        double topThreat = ThreatAPI.getThreat(mob, topPlayer);
        if (topThreat <= 0) return 0;

        return (int)((PlayerThreat / topThreat) * 100);
    }

    /**
     * Couleur basée sur le niveau de menace
     * Vert = faible, Orange = moyen, Rouge = élevé/aggro
     */
    public static int getThreatColor(int percentage) {
        if (percentage >= 90) return 0xFF0000; // Rouge
        if (percentage >= 70) return 0xFF5500; // Orange foncé
        if (percentage >= 50) return 0xFFAA00; // Orange
        if (percentage >= 30) return 0xFFFF00; // Jaune
        return 0x00FF00; // Vert
    }
}
