package com.mmo.core.model.profession;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Player;

/**
 * Données de profession d'un joueur
 */
public class PlayerProfessionData {

    private final Player Player;
    private final Map<ProfessionType, Integer> levels = new EnumMap<>(ProfessionType.class);
    private final Map<ProfessionType, Double> xp = new EnumMap<>(ProfessionType.class);
    private final Set<String> unlockedRecipes = new HashSet<>();
    private final Map<ProfessionType, Integer> totalCrafts = new EnumMap<>(ProfessionType.class);
    private final Map<ProfessionType, Integer> totalGathers = new EnumMap<>(ProfessionType.class);

    public PlayerProfessionData(Player Player) {
        this.Player = Player;
        for (ProfessionType type : ProfessionType.values()) {
            levels.put(type, 1);
            xp.put(type, 0.0);
            totalCrafts.put(type, 0);
            totalGathers.put(type, 0);
        }
    }

    public Player getPlayer() {
        return Player;
    }

    public int getLevel(ProfessionType type) {
        return levels.getOrDefault(type, 1);
    }

    public double getXp(ProfessionType type) {
        return xp.getOrDefault(type, 0.0);
    }

    public void addXp(ProfessionType type, double amount) {
        double current = xp.getOrDefault(type, 0.0);
        xp.put(type, current + amount);
    }

    public void setXp(ProfessionType type, double amount) {
        xp.put(type, amount);
    }

    public void setLevel(ProfessionType type, int level) {
        levels.put(type, level);
    }

    public boolean hasRecipe(String recipeId) {
        return unlockedRecipes.contains(recipeId);
    }

    public void unlockRecipe(String recipeId) {
        unlockedRecipes.add(recipeId);
    }

    public Set<String> getUnlockedRecipes() {
        return new HashSet<>(unlockedRecipes);
    }

    public int getTotalCrafts(ProfessionType type) {
        return totalCrafts.getOrDefault(type, 0);
    }

    public void incrementCrafts(ProfessionType type) {
        totalCrafts.put(type, totalCrafts.getOrDefault(type, 0) + 1);
    }

    public int getTotalGathers(ProfessionType type) {
        return totalGathers.getOrDefault(type, 0);
    }

    public void incrementGathers(ProfessionType type) {
        totalGathers.put(type, totalGathers.getOrDefault(type, 0) + 1);
    }
}
