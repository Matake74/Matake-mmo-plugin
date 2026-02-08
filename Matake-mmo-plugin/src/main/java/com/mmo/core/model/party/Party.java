package com.mmo.core.model.party;

import com.hytale.server.Player.Player;

import java.util.*;

/**
 * Représente un groupe de joueurs (party)
 */
public class Party {

    private final Player partyId;
    private Player leader;
    private final List<Player> members;
    private final int maxSize;
    private final long creationTime;

    public Party(Player leader, int maxSize) {
        this.partyId = Player.randomPlayer();
        this.leader = leader;
        this.members = new ArrayList<>();
        this.members.add(leader);
        this.maxSize = maxSize;
        this.creationTime = System.currentTimeMillis();
    }

    // -----------------------------
    //  GETTERS
    // -----------------------------

    public Player getPartyId() {
        return partyId;
    }

    public Player getLeader() {
        return leader;
    }

    public List<Player> getMembers() {
        return new ArrayList<>(members);
    }

    public int getSize() {
        return members.size();
    }

    public int getMaxSize() {
        return maxSize;
    }

    public long getCreationTime() {
        return creationTime;
    }

    // -----------------------------
    //  MODIFICATIONS
    // -----------------------------

    public void setLeader(Player newLeader) {
        if (members.contains(newLeader)) {
            this.leader = newLeader;
        }
    }

    public void addMember(Player Player) {
        if (!isFull() && !members.contains(Player)) {
            members.add(Player);
        }
    }

    public void removeMember(Player Player) {
        members.remove(Player);
        
        // Si le leader part et qu'il reste des membres, promouvoir quelqu'un
        if (Player.equals(leader) && !members.isEmpty()) {
            leader = members.get(0);
        }
    }

    /**
     * Promeut le prochain membre comme leader
     * @return Le nouveau leader ou null si le groupe est vide
     */
    public Player promoteNextLeader() {
        if (members.isEmpty()) return null;
        
        leader = members.get(0);
        return leader;
    }

    // -----------------------------
    //  VÉRIFICATIONS
    // -----------------------------

    public boolean isLeader(Player Player) {
        return leader != null && leader.equals(Player);
    }

    public boolean isMember(Player Player) {
        return members.contains(Player);
    }

    public boolean isFull() {
        return members.size() >= maxSize;
    }

    public boolean isEmpty() {
        return members.isEmpty();
    }

    /**
     * Vérifie si tous les membres sont en ligne
     */
    public boolean allMembersOnline() {
        for (Player member : members) {
            if (!member.isOnline()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Récupère uniquement les membres en ligne
     */
    public List<Player> getOnlineMembers() {
        List<Player> online = new ArrayList<>();
        for (Player member : members) {
            if (member.isOnline()) {
                online.add(member);
            }
        }
        return online;
    }

    // -----------------------------
    //  UTILITAIRES
    // -----------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Party party = (Party) o;
        return partyId.equals(party.partyId);
    }

    @Override
    public int hashCode() {
        return partyId.hashCode();
    }

    @Override
    public String toString() {
        return "Party{" +
                "id=" + partyId +
                ", leader=" + (leader != null ? leader.getName() : "null") +
                ", size=" + members.size() +
                "/" + maxSize +
                '}';
    }
}
