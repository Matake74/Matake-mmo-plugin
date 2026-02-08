package com.mmo.core.model.instance;

/**
 * Représente un item de loot avec sa quantité et sa chance de drop
 * Utilisé pour les systèmes de loot simples
 */
public class LootItem {

    private final String id;
    private final int amount;
    private final double chance;

    public LootItem(String id, int amount, double chance) {
        this.id = id;
        this.amount = amount;
        this.chance = chance;
    }

    public String getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public double getChance() {
        return chance;
    }
}
