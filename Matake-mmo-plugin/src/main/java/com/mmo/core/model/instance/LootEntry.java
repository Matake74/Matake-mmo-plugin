package com.mmo.core.model.instance;

/**
 * Entrée de loot individuelle
 * Définit un item avec sa quantité et sa probabilité de drop
 */
public class LootEntry {

    private final String itemId;
    private final int min;
    private final int max;
    private final double chance;

    /**
     * Crée une entrée de loot
     *
     * @param itemId Type d'item Hytale
     * @param min Quantité minimum
     * @param max Quantité maximum
     * @param chance Probabilité de drop (0.0 à 1.0)
     */
    public LootEntry(String itemId, int min, int max, double chance) {
        if (itemId == null || itemId.isEmpty()) {
            throw new IllegalArgumentException("Item ID cannot be null or empty");
        }
        if (min < 0) {
            throw new IllegalArgumentException("Min quantity cannot be negative");
        }
        if (max < min) {
            throw new IllegalArgumentException("Max quantity cannot be less than min");
        }
        if (chance < 0.0 || chance > 1.0) {
            throw new IllegalArgumentException("Chance must be between 0.0 and 1.0");
        }

        this.itemId = itemId;
        this.min = min;
        this.max = max;
        this.chance = chance;
    }

    // ==================== Getters ====================

    public String getItemId() { return itemId; }
    public int getMin() { return min; }
    public int getMax() { return max; }
    public double getChance() { return chance; }

    // ==================== Utility Methods ====================

    /**
     * Calcule la quantité moyenne qu'un joueur peut s'attendre à recevoir
     * Prend en compte la probabilité de drop
     */
    public double getExpectedAmount() {
        double avgQuantity = (min + max) / 2.0;
        return avgQuantity * chance;
    }

    /**
     * Vérifie si cet item est garanti (100% de chance)
     */
    public boolean isGuaranteed() {
        return chance >= 1.0;
    }

    /**
     * Vérifie si la quantité est fixe (min == max)
     */
    public boolean isFixedAmount() {
        return min == max;
    }

    /**
     * Retourne le pourcentage de chance (0-100)
     */
    public double getChancePercent() {
        return chance * 100.0;
    }

    @Override
    public String toString() {
        return "LootEntry{" +
                "item='" + itemId + '\'' +
                ", amount=" + min + (min == max ? "" : "-" + max) +
                ", chance=" + String.format("%.1f%%", getChancePercent()) +
                '}';
    }
}