package com.mmo.core.model.hud;

/**
 * Positions possibles pour l'affichage des éléments HUD
 */
public enum HudPosition {
    /**
     * Coin supérieur gauche
     */
    TOP_LEFT,
    
    /**
     * Haut centré
     */
    TOP_CENTER,
    
    /**
     * Coin supérieur droit
     */
    TOP_RIGHT,
    
    /**
     * Milieu gauche
     */
    MIDDLE_LEFT,
    
    /**
     * Centre de l'écran
     */
    CENTER,
    
    /**
     * Milieu droit
     */
    MIDDLE_RIGHT,
    
    /**
     * Coin inférieur gauche
     */
    BOTTOM_LEFT,
    
    /**
     * Bas centré
     */
    BOTTOM_CENTER,
    
    /**
     * Coin inférieur droit
     */
    BOTTOM_RIGHT,
    
    /**
     * Juste au-dessus du centre bas (pour barres d'action)
     */
    CENTER_BOTTOM;
    
    /**
     * Retourne la position opposée
     */
    public HudPosition getOpposite() {
        switch (this) {
            case TOP_LEFT: return BOTTOM_RIGHT;
            case TOP_CENTER: return BOTTOM_CENTER;
            case TOP_RIGHT: return BOTTOM_LEFT;
            case MIDDLE_LEFT: return MIDDLE_RIGHT;
            case CENTER: return CENTER;
            case MIDDLE_RIGHT: return MIDDLE_LEFT;
            case BOTTOM_LEFT: return TOP_RIGHT;
            case BOTTOM_CENTER: return TOP_CENTER;
            case BOTTOM_RIGHT: return TOP_LEFT;
            case CENTER_BOTTOM: return TOP_CENTER;
            default: return CENTER;
        }
    }
    
    /**
     * Vérifie si la position est sur le côté gauche
     */
    public boolean isLeft() {
        return this == TOP_LEFT || this == MIDDLE_LEFT || this == BOTTOM_LEFT;
    }
    
    /**
     * Vérifie si la position est sur le côté droit
     */
    public boolean isRight() {
        return this == TOP_RIGHT || this == MIDDLE_RIGHT || this == BOTTOM_RIGHT;
    }
    
    /**
     * Vérifie si la position est centrée horizontalement
     */
    public boolean isCentered() {
        return this == TOP_CENTER || this == CENTER || this == BOTTOM_CENTER || this == CENTER_BOTTOM;
    }
    
    /**
     * Vérifie si la position est en haut
     */
    public boolean isTop() {
        return this == TOP_LEFT || this == TOP_CENTER || this == TOP_RIGHT;
    }
    
    /**
     * Vérifie si la position est en bas
     */
    public boolean isBottom() {
        return this == BOTTOM_LEFT || this == BOTTOM_CENTER || this == BOTTOM_RIGHT || this == CENTER_BOTTOM;
    }
}
