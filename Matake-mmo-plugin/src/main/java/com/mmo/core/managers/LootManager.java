package com.mmo.core.managers;

import com.hypixel.hytale.server.Server;
import com.hypixel.hytale.server.entity.Entity;
import com.hypixel.hytale.server.world.Location;
import com.hypixel.hytale.server.world.World;
import com.mmo.core.CorePlugin;
import com.mmo.core.model.instance.ActiveInstance;
import com.mmo.core.model.instance.ChestDef;
import com.mmo.core.model.instance.LootItem;
import com.mmo.core.model.instance.LootTable;

import java.util.*;

/**
 * Gestionnaire de loot pour les instances
 * Gère le chargement des tables de loot et le spawn des coffres
 */
public class LootManager {

    private final CorePlugin plugin;
    private final Server server;
    private final Map<String, LootTable> lootTables = new HashMap<>();
    
    // Stockage des entités coffres par instance
    private final Map<String, List<Player>> instanceChests = new HashMap<>();

    public LootManager(CorePlugin plugin, Server server) {
        this.plugin = plugin;
        this.server = server;
        loadLootTables();
    }

    /**
     * Charge les tables de loot depuis le fichier de configuration
     */
    private void loadLootTables() {
        try {
            var cfg = server.getConfigManager().getConfig(plugin, "config/loot.yml");
            var section = cfg.getSection("lootTables");
            
            if (section == null) {
                plugin.getLogger().warn("[LootManager] Section 'lootTables' introuvable dans loot.yml");
                createDefaultLootTable();
                return;
            }

            int loaded = 0;
            for (String id : section.getKeys()) {
                try {
                    var tSec = section.getSection(id);
                    String mode = tSec.getString("mode", "INDIVIDUAL");
                    List<LootItem> items = new ArrayList<>();
                    
                    for (Object o : tSec.getList("items")) {
                        var m = (Map<String, Object>) o;
                        String itemId = (String) m.get("id");
                        int amount = ((Number) m.get("amount")).intValue();
                        double chance = ((Number) m.get("chance")).doubleValue();
                        items.add(new LootItem(itemId, amount, chance));
                    }
                    
                    lootTables.put(id, new LootTable(id, mode, items));
                    loaded++;
                    
                } catch (Exception e) {
                    plugin.getLogger().error("[LootManager] Erreur lors du chargement de la table: " + id);
                    e.printStackTrace();
                }
            }
            
            plugin.getLogger().info("[LootManager] " + loaded + " table(s) de loot chargée(s)");
            
        } catch (Exception e) {
            plugin.getLogger().error("[LootManager] Erreur lors du chargement des loot tables: " + e.getMessage());
            e.printStackTrace();
            createDefaultLootTable();
        }
    }

    /**
     * Crée une table de loot par défaut pour les tests
     */
    private void createDefaultLootTable() {
        List<LootItem> items = Arrays.asList(
            new LootItem("gold_coin", 10, 1.0),
            new LootItem("health_potion", 3, 0.5),
            new LootItem("rare_gem", 1, 0.1)
        );
        
        LootTable defaultTable = new LootTable("default_loot", "INDIVIDUAL", items);
        lootTables.put("default_loot", defaultTable);
        
        plugin.getLogger().info("[LootManager] Table de loot par défaut créée");
    }

    /**
     * Récupère une table de loot par son ID
     * @param id L'identifiant de la table de loot
     * @return La table de loot, ou null si introuvable
     */
    public LootTable getLootTable(String id) {
        return lootTables.get(id);
    }

    /**
     * Fait apparaître les coffres de loot dans une instance active
     * @param inst L'instance dans laquelle spawner les coffres
     */
    public void spawnLootChests(ActiveInstance inst) {
        if (inst == null || inst.getDef() == null) {
            plugin.getLogger().warn("[LootManager] Instance ou définition nulle lors du spawn des coffres");
            return;
        }

        List<ChestDef> chests = inst.getDef().getChests();
        if (chests == null || chests.isEmpty()) {
            plugin.getLogger().info("[LootManager] Aucun coffre défini pour cette instance");
            return;
        }

        try {
            // Récupérer le monde de l'instance
            String worldName = inst.getDef().getWorld();
            World world = server.getWorldManager().getWorld(worldName);
            
            if (world == null) {
                plugin.getLogger().error("[LootManager] Monde introuvable: " + worldName);
                return;
            }

            List<Player> spawnedChests = new ArrayList<>();
            int spawnedCount = 0;

            // Spawner chaque coffre
            for (ChestDef chestDef : chests) {
                try {
                    // Créer la location du coffre
                    Location location = new Location(world, chestDef.getX(), chestDef.getY(), chestDef.getZ());
                    
                    // Spawner l'entité coffre
                    // Note: "quest_chest" est un exemple, adapter selon votre système
                    Entity chest = world.spawnEntity("quest_chest", location);
                    
                    if (chest != null) {
                        // Définir le nom custom pour l'interaction
                        chest.setCustomName(chestDef.getQuestEntityId());
                        
                        // Stocker l'Player du coffre
                        spawnedChests.add(chest.getPlayer());
                        spawnedCount++;
                        
                        plugin.getLogger().debug("[LootManager] Coffre spawné: " + chestDef.getId() + 
                                                 " à (" + chestDef.getX() + ", " + chestDef.getY() + ", " + chestDef.getZ() + ")");
                    } else {
                        plugin.getLogger().warn("[LootManager] Échec du spawn du coffre: " + chestDef.getId());
                    }
                    
                } catch (Exception e) {
                    plugin.getLogger().error("[LootManager] Erreur lors du spawn du coffre " + chestDef.getId() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // Stocker les coffres pour nettoyage ultérieur
            String instanceKey = inst.getDef().getId() + ":" + System.currentTimeMillis();
            instanceChests.put(instanceKey, spawnedChests);

            plugin.getLogger().info("[LootManager] " + spawnedCount + "/" + chests.size() + 
                                   " coffre(s) spawné(s) pour l'instance " + inst.getDef().getName());

        } catch (Exception e) {
            plugin.getLogger().error("[LootManager] Erreur lors du spawn des coffres: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Nettoie les coffres d'une instance (appelé lors de la fermeture de l'instance)
     * @param instanceKey Clé unique de l'instance
     */
    public void clearInstanceChests(String instanceKey) {
        List<Player> chests = instanceChests.remove(instanceKey);
        if (chests == null || chests.isEmpty()) return;

        int removed = 0;
        for (Player chestPlayer : chests) {
            try {
                // Trouver et supprimer l'entité
                Entity chest = server.getEntityManager().getEntity(chestPlayer);
                if (chest != null) {
                    chest.remove();
                    removed++;
                }
            } catch (Exception e) {
                plugin.getLogger().warn("[LootManager] Erreur lors de la suppression du coffre " + chestPlayer);
            }
        }

        plugin.getLogger().debug("[LootManager] " + removed + " coffre(s) nettoyé(s) pour l'instance " + instanceKey);
    }

    /**
     * Effectue un roll sur une table de loot et retourne les items obtenus
     * @param lootTableId L'ID de la table de loot
     * @return Liste des items obtenus après le roll
     */
    public List<LootItem> rollLoot(String lootTableId) {
        LootTable table = lootTables.get(lootTableId);
        if (table == null) {
            plugin.getLogger().warn("[LootManager] Table de loot introuvable: " + lootTableId);
            return new ArrayList<>();
        }
        
        return table.roll();
    }

    /**
     * Vérifie si une table de loot existe
     * @param id L'ID de la table de loot
     * @return true si la table existe
     */
    public boolean hasLootTable(String id) {
        return lootTables.containsKey(id);
    }

    /**
     * Récupère toutes les tables de loot disponibles
     * @return Map des tables de loot par ID
     */
    public Map<String, LootTable> getAllLootTables() {
        return Collections.unmodifiableMap(lootTables);
    }

    /**
     * Recharge les tables de loot depuis la configuration
     */
    public void reload() {
        lootTables.clear();
        loadLootTables();
        plugin.getLogger().info("[LootManager] Tables de loot rechargées");
    }

    /**
     * Nettoie toutes les ressources lors de l'arrêt
     */
    public void shutdown() {
        // Nettoyer tous les coffres actifs
        for (String instanceKey : new ArrayList<>(instanceChests.keySet())) {
            clearInstanceChests(instanceKey);
        }
        
        lootTables.clear();
        plugin.getLogger().info("[LootManager] Shutdown complet");
    }
}
