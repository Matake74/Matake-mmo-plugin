package com.mmo.core.managers;

import com.hytale.server.Player.Player;
import com.mmo.core.CorePlugin;
import com.mmo.core.model.party.Party;
import com.mmo.core.model.party.PartyInvite;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestionnaire des groupes (parties)
 * Intégré dans CorePlugin
 * 
 * Règles :
 * - Seul le leader peut inviter/kicker des membres
 * - Seul le leader peut lancer des instances
 * - Si le leader quitte, un autre joueur devient chef automatiquement
 * - Si tous les joueurs quittent, le groupe est supprimé
 */
public class PartyManager {

    private final CorePlugin plugin;

    // Stockage des groupes actifs
    private final Map<Player, Party> parties;
    
    // Map joueur -> groupe pour accès rapide
    private final Map<Player, Party> PlayerParties;
    
    // Invitations en attente
    private final Map<Player, PartyInvite> pendingInvites;

    // Configuration
    private final int defaultMaxSize;
    private final long inviteExpirationMs;

    public PartyManager(CorePlugin plugin) {
        this.plugin = plugin;
        this.parties = new ConcurrentHashMap<>();
        this.PlayerParties = new ConcurrentHashMap<>();
        this.pendingInvites = new ConcurrentHashMap<>();
        
        // Configuration par défaut
        this.defaultMaxSize = 5;
        this.inviteExpirationMs = 60000; // 60 secondes

        // Tâche de nettoyage des invitations expirées
        startInviteCleanupTask();
        
        plugin.getLogger().info("[PartyManager] Initialisé avec succès.");
    }

    // -----------------------------
    //  CRÉATION ET DISSOLUTION
    // -----------------------------

    public boolean createParty(Player leader, int maxSize) {
        if (leader == null) return false;
        
        // Vérifier si le joueur est déjà dans un groupe
        if (isInParty(leader)) {
            leader.sendMessage("§cVous êtes déjà dans un groupe !");
            return false;
        }

        // Créer le groupe
        Party party = new Party(leader, maxSize);
        parties.put(party.getPartyId(), party);
        PlayerParties.put(leader.getPlayer(), party);

        leader.sendMessage("§aGroupe créé ! Vous êtes le leader.");
        plugin.getLogger().info("[Party] " + leader.getName() + " a créé un groupe (max: " + maxSize + ")");
        
        return true;
    }

    public boolean disbandParty(Player leader) {
        if (leader == null) return false;

        Party party = getParty(leader);
        if (party == null) {
            leader.sendMessage("§cVous n'êtes pas dans un groupe !");
            return false;
        }

        if (!party.isLeader(leader)) {
            leader.sendMessage("§cSeul le leader peut dissoudre le groupe !");
            return false;
        }

        // Notifier tous les membres
        broadcastToParty(leader, "§cLe groupe a été dissous par le leader.");
        
        // Retirer tous les membres
        for (Player member : party.getMembers()) {
            PlayerParties.remove(member.getPlayer());
        }

        // Supprimer le groupe
        parties.remove(party.getPartyId());
        
        plugin.getLogger().info("[Party] Groupe dissous par " + leader.getName());
        return true;
    }

    // -----------------------------
    //  INVITATIONS
    // -----------------------------

    public boolean invitePlayer(Player inviter, Player invited) {
        if (inviter == null || invited == null) return false;

        // Vérifier que l'inviter est dans un groupe
        Party party = getParty(inviter);
        if (party == null) {
            inviter.sendMessage("§cVous devez être dans un groupe pour inviter des joueurs !");
            return false;
        }

        // Vérifier que l'inviter est le leader
        if (!party.isLeader(inviter)) {
            inviter.sendMessage("§cSeul le leader peut inviter des joueurs !");
            return false;
        }

        // Vérifier que le groupe n'est pas plein
        if (party.isFull()) {
            inviter.sendMessage("§cVotre groupe est plein !");
            return false;
        }

        // Vérifier que l'invité n'est pas déjà dans un groupe
        if (isInParty(invited)) {
            inviter.sendMessage("§c" + invited.getName() + " est déjà dans un groupe !");
            return false;
        }

        // Vérifier qu'il n'y a pas déjà une invitation en attente
        if (pendingInvites.containsKey(invited.getPlayer())) {
            inviter.sendMessage("§c" + invited.getName() + " a déjà une invitation en attente !");
            return false;
        }

        // Créer l'invitation
        PartyInvite invite = new PartyInvite(inviter, invited, party, inviteExpirationMs);
        pendingInvites.put(invited.getPlayer(), invite);

        // Messages
        inviter.sendMessage("§aInvitation envoyée à " + invited.getName() + " !");
        invited.sendMessage("§e" + inviter.getName() + " vous invite à rejoindre son groupe !");
        invited.sendMessage("§e/party accept §7pour accepter §8| §e/party decline §7pour refuser");
        
        broadcastToPartyExcept(inviter, "§e" + invited.getName() + " a été invité dans le groupe.", invited);

        return true;
    }

    public boolean acceptInvite(Player Player) {
        if (Player == null) return false;

        PartyInvite invite = pendingInvites.get(Player.getPlayer());
        if (invite == null) {
            Player.sendMessage("§cVous n'avez aucune invitation en attente !");
            return false;
        }

        // Vérifier que l'invitation n'a pas expiré
        if (invite.isExpired()) {
            pendingInvites.remove(Player.getPlayer());
            Player.sendMessage("§cL'invitation a expiré !");
            return false;
        }

        Party party = invite.getParty();
        
        // Vérifier que le groupe existe toujours
        if (!parties.containsValue(party)) {
            pendingInvites.remove(Player.getPlayer());
            Player.sendMessage("§cCe groupe n'existe plus !");
            return false;
        }

        // Vérifier que le groupe n'est pas plein
        if (party.isFull()) {
            pendingInvites.remove(Player.getPlayer());
            Player.sendMessage("§cCe groupe est maintenant plein !");
            return false;
        }

        // Ajouter le joueur au groupe
        party.addMember(Player);
        PlayerParties.put(Player.getPlayer(), party);
        pendingInvites.remove(Player.getPlayer());

        // Messages
        Player.sendMessage("§aVous avez rejoint le groupe de " + party.getLeader().getName() + " !");
        broadcastToPartyExcept(Player, "§a" + Player.getName() + " a rejoint le groupe ! (" + 
                               party.getSize() + "/" + party.getMaxSize() + ")", Player);

        return true;
    }

    public boolean declineInvite(Player Player) {
        if (Player == null) return false;

        PartyInvite invite = pendingInvites.remove(Player.getPlayer());
        if (invite == null) {
            Player.sendMessage("§cVous n'avez aucune invitation en attente !");
            return false;
        }

        Player.sendMessage("§eInvitation refusée.");
        invite.getInviter().sendMessage("§e" + Player.getName() + " a refusé votre invitation.");
        
        return true;
    }

    // -----------------------------
    //  GESTION DES MEMBRES
    // -----------------------------

    public boolean kickMember(Player leader, Player target) {
        if (leader == null || target == null) return false;

        Party party = getParty(leader);
        if (party == null) {
            leader.sendMessage("§cVous n'êtes pas dans un groupe !");
            return false;
        }

        if (!party.isLeader(leader)) {
            leader.sendMessage("§cSeul le leader peut expulser des membres !");
            return false;
        }

        if (!party.isMember(target)) {
            leader.sendMessage("§c" + target.getName() + " n'est pas dans votre groupe !");
            return false;
        }

        if (leader.equals(target)) {
            leader.sendMessage("§cVous ne pouvez pas vous expulser vous-même ! Utilisez /party leave");
            return false;
        }

        // Retirer le membre
        party.removeMember(target);
        PlayerParties.remove(target.getPlayer());

        // Messages
        target.sendMessage("§cVous avez été expulsé du groupe !");
        broadcastToParty(leader, "§c" + target.getName() + " a été expulsé du groupe.");

        return true;
    }

    public boolean leaveParty(Player Player) {
        if (Player == null) return false;

        Party party = getParty(Player);
        if (party == null) {
            Player.sendMessage("§cVous n'êtes pas dans un groupe !");
            return false;
        }

        boolean wasLeader = party.isLeader(Player);
        
        // Retirer le joueur
        party.removeMember(Player);
        PlayerParties.remove(Player.getPlayer());

        // Si le groupe est vide, le supprimer
        if (party.isEmpty()) {
            parties.remove(party.getPartyId());
            Player.sendMessage("§eVous avez quitté le groupe (groupe dissous).");
            plugin.getLogger().info("[Party] Groupe dissous (dernier membre parti)");
            return true;
        }

        // Si c'était le leader, promouvoir quelqu'un d'autre
        if (wasLeader) {
            Player newLeader = party.promoteNextLeader();
            if (newLeader != null) {
                newLeader.sendMessage("§eVous êtes maintenant le leader du groupe !");
                broadcastToPartyExcept(newLeader, "§e" + newLeader.getName() + 
                                      " est maintenant le leader du groupe.", newLeader);
            }
        }

        Player.sendMessage("§eVous avez quitté le groupe.");
        broadcastToParty(Player, "§e" + Player.getName() + " a quitté le groupe. (" + 
                        party.getSize() + "/" + party.getMaxSize() + ")");

        return true;
    }

    public boolean promoteLeader(Player currentLeader, Player newLeader) {
        if (currentLeader == null || newLeader == null) return false;

        Party party = getParty(currentLeader);
        if (party == null) {
            currentLeader.sendMessage("§cVous n'êtes pas dans un groupe !");
            return false;
        }

        if (!party.isLeader(currentLeader)) {
            currentLeader.sendMessage("§cSeul le leader peut transférer le leadership !");
            return false;
        }

        if (!party.isMember(newLeader)) {
            currentLeader.sendMessage("§c" + newLeader.getName() + " n'est pas dans votre groupe !");
            return false;
        }

        if (currentLeader.equals(newLeader)) {
            currentLeader.sendMessage("§cVous êtes déjà le leader !");
            return false;
        }

        // Transférer le leadership
        party.setLeader(newLeader);

        // Messages
        newLeader.sendMessage("§aVous êtes maintenant le leader du groupe !");
        currentLeader.sendMessage("§eVous avez transféré le leadership à " + newLeader.getName());
        broadcastToPartyExcept(newLeader, "§e" + newLeader.getName() + 
                              " est maintenant le leader du groupe.", newLeader);

        return true;
    }

    // -----------------------------
    //  GETTERS
    // -----------------------------

    public boolean isInParty(Player Player) {
        return Player != null && PlayerParties.containsKey(Player.getPlayer());
    }

    public Party getParty(Player Player) {
        return Player != null ? PlayerParties.get(Player.getPlayer()) : null;
    }

    public boolean isLeader(Player Player) {
        Party party = getParty(Player);
        return party != null && party.isLeader(Player);
    }

    public Player getLeader(Player Player) {
        Party party = getParty(Player);
        return party != null ? party.getLeader() : null;
    }

    public List<Player> getMembers(Player Player) {
        Party party = getParty(Player);
        return party != null ? party.getMembers() : null;
    }

    public int getPartySize(Player Player) {
        Party party = getParty(Player);
        return party != null ? party.getSize() : 0;
    }

    public boolean isPartyFull(Player Player) {
        Party party = getParty(Player);
        return party != null && party.isFull();
    }

    public boolean hasPendingInvite(Player Player) {
        return Player != null && pendingInvites.containsKey(Player.getPlayer());
    }

    // -----------------------------
    //  UTILITAIRES
    // -----------------------------

    public void broadcastToParty(Player Player, String message) {
        Party party = getParty(Player);
        if (party == null) return;

        for (Player member : party.getMembers()) {
            member.sendMessage(message);
        }
    }

    public void broadcastToPartyExcept(Player Player, String message, Player except) {
        Party party = getParty(Player);
        if (party == null) return;

        for (Player member : party.getMembers()) {
            if (!member.equals(except)) {
                member.sendMessage(message);
            }
        }
    }

    // -----------------------------
    //  NETTOYAGE
    // -----------------------------

    private void startInviteCleanupTask() {
        // TODO: Adapter selon l'API Scheduler de Hytale
        // Tâche pour nettoyer les invitations expirées toutes les 10 secondes
        // plugin.getServer().getScheduler().scheduleRepeatingTask(() -> {
        //     pendingInvites.entrySet().removeIf(entry -> entry.getValue().isExpired());
        // }, 200L, 200L); // 10 secondes
    }

    /**
     * Appelé quand un joueur se déconnecte
     */
    public void handlePlayerDisconnect(Player Player) {
        // Retirer les invitations en attente
        pendingInvites.remove(Player.getPlayer());

        // Si le joueur est dans un groupe, le faire quitter
        if (isInParty(Player)) {
            leaveParty(Player);
        }
    }

    /**
     * Arrêt propre du manager
     */
    public void shutdown() {
        // Dissoudre tous les groupes
        for (Party party : parties.values()) {
            broadcastToParty(party.getLeader(), "§cLe serveur s'arrête, le groupe est dissous.");
        }
        
        parties.clear();
        PlayerParties.clear();
        pendingInvites.clear();
        
        plugin.getLogger().info("[PartyManager] Arrêté.");
    }
}
