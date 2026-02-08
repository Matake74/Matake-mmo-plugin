package com.mmo.core.managers;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.mmo.core.model.profession.*;
import java.util.*;

/**
 * Gestionnaire des recettes de craft
 */
public class RecipeManager {

    private final JavaPlugin plugin;
    private final ProfessionManager professionManager;

    private final Map<String, RecipeDef> recipes = new HashMap<>();

    public RecipeManager(JavaPlugin plugin, ProfessionManager professionManager) {
        this.plugin = plugin;
        this.professionManager = professionManager;
        loadRecipes();
    }

    /**
     * Charge les recettes depuis la configuration
     */
    private void loadRecipes() {
        try {
            // TODO: Charger depuis config/recipes.yml via l'API Hytale
            // Pour l'instant, quelques recettes d'exemple
            
            // Exemple: Épée de fer
            List<RecipeDef.Ingredient> ironSwordInputs = Arrays.asList(
                new RecipeDef.Ingredient("iron_ingot", 2),
                new RecipeDef.Ingredient("wood_stick", 1)
            );
            recipes.put("iron_sword", new RecipeDef(
                "iron_sword",
                ProfessionType.BLACKSMITH_WEAPON,
                10,
                "anvil",
                ironSwordInputs,
                new RecipeDef.Ingredient("iron_sword", 1),
                50.0
            ));

            // Exemple: Armure de cuir
            List<RecipeDef.Ingredient> leatherArmorInputs = Arrays.asList(
                new RecipeDef.Ingredient("leather", 8)
            );
            recipes.put("leather_chestplate", new RecipeDef(
                "leather_chestplate",
                ProfessionType.LEATHERWORKER,
                5,
                "workbench",
                leatherArmorInputs,
                new RecipeDef.Ingredient("leather_chestplate", 1),
                30.0
            ));

            plugin.getLogger().info("[RecipeManager] " + recipes.size() + " recettes chargées.");
        } catch (Exception e) {
            plugin.getLogger().error("[RecipeManager] Erreur lors du chargement des recettes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Récupère une recette par son ID
     */
    public RecipeDef getRecipe(String id) {
        return recipes.get(id);
    }

    /**
     * Récupère toutes les recettes disponibles pour un atelier
     */
    public Collection<RecipeDef> getRecipesForWorkstation(String workstationId) {
        List<RecipeDef> list = new ArrayList<>();
        for (RecipeDef def : recipes.values()) {
            if (def.getWorkstationId().equals(workstationId)) {
                list.add(def);
            }
        }
        return list;
    }

    /**
     * Récupère toutes les recettes d'une profession
     */
    public Collection<RecipeDef> getRecipesForProfession(ProfessionType type) {
        List<RecipeDef> list = new ArrayList<>();
        for (RecipeDef def : recipes.values()) {
            if (def.getProfession() == type) {
                list.add(def);
            }
        }
        return list;
    }

    /**
     * Récupère toutes les recettes
     */
    public Collection<RecipeDef> getAllRecipes() {
        return new ArrayList<>(recipes.values());
    }
}
