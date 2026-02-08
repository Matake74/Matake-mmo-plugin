package com.mmo.core.model.profession;

/**
 * Définition d'une profession avec ses paramètres
 */
public class ProfessionDef {

    private final ProfessionType type;
    private final String name;
    private final int maxLevel;
    private final String xpCurve;

    public ProfessionDef(ProfessionType type, String name, int maxLevel, String xpCurve) {
        this.type = type;
        this.name = name;
        this.maxLevel = maxLevel;
        this.xpCurve = xpCurve;
    }

    public ProfessionType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public String getXpCurve() {
        return xpCurve;
    }
}
