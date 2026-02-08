package com.mmo.core;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.mmo.core.managers.*;
import com.mmo.core.commands.*;
import com.mmo.core.listeners.*;
import javax.annotation.Nonnull;
import com.hypixel.hytale.server.core.universe.PlayerRef;

/**
 * Plugin principal MMO-Core pour Hytale
 * Gère les systèmes de party, quêtes, waypoints, menaces, instances, professions et HUD
 */
public class CorePlugin extends JavaPlugin {

    private static CorePlugin instance;

    // Managers
    private PartyManager partyManager;
    private QuestManager questManager;
    private GroupQuestManager groupQuestManager;
    private WaypointManager waypointManager;
    private ThreatManager threatManager;
    private InstanceManager instanceManager;
    private ProfessionManager professionManager;
    private ResourceManager resourceManager;
    private RecipeManager recipeManager;
    private WorkstationManager workstationManager;
    private HudManager hudManager;

    // Listeners
    private ThreatListener threatListener;

    /**
     * Constructeur requis par l'API Hytale
     * @param init Objet d'initialisation fourni par le serveur
     */
    public CorePlugin(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
    }

    /**
     * Appelé avant le chargement complet - pour charger les configs de manière asynchrone
     */
    @Override
    protected void preLoad() {
        getLogger().info("[MMO-Core] Préchargement des configurations...");
        // Les configurations peuvent être chargées ici de manière asynchrone
    }

    /**
     * Appelé pendant la phase de setup - pour enregistrer les commandes, événements et assets
     */
    @Override
    protected void setup() {
        getLogger().info("[MMO-Core] Configuration du plugin...");
        
        // Initialisation des managers
        initializeManagers();
        
        // Enregistrement des commandes
        registerCommands();
        
        // Enregistrement des événements/listeners
        registerListeners();
        
        getLogger().info("[MMO-Core] Plugin configuré avec succès.");
    }

    /**
     * Appelé après que tous les plugins sont configurés - le plugin démarre
     */
    @Override
    protected void start() {
        getLogger().info("[MMO-Core] Démarrage du plugin...");
        
        // Initialiser les systèmes qui nécessitent que tous les plugins soient chargés
        if (hudManager != null) {
            hudManager.initialize();
        }
        
        getLogger().info("[MMO-Core] Plugin démarré avec succès.");
    }

    /**
     * Appelé lors de l'arrêt du plugin
     */
    @Override
    protected void shutdown() {
        getLogger().info("[MMO-Core] Arrêt du plugin...");
        
        // Sauvegarder les données avant l'arrêt
        if (partyManager != null) partyManager.shutdown();
        if (questManager != null) questManager.shutdown();
        if (threatManager != null) threatManager.shutdown();
        if (instanceManager != null) instanceManager.shutdown();
        if (professionManager != null) professionManager.shutdown();
        
        getLogger().info("[MMO-Core] Plugin arrêté.");
    }

    /**
     * Initialise tous les managers dans le bon ordre
     */
    private void initializeManagers() {
        try {
            // HUD d'abord (indépendant)
            this.hudManager = new HudManager(this);
            
            // Systèmes de base
            this.waypointManager = new WaypointManager(this);
            this.partyManager = new PartyManager(this);
            this.threatManager = new ThreatManager(this);
            
            // Systèmes qui dépendent des systèmes de base
            this.questManager = new QuestManager(this, waypointManager);
            this.groupQuestManager = new GroupQuestManager(this, questManager, waypointManager, partyManager);
            this.instanceManager = new InstanceManager(this, partyManager, questManager, threatManager);
            
            // Système de professions et crafting
            this.professionManager = new ProfessionManager(this);
            this.resourceManager = new ResourceManager(this, professionManager);
            this.recipeManager = new RecipeManager(this, professionManager);
            this.workstationManager = new WorkstationManager(this, recipeManager, professionManager);
            
            getLogger().info("[MMO-Core] Tous les managers initialisés.");
        } catch (Exception e) {
            getLogger().error("[MMO-Core] Erreur lors de l'initialisation des managers: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Enregistre toutes les commandes du plugin
     */
    private void registerCommands() {
        try {
            getCommandRegistry().registerCommand(new PartyCommand(partyManager));
            getCommandRegistry().registerCommand(new QuestCommand(questManager, groupQuestManager));
            getCommandRegistry().registerCommand(new WaypointCommand(waypointManager));
            getCommandRegistry().registerCommand(new InstanceCommand(instanceManager));
            getCommandRegistry().registerCommand(new ProfessionCommand(professionManager));
            
            getLogger().info("[MMO-Core] Commandes enregistrées.");
        } catch (Exception e) {
            getLogger().error("[MMO-Core] Erreur lors de l'enregistrement des commandes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Enregistre tous les listeners d'événements
     */
    private void registerListeners() {
        try {
            // Créer le listener de menaces
            this.threatListener = new ThreatListener(this);
            
            // Enregistrer les événements de menaces
            // TODO: Adapter selon l'API Event de Hytale
            // Exemples de ce qui devrait être enregistré :
            
            // getEventRegistry().register(EntityDamageEvent.class, threatListener::onEntityDamage);
            // getEventRegistry().register(EntityHealEvent.class, threatListener::onEntityHeal);
            // getEventRegistry().register(PlayerBlockEvent.class, threatListener::onPlayerBlock);
            // getEventRegistry().register(PlayerUseSkillEvent.class, threatListener::onPlayerUseSkill);
            
            // Listeners pour les ressources
            // getEventRegistry().register(ResourceHarvestEvent.class, resourceManager::onResourceHarvest);
            
            // Listeners pour le HUD
            // getEventRegistry().register(PlayerJoinEvent.class, hudManager::onPlayerJoin);
            
            // Listeners pour les instances
            // getEventRegistry().register(PlayerRespawnEvent.class, instanceManager::onPlayerRespawn);
            
            // Listeners pour les professions
            // getEventRegistry().register(CraftItemEvent.class, professionManager::onCraft);
            
            getLogger().info("[MMO-Core] Listeners enregistrés.");
        } catch (Exception e) {
            getLogger().error("[MMO-Core] Erreur lors de l'enregistrement des listeners: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==================== Getters ====================
    
    public static CorePlugin getInstance() {
        return instance;
    }

    public PartyManager getPartyManager() {
        return partyManager;
    }

    public QuestManager getQuestManager() {
        return questManager;
    }

    public GroupQuestManager getGroupQuestManager() {
        return groupQuestManager;
    }

    public WaypointManager getWaypointManager() {
        return waypointManager;
    }

    public ThreatManager getThreatManager() {
        return threatManager;
    }

    public InstanceManager getInstanceManager() {
        return instanceManager;
    }

    public ProfessionManager getProfessionManager() {
        return professionManager;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public RecipeManager getRecipeManager() {
        return recipeManager;
    }

    public WorkstationManager getWorkstationManager() {
        return workstationManager;
    }

    public HudManager getHudManager() {
        return hudManager;
    }

    public ThreatListener getThreatListener() {
        return threatListener;
    }
}
