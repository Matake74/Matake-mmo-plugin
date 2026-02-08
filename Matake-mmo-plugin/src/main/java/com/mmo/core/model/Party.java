package com.mmo.core.model;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Représente un groupe de joueurs
 */
public class Party {

    private final Player partyId;
    private Player leaderPlayer;
    private final int maxSize;
    private final Set<Player> members;
    private final long createdAt;

    public Party(com.hypixel.hytale.server.Player.Player leader, int maxSize) {
        this.partyId = Player.randomPlayer();
        this.leaderPlayer = leader.getPlayer();
        this.maxSize = maxSize;
        this.members = ConcurrentHashMap.newKeySet();
        this.members.add(leader.getPlayer());
        this.createdAt = System.currentTimeMillis();
    }

    // -----------------------------
    //  MEMBRES
    // -----------------------------

    public void addMember(Player PlayerPlayer) {
        if (members.size() < maxSize) {
            members.add(PlayerPlayer);
        }
    }

    public void removeMember(Player PlayerPlayer) {
        members.remove(PlayerPlayer);
    }

    public boolean isMember(Player PlayerPlayer) {
        return members.contains(PlayerPlayer);
    }

    public Set<Player> getMemberPlayers() {
        return new HashSet<>(members);
    }

    public int getSize() {
        return members.size();
    }

    public boolean isFull() {
        return members.size() >= maxSize;
    }

    public boolean isEmpty() {
        return members.isEmpty();
    }

    // -----------------------------
    //  LEADERSHIP
    // -----------------------------

    public boolean isLeader(Player PlayerPlayer) {
        return leaderPlayer.equals(PlayerPlayer);
    }

    public Player getLeaderPlayer() {
        return leaderPlayer;
    }

    public void setLeader(Player newLeaderPlayer) {
        if (members.contains(newLeaderPlayer)) {
            this.leaderPlayer = newLeaderPlayer;
        }
    }

    /**
     * Promeut le prochain membre comme leader
     * @return L'Player du nouveau leader ou null si le groupe est vide
     */
    public Player promoteNextLeader() {
        if (members.isEmpty()) return null;
        
        // Trouver le premier membre qui n'est pas l'ancien leader
        for (Player memberPlayer : members) {
            if (!memberPlayer.equals(leaderPlayer)) {
                this.leaderPlayer = memberPlayer;
                return memberPlayer;
            }
        }
        
        // Si on arrive ici, tous les membres sont le même joueur (edge case)
        return members.iterator().next();
    }

    // -----------------------------
    //  GETTERS
    // -----------------------------

    public Player getPartyId() {
        return partyId;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Party party = (Party) o;
        return partyId.equals(party.partyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partyId);
    }

    @Override
    public String toString() {
        return "Party{" +
                "id=" + partyId +
                ", leader=" + leaderPlayer +
                ", size=" + members.size() + "/" + maxSize +
                '}';
    }
}
