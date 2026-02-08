package com.mmo.core.model.instance;

import java.util.Collections;
import java.util.List;

/**
 * Table de loot contenant plusieurs entrées
 * Définit les items possibles et leurs probabilités
 */
public class LootTableDef {

    private final String id;
    private final List<LootEntry> entries;

    /**
     * Crée une table de loot
     *
     * @param id Identifiant unique de la table
     * @param entries Liste des entrées de loot
     */
    public LootTableDef(String id, List<LootEntry> entries) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Loot table ID cannot be null or empty");
        }
        if (entries == null || entries.isEmpty()) {
            throw new IllegalArgumentException("Loot entries cannot be null or empty");
        }

        this.id = id;
        this.entries = entries;
    }

    // ==================== Getters ====================

    public String getId() { return id; }

    /**
     * Retourne une liste immuable des entrées
     */
    public List<LootEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    // ==================== Utility Methods ====================

    /**
     * Retourne le nombre d'entrées dans cette table
     */
    public int getEntryCount() {
        return entries.size();
    }

    /**
     * Vérifie si la table contient un item spécifique
     */
    public boolean containsItem(String itemId) {
        return entries.stream()
                .anyMatch(entry -> entry.getItemId().equals(itemId));
    }

    /**
     * Calcule le nombre moyen d'items différents qu'un joueur peut recevoir
     */
    public double getExpectedItemTypeCount() {
        return entries.stream()
                .mapToDouble(LootEntry::getChance)
                .sum();
    }

    @Override
    public String toString() {
        return "LootTableDef{" +
                "id='" + id + '\'' +
                ", entries=" + entries.size() +
                '}';
    }
}