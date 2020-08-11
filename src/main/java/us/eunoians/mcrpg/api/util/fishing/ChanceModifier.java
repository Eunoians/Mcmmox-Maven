package us.eunoians.mcrpg.api.util.fishing;

import org.bukkit.entity.Player;

public class ChanceModifier{
  
  private ChanceModifierType chanceModifierType;
  private Object validationCriteria;
  private double chanceIfSuccessful;
  
  public ChanceModifier(ChanceModifierType chanceModifierType, Object validationCriteria, double chanceIfSuccessful){
    this.chanceModifierType = chanceModifierType;
    this.validationCriteria = validationCriteria;
    this.chanceIfSuccessful = chanceIfSuccessful;
  }
  
  public double getChanceIfSuccessful(){
    return this.chanceIfSuccessful;
  }
  
  public boolean checkCriteria(Player player){
    return chanceModifierType.getChanceModifier().useChance(player, validationCriteria);
  }
}
