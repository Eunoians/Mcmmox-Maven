package us.eunoians.mcrpg.api.util.fishing;

import java.util.Arrays;
import java.util.List;

/**
 * @author DiamondDagger590
 *
 * This class serves as a provider of various filtering methods of a List containing doubles.
 *
 * The code also handles empty lists and will attempt to return 0.0d if there is nothing within the list.
 */
public enum ChanceFilterMode{
  
  //Get the highest chance out of the list
  HIGHEST("highest", ((List<Double> chances) -> {
    
    double highest = 0;
    for(Double chance : chances){
      highest = Math.max(chance, highest);
    }
    return highest;
  })),
  
  //Get the lowest chance out of the list
  LOWEST("lowest", ((List<Double> chances) -> {
    
    double lowest = Double.MAX_VALUE;
    for(Double chance : chances){
      lowest = Math.min(chance, lowest);
    }
    return lowest;
  })),
  
  //Get the first chance out of the list
  FIRST("first", ((List<Double> chances) -> chances.size() > 0 ? chances.get(0) : 0.0d)),
  
  //Get the last chance out of the list
  LAST("last", ((List<Double> chances) -> chances.size() > 0 ? chances.get(chances.size() - 1) : 0.0d)),
  
  //Get the average of all chances in the list
  AVERAGE("average", ((List<Double> chances) -> {
    
    double sum = 0;
    for(Double chance : chances){
      sum += chance;
    }
    return sum/chances.size();
  }))
  ;
  
  private String id; //The internal id of this filter mode
  private ChanceFilterEquation chanceFilterEquation; //The equation used to filter the results of a list
  
  /**
   *
   * @param id The {@link String} id mapped to this enum value
   * @param chanceFilterMode The {@link ChanceFilterEquation} used to filter the results of a list
   */
  ChanceFilterMode(String id, ChanceFilterEquation chanceFilterMode){
    this.id = id;
    this.chanceFilterEquation = chanceFilterMode;
  }
  
  /**
   * Gets the {@link ChanceFilterEquation} that this enum value implements
   *
   * @return The {@link ChanceFilterEquation} that this enum value implements
   */
  public ChanceFilterEquation getChanceFilterEquation(){
    return chanceFilterEquation;
  }
  
  /**
   * Gets the {@link ChanceFilterMode} associated with the {@link String} id
   *
   * @param id The {@link String} id mapped to the desired enum value
   * @return The {@link ChanceFilterMode} mapped to the provided id or null if invalid
   */
  public static ChanceFilterMode getFromID(String id){
    return Arrays.stream(values()).filter(chanceFilterMode -> chanceFilterMode.id.equalsIgnoreCase(id)).findFirst().orElse(null);
  }
}
