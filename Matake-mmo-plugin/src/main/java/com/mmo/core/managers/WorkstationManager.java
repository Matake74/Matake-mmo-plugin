package com.mmo.core.managers;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.mmo.core.model.profession.*;
import java.util.*;

/**
 * Gestionnaire des ateliers de craft
 */
public class WorkstationManager {

    private final JavaPlugin plugin;
    private final RecipeManager recipeManager;
    private final ProfessionManager professionManager;

    private final Map<String, WorkstationDef> workstations = new HashMap<>();

    public WorkstationManager(JavaPlugin plugin, RecipeManager recipeManager, ProfessionManager professionManager) {
        this.plugin = plugin;
        this.recipeManager = recipeManager;
        this.professionManager = professionManager;
        loadWorkstations();
    }

    /**
     * Charge les définitions d'ateliers depuis la configuration
     */
    private void loadWorkstations() {
        try {
            // TODO: Charger depuis config/workstations.yml via l'API Hytale
            // Pour l'instant, quelques ateliers d'exemple
            
            workstations.put("anvil", new WorkstationDef(
                "anvil",
                "Enclume",
                "hytale:anvil"
            ));

            workstations.put("workbench", new WorkstationDef(
                "workbench",
                "Établi",
                "hytale:crafting_table"
            ));

            workstations.put("furnace", new WorkstationDef(
                "furnace",
                "Fourneau",
                "hytale:furnace"
            ));

            workstations.put("cooking_pot", new WorkstationDef(
                "cooking_pot",
                "Marmite",
                "hytale:cooking_pot"
            ));

            plugin.getLogger().info("[WorkstationManager] " + workstations.size() + " ateliers chargés.");
        } catch (Exception e) {
            plugin.getLogger().error("[WorkstationManager] Erreur lors du chargement des ateliers: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Traite l'interaction avec un atelier
     * @return true si c'est un atelier géré
     */
    public boolean onWorkstationInteract(Player Player, String blockId) {
        WorkstationDef ws = workstations.values().stream()
            .filter(w -> w.getBlockId().equals(blockId))
            .findFirst()
            .orElse(null);

        if (ws == null) {
            return false; // Pas un atelier géré
        }

        // TODO: Ouvrir une GUI custom Hytale pour afficher les recettes
        // Pour l'instant, juste un log
        plugin.getLogger().info("Joueur " + Player + " a interagi avec " + ws.getName());

        return true;
    }

    /**
     * Traite le craft d'une recette
     * @return true si le craft a réussi
     */
    public boolean craft(Player Player, String recipeId) {
        RecipeDef recipe = recipeManager.getRecipe(recipeId);
        if (recipe == null) {
            // TODO: Envoyer message au joueur
            return false;
        }

        ProfessionType prof = recipe.getProfession();
        
        // Vérifier le niveau requis
        if (!professionManager.hasLevel(Player, prof, recipe.getMinLevel())) {
            // TODO: Envoyer message au joueur
            return false;
        }

        // TODO: Vérifier l'inventaire du joueur pour les ingrédients
        // TODO: Retirer les ingrédients de l'inventaire
        // TODO: Ajouter l'objet crafté à l'inventaire

        // Calculer la qualité du craft
        double qualityBonus = professionManager.getCraftingQualityBonus(Player, prof);
        // TODO: Appliquer le bonus de qualité à l'item crafté

        // Donner l'XP
        professionManager.addXp(Player, prof, recipe.getXp());
        professionManager.incrementCrafts(Player, prof);

        // TODO: Envoyer message de succès au joueur
        return true;
    }

    /**
     * Récupère un atelier par son ID
     */
    public WorkstationDef getWorkstation(String id) {
        return workstations.get(id);
    }

    /**
     * Récupère un atelier par son blockId
     */
    public WorkstationDef getWorkstationByBlock(String blockId) {
        return workstations.values().stream()
            .filter(w -> w.getBlockId().equals(blockId))
            .findFirst()
            .orElse(null);
    }

    /**
     * Récupère toutes les recettes disponibles pour un atelier
     */
    public List<RecipeDef> getAvailableRecipes(Player Player, String workstationId) {
        List<RecipeDef> available = new ArrayList<>();
        
        for (RecipeDef recipe : recipeManager.getRecipesForWorkstation(workstationId)) {
            ProfessionType prof = recipe.getProfession();
            if (professionManager.hasLevel(Player, prof, recipe.getMinLevel())) {
                available.add(recipe);
            }
        }
        
        return available;
    }

    /**
     * Récupère tous les ateliers
     */
    public Collection<WorkstationDef> getAllWorkstations() {
        return new ArrayList<>(workstations.values());
    }
}
