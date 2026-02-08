package com.mmo.core.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Player;

/**
 * Représente les menaces accumulées sur un mob spécifique
 * Stocke la menace de chaque joueur et gère la décroissance
 */
public class ThreatEntry {

    private final Player mobId;
    private final Map<Player, Double> threatMap = new HashMap<>();

    public ThreatEntry(Player mobId) {
        this.mobId = mobId;
    }

    public Player getMobId() {
        return mobId;
    }

    /**
     * Ajoute de la menace pour un joueur
     * @param Player Player du joueur
     * @param amount Montant de menace à ajouter (peut être négatif pour réduire)
     */
    public void addThreat(Player Player, double amount) {
        threatMap.merge(Player, amount, Double::sum);
        // Assurer que la menace ne descend jamais en dessous de 0
        threatMap.computeIfPresent(Player, (id, value) -> Math.max(0, value));
    }

    /**
     * Supprime complètement la menace d'un joueur
     * @param Player Player du joueur
     */
    public void removeThreat(Player Player) {
        threatMap.remove(Player);
    }

    /**
     * Réduit la menace de tous les joueurs (décroissance passive)
     * @param amount Montant à soustraire
     */
    public void decay(double amount) {
        threatMap.replaceAll((id, value) -> Math.max(0, value - amount));
    }

    /**
     * Récupère le joueur avec le plus de menace
     * @return Player du joueur avec le plus de menace, ou null si aucune menace
     */
    public Player getTopTarget() {
        return threatMap.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Récupère la menace d'un joueur spécifique
     * @param Player Player du joueur
     * @return Niveau de menace (0 si aucune menace)
     */
    public double getThreat(Player Player) {
        return threatMap.getOrDefault(Player, 0.0);
    }

    /**
     * Récupère toutes les menaces actives
     * @return Map immuable des menaces par joueur
     */
    public Map<Player, Double> getAllThreats() {
        return Collections.unmodifiableMap(threatMap);
    }

    /**
     * Vérifie si cette entrée n'a plus de menace active
     * @return true si toutes les menaces sont à 0 ou si aucun joueur n'est enregistré
     */
    public boolean isEmpty() {
        return threatMap.isEmpty() || 
               threatMap.values().stream().allMatch(v -> v <= 0);
    }

    /**
     * Réinitialise toutes les menaces
     */
    public void clear() {
        threatMap.clear();
    }
}
