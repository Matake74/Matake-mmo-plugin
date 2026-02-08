package com.mmo.core.model.instance;

/**
 * Types d'instances disponibles
 * Définit les modes de jeu pour les donjons et raids
 */
public enum InstanceType {
    /**
     * Instance solo - 1 joueur uniquement
     */
    SOLO,

    /**
     * Instance de groupe - 2 à 5 joueurs
     */
    GROUP,

    /**
     * Instance de raid - 5 à 10 joueurs
     */
    RAID;

    /**
     * Vérifie si ce type nécessite un groupe/party
     */
    public boolean requiresParty() {
        return this == GROUP || this == RAID;
    }

    /**
     * Retourne le nombre minimum recommandé de joueurs
     */
    public int getDefaultMinPlayers() {
        switch (this) {
            case SOLO: return 1;
            case GROUP: return 2;
            case RAID: return 5;
            default: return 1;
        }
    }

    /**
     * Retourne le nombre maximum recommandé de joueurs
     */
    public int getDefaultMaxPlayers() {
        switch (this) {
            case SOLO: return 1;
            case GROUP: return 5;
            case RAID: return 10;
            default: return 1;
        }
    }
}