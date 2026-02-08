package com.mmo.core.util;

import com.mmo.core.model.instance.*;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

/**
 * Utilitaire pour charger les configurations YAML des instances
 * Utilise SnakeYAML pour le parsing
 */
public class InstanceConfigLoader {

    /**
     * Charge les définitions d'instances depuis un fichier YAML
     */
    public static Map<String, InstanceDef> loadInstances(InputStream input) {
        Map<String, InstanceDef> instances = new HashMap<>();
        
        try {
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(input);
            
            if (data == null || !data.containsKey("instances")) {
                return instances;
            }
            
            @SuppressWarnings("unchecked")
            Map<String, Map<String, Object>> instancesData = 
                (Map<String, Map<String, Object>>) data.get("instances");
            
            for (Map.Entry<String, Map<String, Object>> entry : instancesData.entrySet()) {
                try {
                    InstanceDef def = parseInstanceDef(entry.getValue());
                    instances.put(def.getId(), def);
                } catch (Exception e) {
                    System.err.println("Error loading instance " + entry.getKey() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error loading instances config: " + e.getMessage());
            e.printStackTrace();
        }
        
        return instances;
    }

    /**
     * Parse une définition d'instance depuis les données YAML
     */
    @SuppressWarnings("unchecked")
    private static InstanceDef parseInstanceDef(Map<String, Object> data) {
        String id = (String) data.get("id");
        String name = (String) data.get("name");
        InstanceType type = InstanceType.valueOf(((String) data.get("type")).toUpperCase());
        int minPlayers = (Integer) data.get("minPlayers");
        int maxPlayers = (Integer) data.get("maxPlayers");
        
        // Spawn
        String world = (String) data.get("world");
        Map<String, Object> spawnData = (Map<String, Object>) data.get("spawn");
        double spawnX = getDouble(spawnData, "x");
        double spawnY = getDouble(spawnData, "y");
        double spawnZ = getDouble(spawnData, "z");
        
        // Exit
        String exitWorld = (String) data.get("exitWorld");
        Map<String, Object> exitData = (Map<String, Object>) data.get("exit");
        double exitX = getDouble(exitData, "x");
        double exitY = getDouble(exitData, "y");
        double exitZ = getDouble(exitData, "z");
        
        // Quests
        String startQuest = (String) data.get("startQuest");
        String endQuest = (String) data.get("endQuest");
        
        // Timings
        int startCountdown = (Integer) data.getOrDefault("startCountdownSeconds", 10);
        int lootPhase = (Integer) data.getOrDefault("lootPhaseSeconds", 60);
        
        // Mobs
        List<MobPackDef> mobPacks = new ArrayList<>();
        if (data.containsKey("mobs")) {
            List<Map<String, Object>> mobsData = (List<Map<String, Object>>) data.get("mobs");
            for (Map<String, Object> mobData : mobsData) {
                mobPacks.add(parseMobPack(mobData));
            }
        }
        
        // Boss
        BossDef boss = null;
        if (data.containsKey("boss")) {
            Map<String, Object> bossData = (Map<String, Object>) data.get("boss");
            boss = parseBoss(bossData);
        }
        
        // Chests
        List<ChestDef> chests = new ArrayList<>();
        if (data.containsKey("chests")) {
            List<Map<String, Object>> chestsData = (List<Map<String, Object>>) data.get("chests");
            for (Map<String, Object> chestData : chestsData) {
                chests.add(parseChest(chestData));
            }
        }
        
        return new InstanceDef(id, name, type, minPlayers, maxPlayers,
                world, spawnX, spawnY, spawnZ,
                exitWorld, exitX, exitY, exitZ,
                startQuest, endQuest, startCountdown, lootPhase,
                mobPacks, boss, chests);
    }

    /**
     * Parse un pack de mobs
     */
    @SuppressWarnings("unchecked")
    private static MobPackDef parseMobPack(Map<String, Object> data) {
        String id = (String) data.get("id");
        String type = (String) data.get("type");
        int count = (Integer) data.get("count");
        
        Map<String, Object> spawnData = (Map<String, Object>) data.get("spawn");
        double x = getDouble(spawnData, "x");
        double y = getDouble(spawnData, "y");
        double z = getDouble(spawnData, "z");
        
        return new MobPackDef(id, type, count, x, y, z);
    }

    /**
     * Parse un boss
     */
    @SuppressWarnings("unchecked")
    private static BossDef parseBoss(Map<String, Object> data) {
        String id = (String) data.get("id");
        String type = (String) data.get("type");
        
        Map<String, Object> spawnData = (Map<String, Object>) data.get("spawn");
        double x = getDouble(spawnData, "x");
        double y = getDouble(spawnData, "y");
        double z = getDouble(spawnData, "z");
        
        return new BossDef(id, type, x, y, z);
    }

    /**
     * Parse un coffre
     */
    @SuppressWarnings("unchecked")
    private static ChestDef parseChest(Map<String, Object> data) {
        String id = (String) data.get("id");
        String questEntityId = (String) data.get("questEntityId");
        String lootTable = (String) data.get("lootTable");
        
        Map<String, Object> spawnData = (Map<String, Object>) data.get("spawn");
        double x = getDouble(spawnData, "x");
        double y = getDouble(spawnData, "y");
        double z = getDouble(spawnData, "z");
        
        return new ChestDef(id, questEntityId, lootTable, x, y, z);
    }

    /**
     * Charge les tables de loot depuis un fichier YAML
     */
    @SuppressWarnings("unchecked")
    public static Map<String, LootTableDef> loadLootTables(InputStream input) {
        Map<String, LootTableDef> lootTables = new HashMap<>();
        
        try {
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(input);
            
            if (data == null || !data.containsKey("lootTables")) {
                return lootTables;
            }
            
            Map<String, List<Map<String, Object>>> tablesData = 
                (Map<String, List<Map<String, Object>>>) data.get("lootTables");
            
            for (Map.Entry<String, List<Map<String, Object>>> entry : tablesData.entrySet()) {
                String tableId = entry.getKey();
                List<LootEntry> entries = new ArrayList<>();
                
                for (Map<String, Object> entryData : entry.getValue()) {
                    String itemId = (String) entryData.get("itemId");
                    int min = (Integer) entryData.get("min");
                    int max = (Integer) entryData.get("max");
                    double chance = getDouble(entryData, "chance");
                    
                    entries.add(new LootEntry(itemId, min, max, chance));
                }
                
                lootTables.put(tableId, new LootTableDef(tableId, entries));
            }
            
        } catch (Exception e) {
            System.err.println("Error loading loot tables config: " + e.getMessage());
            e.printStackTrace();
        }
        
        return lootTables;
    }

    /**
     * Helper pour récupérer un double depuis une map
     * Gère les cas où YAML parse en Integer
     */
    private static double getDouble(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Float) {
            return ((Float) value).doubleValue();
        }
        return 0.0;
    }
}
