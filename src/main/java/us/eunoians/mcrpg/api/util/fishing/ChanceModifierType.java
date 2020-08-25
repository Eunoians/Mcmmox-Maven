package us.eunoians.mcrpg.api.util.fishing;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import uk.co.harieo.seasons.plugin.Seasons;
import us.eunoians.mcrpg.McRPG;
import us.eunoians.mcrpg.api.exceptions.McRPGPlayerNotFoundException;
import us.eunoians.mcrpg.players.McRPGPlayer;
import us.eunoians.mcrpg.players.PlayerManager;
import us.eunoians.mcrpg.types.Skills;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author DiamondDagger590
 *
 * This enum represents various modifiers that can affect the odds for various things. Only implementation at
 * time of writing is for fishing but could be expanded in the future.
 */
public enum ChanceModifierType{
  
  /*Takes in a player and either a string or an integer.
    If you have an integer, it checks to see if the time is exactly the same which isn't really reliable but it's there...
    Otherwise you can use predefined splices of time or day/night which encompass 12 hours each
   */
  TIME("time", (player, requiredValue) -> {
    
    if(requiredValue instanceof String){
      String string = (String) requiredValue;
      
      // 6 am - 6 pm
      if(string.equalsIgnoreCase("day")){
        return (player.getPlayerTime() >= 0 && player.getPlayerTime() <= 12000);
      }
      
      // 6 pm - 6 am
      else if(string.equalsIgnoreCase("night")){
        return (player.getPlayerTime() >= 12000 && player.getPlayerTime() <= 24000);
      }
      
      // 6 am - 10 am
      else if(string.equalsIgnoreCase("morning")){
        return (player.getPlayerTime() >= 0 && player.getPlayerTime() <= 4000);
      }
      
      // 10 am - 2 pm
      else if(string.equalsIgnoreCase("noon")){
        return (player.getPlayerTime() >= 4000 && player.getPlayerTime() <= 8000);
      }
      
      // 2 pm - 5 pm
      else if(string.equalsIgnoreCase("afternoon")){
        return (player.getPlayerTime() >= 8000 && player.getPlayerTime() <= 11000);
      }
      
      // 5 pm - 8 pm
      else if(string.equalsIgnoreCase("evening")){
        return (player.getPlayerTime() >= 11000 && player.getPlayerTime() <= 14000);
      }
      
      // 8 pm - 10 pm
      else if(string.equalsIgnoreCase("early_night")){
        return (player.getPlayerTime() >= 14000 && player.getPlayerTime() <= 16000);
      }
      
      // 10 pm - 2 am
      else if(string.equalsIgnoreCase("midnight")){
        return (player.getPlayerTime() >= 16000 && player.getPlayerTime() <= 20000);
      }
      
      // 2 am - 6 am
      else if(string.equalsIgnoreCase("late_night")){
        return (player.getPlayerTime() >= 20000 && player.getPlayerTime() <= 24000);
      }
    }
    
    //Handle tick specific value... idk why someone would use this but it's there
    else if(requiredValue instanceof Integer){
      return player.getPlayerTime() == ((Integer) requiredValue);
    }
    return false;
  }),
  
  //Handle biome specific modifiers
  BIOME("biome", (player, requiredValue) -> {
    
    if(requiredValue instanceof String){
      String string = (String) requiredValue;
      
      //Validate the biome
      return player.getLocation().getBlock().getBiome().toString().equalsIgnoreCase(string);
    }
    return false;
  }),
  
  //Check if a player has a certain level or higher in a skill. Since we can only pass in a singular object, we pass in a wrapper
  SKILL("skill", (player, requiredValue) -> {
    
    if(requiredValue instanceof ChanceSkillMetaData){
      
      //Extract our variables
      ChanceSkillMetaData chanceSkillMetaData = (ChanceSkillMetaData) requiredValue;
      McRPGPlayer mp;
      try{
        mp = PlayerManager.getPlayer(player.getUniqueId());
        
        //Check the levels
        return mp.getSkill(chanceSkillMetaData.getSkill()).getCurrentLevel() >= chanceSkillMetaData.getRequiredLevel();
      }
      catch(McRPGPlayerNotFoundException exception){
        return false;
      }
    }
    return false;
  }),
  
  //Check against world guard regions
  REGION("region", (player, requiredValue) -> {
    
    if(requiredValue instanceof String){
      
      String string = (String) requiredValue;
      Location location = player.getLocation();
  
      //We don't need to check if world guard isn't even enabled
      if(McRPG.getInstance().isWorldGuardEnabled()){
  
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager manager = container.get(BukkitAdapter.adapt(location.getWorld()));
  
        ApplicableRegionSet set = manager.getApplicableRegions(BukkitAdapter.asBlockVector(location));
        
        //Check to see if any regions match the region the player is in
        for(ProtectedRegion protectedRegion : set){
          if(protectedRegion.getId().equalsIgnoreCase(string)){
            return true;
          }
        }
        
      }
    }
    return false;
  }),
  
  //Handle weather checks
  WEATHER("weather", (player, requiredValue) -> {
    
    if(requiredValue instanceof String){
      String string = (String) requiredValue;
      
      //Check world weather first
      if(player.getPlayerWeather() == null){
        
        World world = player.getWorld();
        
        //Handle rain and snow
        if((string.equalsIgnoreCase("raining") || string.equalsIgnoreCase("snowing")) && world.hasStorm()){
          return WeatherWrapper.weatherToBiomes.get(string.toLowerCase()).contains(player.getLocation().getBlock().getBiome());
        }
        
        //Handle storms
        else if(string.equalsIgnoreCase("storming") && world.isThundering()){
          return WeatherWrapper.weatherToBiomes.get("storming").contains(player.getLocation().getBlock().getBiome());
        }
      }
      else{
        
        //Player specific weather doesn't appear to allow thunderstorms likely as lightning is server side so only care about rain and snow for player weather
        if((string.equalsIgnoreCase("raining") || string.equalsIgnoreCase("snowing")) && player.getPlayerWeather() == WeatherType.DOWNFALL){
          return WeatherWrapper.weatherToBiomes.get(string.toLowerCase()).contains(player.getLocation().getBlock().getBiome());
        }
      }
    }
    return false;
  }),
  
  //Handle integration with weather from Seasons: https://www.spigotmc.org/resources/seasons.39298/
  SEASON_WEATHER("season_weather", (player, requiredValue) -> {
    
    if(requiredValue instanceof String){
      String string = (String) requiredValue;
      
      if(Bukkit.getPluginManager().isPluginEnabled("Seasons")){
        
        //Check if the provided string is the current weather
        return Seasons.getInstance().getWorldCycle(player.getWorld()).getWeather().getName().equalsIgnoreCase(string);
      }
    }
    return false;
  }),
  
  //Handle integration with seasons from Seasons: https://www.spigotmc.org/resources/seasons.39298/
  SEASON("season", (player, requiredValue) -> {
    
    if(requiredValue instanceof String){
      String string = (String) requiredValue;
      
      //Check if the provided string is the current season
      if(Bukkit.getPluginManager().isPluginEnabled("Seasons")){
        return Seasons.getInstance().getWorldCycle(player.getWorld()).getSeason().getName().equalsIgnoreCase(string);
      }
    }
    return false;
  }),
  ;
  
  private String id; //The internal id
  private ChanceModifierFunction chanceModifier; //The functional interface that we can run later
  
  /**
   *
   * @param id The internal ID of the modifier
   * @param chanceModifier The functional interface that we can run later
   */
  ChanceModifierType(String id, ChanceModifierFunction chanceModifier){
    this.id = id;
    this.chanceModifier = chanceModifier;
  }
  
  /**
   * Gets the {@link ChanceModifierFunction} provided by this enum value
   *
   * @return The {@link ChanceModifierFunction} provided by this enum value
   */
  public ChanceModifierFunction getChanceModifier(){
    return this.chanceModifier;
  }
  
  /**
   * Gets the {@link String} id mapped to this enum value
   *
   * @return The {@link String} id mapped to this enum value
   */
  public String getID(){
    return this.id;
  }
  
  /**
   * Attempts to get a {@link ChanceModifierType} from the provided {@link String} id
   *
   * @param id The {@link String} that maps to an enum value
   * @return The {@link ChanceModifierType} mapped to the provided string id or null if there isn't one
   */
  public static ChanceModifierType getFromID(String id){
    return Arrays.stream(values()).filter(chanceModifierType -> chanceModifierType.getID().equalsIgnoreCase(id)).findFirst().orElse(null);
  }
  
  /**
   * A wrapper class to pass into the {@link ChanceModifierType#SKILL}
   */
  public static class ChanceSkillMetaData{
    
    private Skills skill; //The skill stored by this wrapper
    private int requiredLevel; //The minimum level to validate the functional interface
  
    /**
     *
     * @param skill The skill to be checked
     * @param requiredLevel The minimum level to validate the functional interface
     */
    public ChanceSkillMetaData(Skills skill, int requiredLevel){
      this.skill = skill;
      this.requiredLevel = requiredLevel;
    }
  
    /**
     * Gets the {@link Skills} to be checked
     * @return The {@link Skills} to be checked
     */
    public Skills getSkill(){
      return this.skill;
    }
  
    /**
     * Gets the minimum level to validate the functional interface
     * @return The minimum level to validate the functional interface
     */
    public int getRequiredLevel(){
      return this.requiredLevel;
    }
  }
  
  /**
   * Literally just a way to expose the static hashmap needed for usage of the functional interfaces later during runtime
   */
  public static class WeatherWrapper{
    
    //We need to move it here so the lambda's in the enum can reference these values. A workaround but it works
    public static final Map<String, Set<Biome>> weatherToBiomes = new HashMap<>();
  
    static {
      
      //Handle all raining biomes
      weatherToBiomes.put("raining", new HashSet<>(Arrays.asList(Biome.BAMBOO_JUNGLE, Biome.BAMBOO_JUNGLE_HILLS, Biome.BEACH, Biome.BIRCH_FOREST, Biome.BIRCH_FOREST_HILLS,
        Biome.DARK_FOREST, Biome.DARK_FOREST_HILLS, Biome.DEEP_LUKEWARM_OCEAN, Biome.FLOWER_FOREST, Biome.FOREST, Biome.GIANT_SPRUCE_TAIGA, Biome.GIANT_SPRUCE_TAIGA_HILLS, Biome.GIANT_TREE_TAIGA, Biome.GIANT_TREE_TAIGA_HILLS,
        Biome.GRAVELLY_MOUNTAINS, Biome.JUNGLE, Biome.JUNGLE_EDGE, Biome.JUNGLE_HILLS, Biome.LUKEWARM_OCEAN, Biome.MODIFIED_GRAVELLY_MOUNTAINS, Biome.MODIFIED_JUNGLE, Biome.MODIFIED_JUNGLE_EDGE, Biome.MOUNTAIN_EDGE,
        Biome.MOUNTAINS, Biome.MUSHROOM_FIELD_SHORE, Biome.MUSHROOM_FIELDS, Biome.OCEAN, Biome.PLAINS, Biome.RIVER, Biome.STONE_SHORE, Biome.SUNFLOWER_PLAINS, Biome.SWAMP, Biome.SWAMP_HILLS, Biome.TAIGA, Biome.TAIGA_HILLS,
        Biome.TALL_BIRCH_FOREST, Biome.TALL_BIRCH_HILLS, Biome.WARM_OCEAN, Biome.WOODED_HILLS, Biome.WOODED_MOUNTAINS)));
      
      //If it can rain, it can storm so just use the same set
      weatherToBiomes.put("storming", weatherToBiomes.get("raining"));
      
      //Setup the snowing bioms
      weatherToBiomes.put("snowing", new HashSet<>(Arrays.asList(Biome.COLD_OCEAN, Biome.DEEP_COLD_OCEAN, Biome.DEEP_FROZEN_OCEAN, Biome.FROZEN_OCEAN, Biome.FROZEN_RIVER, Biome.ICE_SPIKES, Biome.SNOWY_BEACH, Biome.SNOWY_MOUNTAINS,
        Biome.SNOWY_TAIGA, Biome.SNOWY_TAIGA_HILLS, Biome.SNOWY_TAIGA_MOUNTAINS, Biome.SNOWY_TUNDRA)));
    }
  }
}
