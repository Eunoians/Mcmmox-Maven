package us.eunoians.mcrpg.api.util.fishing;

import java.util.List;

/**
 * @author DiamondDagger590
 *
 * This is a functional interface for use in {@link ChanceFilterMode} enum values
 */
interface ChanceFilterEquation{
  
  /**
   * Gets a singular chance from a list of chances based on varying criteria implemented by {@link ChanceFilterMode}
   *
   * @param chances A {@link List} of potential chances to be filtered depending on the filter type
   * @return A chance to use post filter
   */
  double findChance(List<Double> chances);
}
