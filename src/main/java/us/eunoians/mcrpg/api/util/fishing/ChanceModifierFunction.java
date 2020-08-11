package us.eunoians.mcrpg.api.util.fishing;

import org.bukkit.entity.Player;

/**
 * @author DiamondDagger590
 *
 * This functional interface should be implemented by {@link ChanceModifierType} and allows for easy future implementation
 * of new chance modifiers without requiring much legacy rework if required
 */
interface ChanceModifierFunction{
  
  /**
   * Checks to see if the player should use a different chance that corresponds with this function
   * instead of whatever default chance is provided in the original implementation
   *
   * @param player The player to compare against the criteria
   * @param requiredValue The criteria to be processed by the {@link ChanceModifierType}
   * @return True if the new chance is valid to be used
   */
  boolean useChance(Player player, Object requiredValue);
}
