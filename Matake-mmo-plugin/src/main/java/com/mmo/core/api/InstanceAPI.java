package com.mmo.core.api;

import com.hypixel.hytale.server.*//npc.entities.*;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.mmo.core.CorePlugin;
import com.mmo.core.managers.InstanceManager;
import com.mmo.core.model.instance.ActiveInstance;
import com.mmo.core.model.instance.InstanceDef;

/**
 * API publique pour gérer les instances (donjons, raids)
 */
public class InstanceAPI {

    // -----------------------------
    //  GETTERS
    // -----------------------------

    /**
     * Retourne la définition d'une instance (solo, groupe, raid)
     * @param id L'identifiant de l'instance
     * @return La définition de l'instance ou null
     */
    public static InstanceDef getInstanceDef(String id) {
        InstanceManager im = getManager();
        return im != null ? im.getInstanceDef(id) : null;
    }

    /**
     * Vérifie si un joueur est actuellement dans une instance
     * @param Player Le joueur à vérifier
     * @return true si le joueur est dans une instance
     */
    public static boolean isInInstance(PlayerRef Player) {
        InstanceManager im = getManager();
        return im != null && im.isInInstance(Player);
    }

    /**
     * Retourne l'instance active d'un joueur
     * @param Player Le joueur
     * @return L'instance active ou null
     */
    public static ActiveInstance getActiveInstance(PlayerRef Player) {
        InstanceManager im = getManager();
        return im != null ? im.getActiveInstance(PlayerRef) : null;
    }

    /**
     * Vérifie si un joueur peut entrer dans une instance
     * @param Player Le joueur
     * @param instanceId L'identifiant de l'instance
     * @return true si le joueur peut entrer
     */
    public static boolean canEnterInstance(PlayerRef Player, String instanceId) {
        InstanceManager im = getManager();
        return im != null && im.canEnterInstance(Player, instanceId);
    }

    // -----------------------------
    //  LANCEMENT D'INSTANCES
    // -----------------------------

    /**
     * Lance une instance SOLO
     * @param Player Le joueur qui entre dans l'instance
     * @param instanceId L'identifiant de l'instance
     */
    public static void startSoloInstance(Player Player, String instanceId) {
        InstanceManager im = getManager();
        if (im != null) {
            im.startSoloInstance(Player, instanceId);
        }
    }

    /**
     * Lance une instance GROUP (2–5 joueurs) ou RAID (5–10 joueurs)
     * Le joueur doit être le leader d'un groupe
     * @param leader Le leader du groupe
     * @param instanceId L'identifiant de l'instance
     */
    public static void startGroupOrRaidInstance(Player leader, String instanceId) {
        InstanceManager im = getManager();
        if (im != null) {
            im.startGroupOrRaidInstance(leader, instanceId);
        }
    }

    // -----------------------------
    //  PROGRESSION / FIN
    // -----------------------------

    /**
     * Marque l'instance comme terminée pour un joueur (boss mort, etc.)
     * @param Player Le joueur qui termine l'instance
     */
    public static void completeInstanceForPlayer(PlayerRef Player) {
        InstanceManager im = getManager();
        if (im != null) {
            im.completeInstanceForPlayer(Player);
        }
    }

    /**
     * Quitter manuellement l'instance
     * @param Player Le joueur qui quitte
     */
    public static void leaveInstance(PlayerRef Player) {
        InstanceManager im = getManager();
        if (im != null) {
            im.leaveInstance(Player);
        }
    }

    /**
     * Téléporte le joueur hors de l'instance
     * @param Player Le joueur à téléporter
     */
    public static void exitInstance(PlayerRef Player) {
        InstanceManager im = getManager();
        if (im != null) {
            im.exitInstance(Player);
        }
    }

    // -----------------------------
    //  COFFRES / LOOT
    // -----------------------------

    /**
     * Interaction avec un coffre de quête dans une instance
     * @param Player Le joueur qui interagit
     * @param questEntityId L'identifiant de l'entité de quête
     */
    public static void openInstanceChest(PlayerRef Player, String questEntityId) {
        InstanceManager im = getManager();
        if (im != null) {
            im.onChestQuestEntityInteract(Player, questEntityId);
        }
    }

    /**
     * Distribue le loot d'un boss aux membres du groupe
     * @param Player Un membre du groupe
     */
    public static void distributeLoot(PlayerRef Player) {
        InstanceManager im = getManager();
        if (im != null) {
            im.distributeLoot(Player);
        }
    }

    // -----------------------------
    //  MOBS / BOSS
    // -----------------------------

    /**
     * À appeler depuis un listener de mort d'entité
     * Gère la progression de l'instance et le loot
     * @param entity L'entité qui meurt
     */
    public static void notifyMobDeath(LivingEntity entity) {
        InstanceManager im = getManager();
        if (im != null) {
            im.onMobDeath(entity);
        }
    }

    /**
     * Vérifie si une entité est un boss d'instance
     * @param entity L'entité à vérifier
     * @return true si c'est un boss
     */
    public static boolean isBoss(LivingEntity entity) {
        InstanceManager im = getManager();
        return im != null && im.isBoss(entity);
    }

    // -----------------------------
    //  STATISTIQUES
    // -----------------------------

    /**
     * Récupère le nombre de fois qu'un joueur a complété une instance
     * @param Player Le joueur
     * @param instanceId L'identifiant de l'instance
     * @return Le nombre de complétions
     */
    public static int getCompletionCount(PlayerRef Player, String instanceId) {
        InstanceManager im = getManager();
        return im != null ? im.getCompletionCount(Player, instanceId) : 0;
    }

    /**
     * Vérifie si un joueur a atteint la limite de complétions pour une instance
     * @param Player Le joueur
     * @param instanceId L'identifiant de l'instance
     * @return true si la limite est atteinte
     */
    public static boolean hasReachedLimit(PlayerRef Player, String instanceId) {
        InstanceManager im = getManager();
        return im != null && im.hasReachedLimit(Player, instanceId);
    }

    // -----------------------------
    //  HELPER
    // -----------------------------

    private static InstanceManager getManager() {
        CorePlugin plugin = CorePlugin.getInstance();
        return plugin != null ? plugin.getInstanceManager() : null;
    }
}
