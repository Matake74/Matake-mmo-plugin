package com.mmo.core.model.hud;

/**
 * Énumération des différents layers (couches) du HUD
 * Chaque layer peut afficher un type d'information différent
 */
public enum HudLayer {
    /**
     * Affichage des informations du groupe (party)
     */
    PARTY,
    
    /**
     * Affichage des objectifs de quête actifs
     */
    QUEST,
    
    /**
     * Affichage du niveau de menace (aggro)
     */
    THREAT,
    
    /**
     * Affichage des informations d'instance (boss, objectifs)
     */
    INSTANCE,
    
    /**
     * Affichage des informations de profession (niveau, XP)
     */
    PROFESSION,
    
    /**
     * Barre de santé d'un boss
     */
    BOSS_BAR,
    
    /**
     * Notifications temporaires
     */
    NOTIFICATION,
    
    /**
     * Texte de combat (dégâts, soins)
     */
    COMBAT_TEXT,
    
    /**
     * Barre de progression (chargement, crafting, etc.)
     */
    PROGRESS_BAR,
    
    /**
     * Informations de waypoint/navigation
     */
    WAYPOINT,
    
    /**
     * Statistiques du joueur (mana, énergie, etc.)
     */
    Player_STATS,
    
    /**
     * Buffs/debuffs actifs
     */
    BUFFS,
    
    /**
     * Minimap ou boussole
     */
    MINIMAP,
    
    /**
     * Cooldowns de compétences
     */
    COOLDOWNS,
    
    /**
     * Messages de chat/système
     */
    CHAT,
    
    /**
     * Layer personnalisé 1
     */
    CUSTOM_1,
    
    /**
     * Layer personnalisé 2
     */
    CUSTOM_2,
    
    /**
     * Layer personnalisé 3
     */
    CUSTOM_3
}
