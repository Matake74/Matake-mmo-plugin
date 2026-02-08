package com.mmo.core.model.party;

import com.hytale.server.Player.Player;

/**
 * Représente une invitation à rejoindre un groupe
 */
public class PartyInvite {

    private final Player inviter;
    private final Player invited;
    private final Party party;
    private final long creationTime;
    private final long expirationTime;

    public PartyInvite(Player inviter, Player invited, Party party, long durationMs) {
        this.inviter = inviter;
        this.invited = invited;
        this.party = party;
        this.creationTime = System.currentTimeMillis();
        this.expirationTime = creationTime + durationMs;
    }

    public Player getInviter() {
        return inviter;
    }

    public Player getInvited() {
        return invited;
    }

    public Party getParty() {
        return party;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    /**
     * Vérifie si l'invitation a expiré
     */
    public boolean isExpired() {
        return System.currentTimeMillis() >= expirationTime;
    }

    /**
     * Temps restant avant expiration (en millisecondes)
     */
    public long getTimeRemaining() {
        long remaining = expirationTime - System.currentTimeMillis();
        return Math.max(0, remaining);
    }

    @Override
    public String toString() {
        return "PartyInvite{" +
                "inviter=" + inviter.getName() +
                ", invited=" + invited.getName() +
                ", expired=" + isExpired() +
                '}';
    }
}
