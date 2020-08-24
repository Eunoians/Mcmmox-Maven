package us.eunoians.mcrpg.api.util.fishing;

import lombok.Getter;
import org.bukkit.Material;
import us.eunoians.mcrpg.api.util.Methods;
import us.eunoians.mcrpg.types.Skills;
import us.eunoians.mcrpg.types.UnlockedAbilities;
import us.eunoians.mcrpg.util.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static us.eunoians.mcrpg.api.util.fishing.FishingItemManager.getFishingLootConfig;

public class FishingItem {

  @Getter private Material itemType;
  @Getter private Parser chance;
  @Getter private int lowEndAmount;
  @Getter private int highEndAmount;
//  @Getter private int lowEndDurability;
 // @Getter private int highEndDurability;
  @Getter private int lowEndVanillaExpAmount;
  @Getter private int highEndVanillaExpAmount;
  @Getter private int mcrpgExpValue;
  @Getter private String displayName;
  @Getter private List<String> lore;
  @Getter private EnchantmentMeta enchantmentMeta;
  @Getter private PotionMeta potionMeta;
  @Getter private ChanceFilterMode filterMode;
  @Getter private List<ChanceModifier> chanceModifiers = new ArrayList<>();
  @Getter private Map<UnlockedAbilities, FishingItemDep> dependancies = new HashMap<>();

  public FishingItem(String filePath){
    
    this.itemType = Material.getMaterial(getFishingLootConfig().getString(filePath + "Material", "AIR"));
    this.chance = new Parser(getFishingLootConfig().getString(filePath + "Chance", "1.0"));
    
    if(getFishingLootConfig().contains(filePath + "ChanceModifiers")){
      
      String tempKey = filePath + "ChanceModifiers.Filters";
      filterMode = ChanceFilterMode.getFromID(getFishingLootConfig().getString(tempKey + ".FilterMode", "highest"));
      
      for(String filter : getFishingLootConfig().getConfigurationSection(tempKey).getKeys(false)){
      
        String subKey = tempKey + "." + filter + ".";
        System.out.println(subKey);
        double chance = getFishingLootConfig().getDouble(subKey + "Chance", 0.0);
        String[] filterInfo = getFishingLootConfig().getString(subKey + "Filter").split(":");
        
        Object validationCriteria;
        
        ChanceModifierType chanceModifierType = ChanceModifierType.getFromID(filterInfo[0]);
        
        if(chanceModifierType == ChanceModifierType.SKILL){
          Skills skills = Skills.fromString(filterInfo[1]);
          Integer minimumLevel = Integer.parseInt(filterInfo[2]);
  
          validationCriteria = new ChanceModifierType.ChanceSkillMetaData(skills, minimumLevel);
        }
        else if(Methods.isInt(filterInfo[1])){
          validationCriteria = Integer.parseInt(filterInfo[1]);
        }
        else{
          validationCriteria = filterInfo[1];
        }
        
        ChanceModifier chanceModifier = new ChanceModifier(chanceModifierType, validationCriteria, chance);
        chanceModifiers.add(chanceModifier);
      }
    }
    
    String[] amountRange = getFishingLootConfig().getString(filePath + "Amount", "1").split("-");
    
    this.lowEndAmount = Integer.parseInt(amountRange[0]);
    this.highEndAmount = amountRange.length > 1 ? Integer.parseInt(amountRange[1]) : lowEndAmount;
    
    String[] expRange = getFishingLootConfig().getString(filePath + "VanillaExp", "0").split("-");
    this.lowEndVanillaExpAmount = Integer.parseInt(expRange[0]);
    this.highEndVanillaExpAmount = expRange.length > 1 ? Integer.parseInt(expRange[1]) : highEndAmount;
    this.mcrpgExpValue = getFishingLootConfig().getInt(filePath + "McRPGExp", 0);
    
    if(getFishingLootConfig().contains(filePath + "DisplayName")) {
      this.displayName = getFishingLootConfig().getString(filePath + "DisplayName");
    }
    if(getFishingLootConfig().contains(filePath + "Lore")){
      this.lore = getFishingLootConfig().getStringList(filePath + "Lore");
    }
    if(getFishingLootConfig().contains(filePath + "EnchantmentMeta")){
      this.enchantmentMeta = new EnchantmentMeta(filePath + "EnchantmentMeta.");
    }
    if(getFishingLootConfig().contains(filePath + "PotionMeta")){
      this.potionMeta = new PotionMeta(filePath + "PotionMeta.");
    }
    
    if(getFishingLootConfig().contains(filePath + "Dependencies")){
      for(String dep : getFishingLootConfig().getConfigurationSection(filePath + "Dependencies").getKeys(false)){
        if(UnlockedAbilities.isAbility(dep)){
          UnlockedAbilities unlockedAbility = UnlockedAbilities.fromString(dep);
          FishingItemDep fishingItemDep = new FishingItemDep(filePath + "Dependencies." + dep + ".");
          dependancies.put(unlockedAbility, fishingItemDep);
        }
        else{
          //TODO log error
        }
      }
    }
  }
}
