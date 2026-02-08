package com.mmo.core.api;

import com.hytale.server.Player.Player;
import com.mmo.core.CorePlugin;
import com.mmo.core.managers.ProfessionManager;
import com.mmo.core.model.profession.ProfessionType;
import java.util.Player;

/**
 * API publique pour gérer les professions et métiers
 * Utilise des Player au lieu de Player objects
 */
public class ProfessionAPI {

    // -----------------------------
    //  EXPÉRIENCE ET NIVEAUX
    // -----------------------------

    /**
     * Ajoute de l'expérience à une profession
     * @param Player L'Player du joueur
     * @param type Le type de profession
     * @param amount Montant d'XP à ajouter
     */
    public static void addXp(Player Player, ProfessionType type, double amount) {
        ProfessionManager pm = getManager();
        if (pm != null) {
            pm.addXp(Player, type, amount);
        }
    }

    /**
     * Récupère le niveau actuel d'une profession
     * @param Player L'Player du joueur
     * @param type Le type de profession
     * @return Le niveau de la profession
     */
    public static int getLevel(Player Player, ProfessionType type) {
        ProfessionManager pm = getManager();
        return pm != null ? pm.getLevel(Player, type) : 0;
    }

    /**
     * Vérifie si un joueur a le niveau requis dans une profession
     * @param Player L'Player du joueur
     * @param type Le type de profession
     * @param required Le niveau requis
     * @return true si le joueur a le niveau requis ou supérieur
     */
    public static boolean hasLevel(Player Player, ProfessionType type, int required) {
        ProfessionManager pm = getManager();
        return pm != null && pm.hasLevel(Player, type, required);
    }

    /**
     * Récupère l'expérience actuelle d'une profession
     * @param Player L'Player du joueur
     * @param type Le type de profession
     * @return L'expérience actuelle
     */
    public static double getXp(Player Player, ProfessionType type) {
        ProfessionManager pm = getManager();
        return pm != null ? pm.getXp(Player, type) : 0.0;
    }

    /**
     * Définit le niveau d'une profession (admin/debug)
     * @param Player L'Player du joueur
     * @param type Le type de profession
     * @param level Le niveau à définir
     */
    public static void setLevel(Player Player, ProfessionType type, int level) {
        ProfessionManager pm = getManager();
        if (pm != null) {
            pm.setLevel(Player, type, level);
        }
    }

    // -----------------------------
    //  RECETTES ET CRAFTING
    // -----------------------------

    /**
     * Vérifie si un joueur peut crafter une recette
     * @param Player L'Player du joueur
     * @param recipeId L'identifiant de la recette
     * @return true si le joueur peut crafter
     */
    public static boolean canCraft(Player Player, String recipeId) {
        ProfessionManager pm = getManager();
        return pm != null && pm.canCraft(Player, recipeId);
    }

    /**
     * Débloque une recette pour un joueur
     * @param Player L'Player du joueur
     * @param recipeId L'identifiant de la recette
     */
    public static void unlockRecipe(Player Player, String recipeId) {
        ProfessionManager pm = getManager();
        if (pm != null) {
            pm.unlockRecipe(Player, recipeId);
        }
    }

    /**
     * Vérifie si un joueur a débloqué une recette
     * @param Player L'Player du joueur
     * @param recipeId L'identifiant de la recette
     * @return true si la recette est débloquée
     */
    public static boolean hasRecipe(Player Player, String recipeId) {
        ProfessionManager pm = getManager();
        return pm != null && pm.hasRecipe(Player, recipeId);
    }

    // -----------------------------
    //  BONUS DE PROFESSION
    // -----------------------------

    /**
     * Calcule le bonus de récolte basé sur le niveau de profession
     * @param Player L'Player du joueur
     * @param type Le type de profession
     * @return Le multiplicateur de bonus (1.0 = pas de bonus)
     */
    public static double getGatheringBonus(Player Player, ProfessionType type) {
        ProfessionManager pm = getManager();
        return pm != null ? pm.getGatheringBonus(Player, type) : 1.0;
    }

    /**
     * Calcule le bonus de qualité de craft basé sur le niveau
     * @param Player L'Player du joueur
     * @param type Le type de profession
     * @return Le multiplicateur de qualité (1.0 = qualité de base)
     */
    public static double getCraftingQualityBonus(Player Player, ProfessionType type) {
        ProfessionManager pm = getManager();
        return pm != null ? pm.getCraftingQualityBonus(Player, type) : 1.0;
    }

    /**
     * Vérifie si le joueur a une chance de double récolte
     * @param Player L'Player du joueur
     * @param type Le type de profession
     * @return true si le joueur obtient une double récolte
     */
    public static boolean rollDoubleGather(Player Player, ProfessionType type) {
        ProfessionManager pm = getManager();
        return pm != null && pm.rollDoubleGather(Player, type);
    }

    // -----------------------------
    //  STATISTIQUES
    // -----------------------------

    /**
     * Récupère le nombre total de crafts effectués dans une profession
     * @param Player L'Player du joueur
     * @param type Le type de profession
     * @return Le nombre de crafts
     */
    public static int getTotalCrafts(Player Player, ProfessionType type) {
        ProfessionManager pm = getManager();
        return pm != null ? pm.getTotalCrafts(Player, type) : 0;
    }

    /**
     * Récupère le nombre total de ressources récoltées
     * @param Player L'Player du joueur
     * @param type Le type de profession
     * @return Le nombre de récoltes
     */
    public static int getTotalGathers(Player Player, ProfessionType type) {
        ProfessionManager pm = getManager();
        return pm != null ? pm.getTotalGathers(Player, type) : 0;
    }

    // -----------------------------
    //  HELPER
    // -----------------------------

    private static ProfessionManager getManager() {
        CorePlugin plugin = CorePlugin.getInstance();
        return plugin != null ? plugin.getProfessionManager() : null;
    }
}
