package us.eunoians.mcmmox.skills;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import us.eunoians.mcmmox.abilities.BaseAbility;
import us.eunoians.mcmmox.Mcmmox;
import us.eunoians.mcmmox.players.McMMOPlayer;
import us.eunoians.mcmmox.types.GenericAbility;
import us.eunoians.mcmmox.types.Skills;

import java.util.HashMap;

/*
A class representation of the swords skill
 */
public class Swords extends Skill {

  /**
   * @param currentLevel The current level of the players swords skill
   * @param currentExp   The current exp amount of the players swords skill
   */
  public Swords(int currentLevel, int currentExp, HashMap<GenericAbility, BaseAbility> map, McMMOPlayer player) {
    super(Skills.SWORDS, map, currentLevel, currentExp, player);
  }

  /**
   * Get the exp worth 1 dmg of a mob
   *
   * @param type What entity you want the value of in exp
   * @return The amount of exp awarded for the provided mob
   */
  public static int getBaseExpAwarded(EntityType type) {
    return Mcmmox.getInstance().getMConfigManager().getSwordsConfig().getMobExpWorth(type);
  }

  /**
   * Get the multiplier of a weapon for exp
   *
   * @param material The material of the weapon used
   * @return
   */
  public static double getWeaponBonus(Material material) {
    if (!material.name().contains("SWORD")) {
      return 0;
    }
    return Mcmmox.getInstance().getSwordsConfig().getWeaponMultiplier(material);
  }
}