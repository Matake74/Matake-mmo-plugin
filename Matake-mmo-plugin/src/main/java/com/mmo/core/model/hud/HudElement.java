package com.mmo.core.model.hud;

/**
 * Représente un élément d'interface HUD
 */
public class HudElement {

    private final String text;
    private final int color;
    private final long creationTime;
    private final long duration; // 0 = permanent

    /**
     * Crée un élément HUD permanent
     */
    public HudElement(String text, int color) {
        this(text, color, 0);
    }

    /**
     * Crée un élément HUD temporaire
     * @param text Le texte à afficher
     * @param color La couleur (format hexadécimal, ex: 0xFF0000 pour rouge)
     * @param durationMs Durée d'affichage en millisecondes (0 = permanent)
     */
    public HudElement(String text, int color, long durationMs) {
        this.text = text;
        this.color = color;
        this.creationTime = System.currentTimeMillis();
        this.duration = durationMs;
    }

    public String getText() {
        return text;
    }

    public int getColor() {
        return color;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getDuration() {
        return duration;
    }

    /**
     * Vérifie si l'élément a expiré
     */
    public boolean isExpired() {
        if (duration == 0) return false; // Permanent
        return System.currentTimeMillis() >= (creationTime + duration);
    }

    /**
     * Temps restant avant expiration (en millisecondes)
     */
    public long getTimeRemaining() {
        if (duration == 0) return Long.MAX_VALUE; // Permanent
        long remaining = (creationTime + duration) - System.currentTimeMillis();
        return Math.max(0, remaining);
    }

    /**
     * Vérifie si l'élément est permanent
     */
    public boolean isPermanent() {
        return duration == 0;
    }

    @Override
    public String toString() {
        return "HudElement{" +
                "text='" + text + '\'' +
                ", color=" + String.format("0x%06X", color) +
                ", permanent=" + isPermanent() +
                '}';
    }
}
