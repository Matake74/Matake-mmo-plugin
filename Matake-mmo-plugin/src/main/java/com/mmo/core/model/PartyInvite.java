package com.mmo.core.model;

import java.util.Player;

/**
 * Représente une invitation à rejoindre un groupe
 */
public class PartyInvite {

    private final Player inviterPlayer;
    private final Player invitedPlayer;
    private final Party party;
    private final long createdAt;
    private final long expiresAt;

    public PartyInvite(Player inviterPlayer, Player invitedPlayer, Party party, long expirationMs) {
        this.inviterPlayer = inviterPlayer;
        this.invitedPlayer = invitedPlayer;
        this.party = party;
        this.createdAt = System.currentTimeMillis();
        this.expiresAt = createdAt + expirationMs;
    }

    /**
     * Vérifie si l'invitation a expiré
     */
    public boolean isExpired() {
        return System.currentTimeMillis() > expiresAt;
    }

    /**
     * Temps restant avant expiration en secondes
     */
    public long getRemainingSeconds() {
        long remaining = expiresAt - System.currentTimeMillis();
        return Math.max(0, remaining / 1000);
    }

    // -----------------------------
    //  GETTERS
    // -----------------------------

    public Player getInviterPlayer() {
        return inviterPlayer;
    }

    public Player getInvitedPlayer() {
        return invitedPlayer;
    }

    public Party getParty() {
        return party;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    @Override
    public String toString() {
        return "PartyInvite{" +
                "inviter=" + inviterPlayer +
                ", invited=" + invitedPlayer +
                ", party=" + party.getPartyId() +
                ", remainingSeconds=" + getRemainingSeconds() +
                '}';
    }
}
