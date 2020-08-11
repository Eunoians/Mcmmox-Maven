package us.eunoians.mcrpg.api.util.fishing;

import org.bukkit.entity.Player;

/**
 * @author DiamondDagger590
 *
 * Exposes a limited amount of funcitonality of {@link ChanceModifierType} and contains the required objects
 * to process for overall cleanliness
 */
public class ChanceModifier{
  
  private ChanceModifierType chanceModifierType; //The enum type to process the criteria
  private Object validationCriteria; //The criteria to be processed against the player
  private double chanceIfSuccessful; //If the criteria passes, then the chance that will be returned
  
  /**
   *
   * @param chanceModifierType The enum type to process the criteria
   * @param validationCriteria The criteria to be processed against the player
   * @param chanceIfSuccessful The chance to be returned should the criteria pass
   */
  public ChanceModifier(ChanceModifierType chanceModifierType, Object validationCriteria, double chanceIfSuccessful){
    this.chanceModifierType = chanceModifierType;
    this.validationCriteria = validationCriteria;
    this.chanceIfSuccessful = chanceIfSuccessful;
  }
  
  /**
   * Gets the chance to be used if the criteria passed
   *
   * @return The chance to be used if the criteria passed
   */
  public double getChanceIfSuccessful(){
    return this.chanceIfSuccessful;
  }
  
  /**
   * Checks to see if {@link ChanceModifier#getChanceIfSuccessful()} should be used over whatever default chance
   *
   * @param player The player to check the criteria against
   * @return True if the player passed the criteria and should use the provided chance
   */
  public boolean checkCriteria(Player player){
    return chanceModifierType.getChanceModifier().useChance(player, validationCriteria);
  }
}
