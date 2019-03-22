package us.eunoians.mcrpg;

import com.cyr1en.mcutils.PluginUpdater;
import com.cyr1en.mcutils.initializers.Initializable;
import com.cyr1en.mcutils.initializers.annotation.Ignore;
import com.cyr1en.mcutils.initializers.annotation.Initialize;
import com.cyr1en.mcutils.initializers.annotation.process.Initializer;
import com.cyr1en.mcutils.logger.Logger;
import com.google.common.base.Charsets;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import us.eunoians.mcrpg.api.displays.DisplayManager;
import us.eunoians.mcrpg.api.util.*;
import us.eunoians.mcrpg.commands.*;
import us.eunoians.mcrpg.events.mcrpg.*;
import us.eunoians.mcrpg.events.vanilla.*;
import us.eunoians.mcrpg.players.PlayerManager;
import us.eunoians.mcrpg.util.blockmeta.chunkmeta.ChunkManager;
import us.eunoians.mcrpg.util.blockmeta.chunkmeta.ChunkManagerFactory;
import us.eunoians.mcrpg.util.worldguard.WGSupportManager;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

//:>
public class McRPG extends JavaPlugin implements Initializable {

  @Ignore private static McRPG instance;
  @Getter private PluginUpdater pluginUpdater;
  @Getter private FileManager fileManager;
  @Getter private DisplayManager displayManager;
  @Getter private static ChunkManager placeStore;
  @Getter private RemoteTransferTracker remoteTransferTracker;
  //Needed to support McMMO's Healthbars
  @Getter private final String customNameKey = "mcMMO: Custom Name";
  @Getter private final String customVisibleKey = "mcMMO: Name Visibility";
  @Getter private boolean healthBarPluginEnabled = false;
  @Getter private boolean mvdwEnabled = false;
  @Getter private boolean papiEnabled = false;
  @Getter private boolean worldGuardEnabled = false;
  @Getter @Setter private WGSupportManager wgSupportManager;


  @Override
  public void onEnable() {
    Bukkit.getScheduler().runTaskLater(this, () -> Initializer.initAll(this), 1L);
  }

  @Override
  public void onDisable() {
    if(!Initializer.finished())
      Initializer.interrupt();
    PlayerManager.shutDownManager();
  }

  @Initialize(priority = 0)
  private void preInit() {
    Logger.init("McRPG");
    /*var configManager = new ConfigManager(this);
    mConfigManager = new MConfigManager(configManager);
    /*if (!mConfigManager.setupConfigs(
            GeneralConfig.class, SwordsConfig.class))
      getServer().shutdown();*/
    //Logger.setDebugMode(mConfigManager.getGeneralConfig().isDebugMode());
    //Locale.init(mConfigManager);
  }

  @SuppressWarnings("Duplicates")
  @Initialize(priority = 2)
  private void initPrimaryInstance() {
    //localizationFiles = new LocalizationFiles(this, true);
    instance = this;
    fileManager = FileManager.getInstance().setup(this);
    healthBarPluginEnabled = getServer().getPluginManager().getPlugin("HealthBar") != null;
    if(healthBarPluginEnabled) {
      getLogger().info("HealthBar plugin found, McRPG's healthbars are automatically disabled.");
    }
    if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
      papiEnabled = true;
      getLogger().info("Papi PlaceholderAPI found... registering hooks");
      new McRPGPlaceHolders().register();
    }
    if(Bukkit.getPluginManager().isPluginEnabled("WorldGuard")){
      worldGuardEnabled = true;
      wgSupportManager = new WGSupportManager(this);
    }
    remoteTransferTracker = new RemoteTransferTracker();
    placeStore = ChunkManagerFactory.getChunkManager(); // Get our ChunkletManager
    File folder = new File(getDataFolder(), File.separator + "PlayerData");
    if(!folder.exists()) {
      folder.mkdir();
    }
    displayManager = DisplayManager.getInstance();
    DiamondFlowersData.init();
    HiddenConfig.getInstance();
    PlayerManager.startSave(this);
  }

  @Initialize(priority = 3)
  private void initCmds() {
    getCommand("mcrpg").setExecutor(new McRPGStub());
    getCommand("mcdisplay").setExecutor(new McDisplay());
    getCommand("mcadmin").setExecutor(new McAdmin());
    getCommand("mclink").setExecutor(new McLink());
    getCommand("mcunlink").setExecutor(new McUnlink());
    getCommand("mchelp").setExecutor(new McHelp());
  }

  @Initialize(priority = 4)
  private void initListener() {
    getServer().getPluginManager().registerEvents(new PlayerLoginEvent(), this);
    getServer().getPluginManager().registerEvents(new MoveEvent(), this);
    getServer().getPluginManager().registerEvents(new PlayerLogoutEvent(), this);
    getServer().getPluginManager().registerEvents(new InvClickEvent(this), this);
    getServer().getPluginManager().registerEvents(new AbilityActivate(), this);
    getServer().getPluginManager().registerEvents(new McRPGPlayerLevelChange(), this);
    getServer().getPluginManager().registerEvents(new VanillaDamageEvent(), this);
    getServer().getPluginManager().registerEvents(new McRPGExpGain(), this);
    getServer().getPluginManager().registerEvents(new InvCloseEvent(), this);
    getServer().getPluginManager().registerEvents(new BleedHandler(), this);
    getServer().getPluginManager().registerEvents(new CheckReadyEvent(), this);
    getServer().getPluginManager().registerEvents(new ShiftToggle(), this);
    getServer().getPluginManager().registerEvents(new BlockListener(this), this);
    getServer().getPluginManager().registerEvents(new WorldListener(this), this);
    getServer().getPluginManager().registerEvents(new McLink(), this);
    getServer().getPluginManager().registerEvents(new BreakEvent(), this);
    getServer().getPluginManager().registerEvents(new AbilityUpgrade(), this);
    getServer().getPluginManager().registerEvents(new LoadoutAdd(), this);
    getServer().getPluginManager().registerEvents(new InteractHandler(), this);
    getServer().getPluginManager().registerEvents(new DisarmHandler(), this);
    getServer().getPluginManager().registerEvents(new PlayerNomNomEvent(), this);
    getServer().getPluginManager().registerEvents(new AbilityUnlock(), this);
    getServer().getPluginManager().registerEvents(new PickupEvent(), this);
    getServer().getPluginManager().registerEvents(new DropItemEvent(), this);
    getServer().getPluginManager().registerEvents(new ShootEvent(), this);
    getServer().getPluginManager().registerEvents(new ArrowHitEvent(), this);
  }

  public static McRPG getInstance() {
    if(instance == null)
      throw new NullPointerException("Plugin was not initialized.");
    return instance;
  }

  public String getPluginPrefix() {
    return getLangFile().getString("Messages.PluginInfo.PluginPrefix");
  }

  public FileManager getFileManager() {
    return fileManager;
  }

  @Override
  public FileConfiguration getConfig() {
    return fileManager.getFile(FileManager.Files.CONFIG);
  }

  public FileConfiguration getLangFile() {
    return FileManager.Files.fromString(getConfig().getString("Configuration.LangFile")).getFile();
  }

  public InputStreamReader getResourceAsReader(String fileName) {
    InputStream in = getResource(fileName);
    return in == null ? null : new InputStreamReader(in, Charsets.UTF_8);
  }
}
