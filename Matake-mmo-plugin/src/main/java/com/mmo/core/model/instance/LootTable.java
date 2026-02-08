package com.mmo.core.model.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Table de loot simple avec système de roll aléatoire
 * Version alternative plus simple que LootTableDef
 */
public class LootTable {

    private final String id;
    private final String mode; // INDIVIDUAL / RNG
    private final List<LootItem> items;
    private final Random random = new Random();

    public LootTable(String id, String mode, List<LootItem> items) {
        this.id = id;
        this.mode = mode;
        this.items = items;
    }

    /**
     * Lance le dé pour déterminer quels items sont obtenus
     * @return Liste des items obtenus après le roll
     */
    public List<LootItem> roll() {
        List<LootItem> result = new ArrayList<>();
        for (LootItem item : items) {
            if (random.nextDouble() <= item.getChance()) {
                result.add(item);
            }
        }
        return result;
    }

    public String getId() {
        return id;
    }

    public String getMode() {
        return mode;
    }
    
    public List<LootItem> getItems() {
        return items;
    }
}
