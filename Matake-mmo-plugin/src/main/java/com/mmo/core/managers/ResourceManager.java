package com.mmo.core.managers;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.mmo.core.model.profession.*;
import java.util.*;

/**
 * Gestionnaire des ressources récoltables
 */
public class ResourceManager {

    private final JavaPlugin plugin;
    private final ProfessionManager professionManager;

    private final Map<String, ResourceDef> resources = new HashMap<>();
    private final Map<String, ResourceInstance> cooldowns = new HashMap<>();

    public ResourceManager(JavaPlugin plugin, ProfessionManager professionManager) {
        this.plugin = plugin;
        this.professionManager = professionManager;
        loadResources();
        loadCooldowns();
        startRespawnTask();
    }

    /**
     * Charge les définitions de ressources depuis la configuration
     */
    private void loadResources() {
        try {
            // TODO: Charger depuis config/resources.yml via l'API Hytale
            // Pour l'instant, quelques ressources d'exemple
            
            resources.put("oak_tree", new ResourceDef(
                "oak_tree",
                "hytale:oak_log",
                ProfessionType.LUMBERJACK,
                1,
                10.0,
                60
            ));

            resources.put("iron_ore", new ResourceDef(
                "iron_ore",
                "hytale:iron_ore",
                ProfessionType.MINER,
                5,
                15.0,
                120
            ));

            resources.put("leather_hide", new ResourceDef(
                "leather_hide",
                "hytale:leather_hide",
                ProfessionType.TANNER,
                1,
                8.0,
                45
            ));

            plugin.getLogger().info("[ResourceManager] " + resources.size() + " ressources chargées.");
        } catch (Exception e) {
            plugin.getLogger().error("[ResourceManager] Erreur lors du chargement des ressources: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Charge les cooldowns de respawn depuis la sauvegarde
     */
    private void loadCooldowns() {
        // TODO: Charger depuis fichier/DB
    }

    /**
     * Sauvegarde les cooldowns de respawn
     */
    private void saveCooldowns() {
        // TODO: Sauvegarder dans fichier/DB
    }

    /**
     * Démarre la tâche de respawn des ressources
     */
    private void startRespawnTask() {
        // TODO: Utiliser l'API Scheduler de Hytale
        // Exemple: server.getScheduler().runRepeating(() -> { ... }, 20, 20);
        
        // Cette tâche devrait tourner toutes les secondes pour vérifier les respawns
    }

    /**
     * Traite le respawn des ressources
     */
    public void processRespawns() {
        long now = System.currentTimeMillis();
        List<String> toRemove = new ArrayList<>();

        for (Map.Entry<String, ResourceInstance> entry : cooldowns.entrySet()) {
            ResourceInstance inst = entry.getValue();
            if (inst.getRespawnAt() <= now) {
                // TODO: Replacer le bloc via l'API World de Hytale
                // World world = server.getWorldManager().getWorld(inst.getWorld());
                // if (world != null) {
                //     ResourceDef def = getResource(inst.getResourceId());
                //     world.setBlock(inst.getX(), inst.getY(), inst.getZ(), def.getBlockId());
                // }
                toRemove.add(entry.getKey());
            }
        }

        toRemove.forEach(cooldowns::remove);
    }

    /**
     * Appelé lors de l'arrêt
     */
    public void shutdown() {
        saveCooldowns();
        plugin.getLogger().info("[ResourceManager] Arrêt du gestionnaire de ressources.");
    }

    /**
     * Traite la récolte d'une ressource
     * @return true si la récolte est autorisée
     */
    public boolean onResourceHarvest(Player Player, String worldName, int x, int y, int z, String blockId) {
        // Trouver la définition de ressource
        ResourceDef def = resources.values().stream()
            .filter(r -> r.getBlockId().equals(blockId))
            .findFirst()
            .orElse(null);

        if (def == null) {
            return true; // Pas une ressource gérée, laisser le comportement par défaut
        }

        // Vérifier le niveau requis
        if (!professionManager.hasLevel(Player, def.getProfession(), def.getMinLevel())) {
            // TODO: Envoyer message au joueur
            return false;
        }

        // Vérifier si la ressource est en cooldown
        String key = worldName + ":" + x + ":" + y + ":" + z;
        if (cooldowns.containsKey(key)) {
            // TODO: Envoyer message au joueur
            return false;
        }

        // Ajouter au cooldown
        long now = System.currentTimeMillis();
        long respawnAt = now + def.getRespawnSeconds() * 1000L;
        cooldowns.put(key, new ResourceInstance(
            def.getId(),
            worldName,
            x, y, z,
            now,
            respawnAt
        ));

        // Donner l'XP
        professionManager.addXp(Player, def.getProfession(), def.getXp());
        professionManager.incrementGathers(Player, def.getProfession());

        // Vérifier bonus de double récolte
        boolean doubleGather = professionManager.rollDoubleGather(Player, def.getProfession());
        if (doubleGather) {
            // TODO: Doubler le loot
        }

        return true;
    }

    /**
     * Récupère une ressource par son ID
     */
    public ResourceDef getResource(String id) {
        return resources.get(id);
    }

    /**
     * Récupère une ressource par son blockId
     */
    public ResourceDef getResourceByBlock(String blockId) {
        return resources.values().stream()
            .filter(r -> r.getBlockId().equals(blockId))
            .findFirst()
            .orElse(null);
    }

    /**
     * Récupère toutes les ressources
     */
    public Collection<ResourceDef> getAllResources() {
        return new ArrayList<>(resources.values());
    }
}
