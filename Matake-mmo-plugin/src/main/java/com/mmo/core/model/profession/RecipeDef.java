package com.mmo.core.model.profession;

import java.util.List;

/**
 * Définition d'une recette de craft
 */
public class RecipeDef {

    public static class Ingredient {
        public final String itemId;
        public final int amount;

        public Ingredient(String itemId, int amount) {
            this.itemId = itemId;
            this.amount = amount;
        }
    }

    private final String id;
    private final ProfessionType profession;
    private final int minLevel;
    private final String workstationId;
    private final List<Ingredient> inputs;
    private final Ingredient output;
    private final double xp;

    public RecipeDef(String id, ProfessionType profession, int minLevel, String workstationId,
                     List<Ingredient> inputs, Ingredient output, double xp) {
        this.id = id;
        this.profession = profession;
        this.minLevel = minLevel;
        this.workstationId = workstationId;
        this.inputs = inputs;
        this.output = output;
        this.xp = xp;
    }

    public String getId() {
        return id;
    }

    public ProfessionType getProfession() {
        return profession;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public String getWorkstationId() {
        return workstationId;
    }

    public List<Ingredient> getInputs() {
        return inputs;
    }

    public Ingredient getOutput() {
        return output;
    }

    public double getXp() {
        return xp;
    }
}
