package com.mmo.core.api;

import com.hytale.server.entity.LivingEntity;
import com.hytale.server.Player.Player;
import com.mmo.core.CorePlugin;
import com.mmo.core.managers.ThreatManager;

/**
 * API publique pour gérer le système de menace (aggro/threat)
 * 
 * Fonctionnalités :
 * - Gestion de l'aggro basée sur les dégâts et les soins
 * - Système de taunt (provocation)
 * - Récupération de la menace actuelle d'un joueur
 */
public class ThreatAPI {

    /**
     * Ajoute de la menace basée sur les dégâts infligés
     * @param mob L'entité qui reçoit la menace
     * @param Player Le joueur qui génère la menace
     * @param damage Montant des dégâts infligés
     */
    public static void addDamageThreat(LivingEntity mob, Player Player, double damage) {
        ThreatManager tm = getManager();
        if (tm != null) {
            tm.addDamageThreat(mob, Player, damage);
        }
    }

    /**
     * Ajoute de la menace basée sur les soins effectués
     * Les soins génèrent généralement 50% de menace par rapport aux dégâts
     * @param mob L'entité qui reçoit la menace
     * @param healer Le joueur qui soigne
     * @param heal Montant des soins effectués
     */
    public static void addHealThreat(LivingEntity mob, Player healer, double heal) {
        ThreatManager tm = getManager();
        if (tm != null) {
            tm.addHealThreat(mob, healer, heal);
        }
    }

    /**
     * Force un mob à cibler un joueur (provocation/taunt)
     * Typiquement utilisé par les tanks pour contrôler l'aggro
     * @param mob L'entité à provoquer
     * @param tank Le tank qui provoque
     */
    public static void taunt(LivingEntity mob, Player tank) {
        ThreatManager tm = getManager();
        if (tm != null) {
            tm.taunt(mob, tank);
        }
    }

    /**
     * Récupère le niveau de menace actuel d'un joueur sur un mob
     * @param mob L'entité
     * @param Player Le joueur
     * @return Le niveau de menace (0 si aucune menace)
     */
    public static double getThreat(LivingEntity mob, Player Player) {
        ThreatManager tm = getManager();
        if (tm != null) {
            return tm.getThreat(mob, Player);
        }
        return 0.0;
    }

    /**
     * Obtient le joueur avec le plus de menace sur un mob
     * @param mob L'entité
     * @return Le joueur avec le plus de menace ou null
     */
    public static Player getTopThreat(LivingEntity mob) {
        ThreatManager tm = getManager();
        if (tm != null) {
            return tm.getTopThreat(mob);
        }
        return null;
    }

    /**
     * Réinitialise toute la menace d'un mob
     * Utile lors d'un wipe ou reset de combat
     * @param mob L'entité à réinitialiser
     */
    public static void resetThreat(LivingEntity mob) {
        ThreatManager tm = getManager();
        if (tm != null) {
            tm.resetThreat(mob);
        }
    }

    /**
     * Supprime la menace d'un joueur spécifique sur un mob
     * @param mob L'entité
     * @param Player Le joueur
     */
    public static void removeThreat(LivingEntity mob, Player Player) {
        ThreatManager tm = getManager();
        if (tm != null) {
            tm.removeThreat(mob, Player);
        }
    }

    /**
     * Réduit la menace d'un joueur d'un certain pourcentage
     * Utile pour les compétences de réduction d'aggro
     * @param mob L'entité
     * @param Player Le joueur
     * @param percentage Pourcentage de réduction (0.0 à 1.0)
     */
    public static void reduceThreat(LivingEntity mob, Player Player, double percentage) {
        ThreatManager tm = getManager();
        if (tm != null) {
            tm.reduceThreat(mob, Player, percentage);
        }
    }

    // -----------------------------
    //  HELPER
    // -----------------------------

    private static ThreatManager getManager() {
        CorePlugin plugin = CorePlugin.getInstance();
        return plugin != null ? plugin.getThreatManager() : null;
    }
}
