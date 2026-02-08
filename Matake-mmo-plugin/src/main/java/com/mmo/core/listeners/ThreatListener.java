package com.mmo.core.listeners;

import com.hytale.server.entity.LivingEntity;
import com.hytale.server.events.entity.EntityDamageEvent;
import com.hytale.server.events.entity.EntityHealEvent;
import com.hytale.server.events.Player.PlayerBlockEvent;
import com.hytale.server.events.Player.PlayerUseSkillEvent;
import com.hytale.server.Player.Player;
import com.mmo.core.CorePlugin;
import com.mmo.core.managers.ThreatManager;

/**
 * Listener pour les événements liés au système de menaces
 * Intégré dans CorePlugin
 */
public class ThreatListener {

    private final CorePlugin plugin;
    private final ThreatManager threatManager;

    public ThreatListener(CorePlugin plugin) {
        this.plugin = plugin;
        this.threatManager = plugin.getThreatManager();
    }

    /**
     * Gère les dégâts infligés aux mobs
     * Ajoute de la menace au joueur qui inflige les dégâts
     */
    public void onEntityDamage(EntityDamageEvent event) {
        // Vérifier que c'est un joueur qui attaque
        if (!(event.getDamager() instanceof Player)) return;
        
        // Vérifier que la cible est un LivingEntity (mob)
        if (!(event.getEntity() instanceof LivingEntity)) return;

        Player Player = (Player) event.getDamager();
        LivingEntity mob = (LivingEntity) event.getEntity();

        double damage = event.getDamage();
        
        // Ajouter la menace via le manager
        threatManager.addDamageThreat(mob, Player, damage);
    }

    /**
     * Gère les soins effectués
     * Les soins génèrent de la menace (50% par défaut)
     */
    public void onEntityHeal(EntityHealEvent event) {
        // Vérifier que c'est un joueur qui soigne
        if (!(event.getHealer() instanceof Player)) return;

        Player healer = (Player) event.getHealer();
        
        // Pour chaque mob en combat avec le groupe, ajouter de la menace de soin
        // TODO: Implémenter la logique de détection des mobs en combat
        // Pour l'instant, on pourrait itérer sur tous les mobs proches
        
        double healAmount = event.getAmount();
        
        // Exemple : Récupérer tous les mobs dans un rayon
        // getNearbyMobs(healer, 30.0).forEach(mob -> {
        //     threatManager.addHealThreat(mob, healer, healAmount);
        // });
    }

    /**
     * Gère les dégâts bloqués par un tank
     * Génère de la menace supplémentaire (150% par défaut)
     */
    public void onPlayerBlock(PlayerBlockEvent event) {
        Player Player = event.getPlayer();
        
        // Vérifier que l'attaquant est un LivingEntity
        if (!(event.getAttacker() instanceof LivingEntity)) return;

        LivingEntity mob = (LivingEntity) event.getAttacker();
        double blockedDamage = event.getBlockedDamage();

        // Ajouter la menace de blocage
        threatManager.addBlockThreat(mob, Player, blockedDamage);
    }

    /**
     * Gère l'utilisation de compétences
     * Détecte notamment les compétences de provocation (taunt)
     */
    public void onPlayerUseSkill(PlayerUseSkillEvent event) {
        Player Player = event.getPlayer();
        String skillId = event.getSkillId();

        // Vérifier si c'est une compétence de taunt
        if ("taunt".equalsIgnoreCase(skillId) || 
            "provoke".equalsIgnoreCase(skillId) || 
            "challenge".equalsIgnoreCase(skillId)) {
            
            // Vérifier que la cible est un LivingEntity
            if (!(event.getTarget() instanceof LivingEntity)) return;

            LivingEntity mob = (LivingEntity) event.getTarget();
            
            // Appliquer le taunt
            threatManager.taunt(mob, Player);
        }
        
        // Autres compétences qui génèrent de la menace
        else if ("shout".equalsIgnoreCase(skillId) || 
                 "roar".equalsIgnoreCase(skillId)) {
            // AoE taunt - pourrait affecter plusieurs mobs
            // TODO: Implémenter selon les besoins
        }
    }

    /**
     * Méthode helper pour récupérer les mobs proches
     * TODO: Adapter selon l'API Hytale
     */
    // private List<LivingEntity> getNearbyMobs(Player Player, double radius) {
    //     return Player.getWorld()
    //         .getNearbyEntities(Player.getLocation(), radius)
    //         .stream()
    //         .filter(e -> e instanceof LivingEntity)
    //         .filter(e -> !(e instanceof Player))
    //         .map(e -> (LivingEntity) e)
    //         .collect(Collectors.toList());
    // }
}
