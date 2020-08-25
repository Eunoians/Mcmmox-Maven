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
  
  /**
   * Returns a string representation of the object. In general, the
   * {@code toString} method returns a string that
   * "textually represents" this object. The result should
   * be a concise but informative representation that is easy for a
   * person to read.
   * It is recommended that all subclasses override this method.
   * <p>
   * The {@code toString} method for class {@code Object}
   * returns a string consisting of the name of the class of which the
   * object is an instance, the at-sign character `{@code @}', and
   * the unsigned hexadecimal representation of the hash code of the
   * object. In other words, this method returns a string equal to the
   * value of:
   * <blockquote>
   * <pre>
   * getClass().getName() + '@' + Integer.toHexString(hashCode())
   * </pre></blockquote>
   *
   * @return a string representation of the object.
   */
  @Override
  public String toString(){
    return "[" + chanceModifierType.getID() + "-" + validationCriteria.toString() + "-" + chanceIfSuccessful + "]";
  }
}
