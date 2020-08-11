package us.eunoians.mcrpg.api.util.fishing;

import org.bukkit.entity.Player;

public interface ChanceModifierFunction{
  
  public boolean useChance(Player player, Object requiredValue);
}
