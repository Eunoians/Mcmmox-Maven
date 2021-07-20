package us.eunoians.mcrpg.player;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.eunoians.mcrpg.api.AbilityHolder;
import us.eunoians.mcrpg.skill.AbstractSkill;
import us.eunoians.mcrpg.skill.SkillProgression;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * This class serves as an object that holds data regarding a player
 *
 * @author Kitsune/DiamondDagger590
 */
public class McRPGPlayer extends AbilityHolder {

    /**
     * The {@link Map} containing the {@link Player}'s skill progression for each skill
     */
    private Map<NamespacedKey, SkillProgression> skillProgression;

    /**
     * Construct a new {@link McRPGPlayer}.
     *
     * @param uniqueId the unique id of the player this object is representing
     */
    public McRPGPlayer(@NotNull UUID uniqueId) {
        super(uniqueId, true);
        this.skillProgression = new HashMap<>();

        //TODO populate skills
    }

    /**
     * Gets the players {@link SkillProgression} that is linked to the skill-type provided
     *
     * @param skillType the id of the skill
     *
     * @return an {@link Optional} containing the {@link SkillProgression} object for the specified skill.
     */
    public Optional<SkillProgression> getSkillProgression(@NotNull NamespacedKey skillType) {

        if (!skillProgression.containsKey(skillType)) return Optional.empty();
        return Optional.of(skillProgression.get(skillType));
    }

    /**
     * Gets the players {@link SkillProgression} that is linked to the {@link AbstractSkill} provided
     *
     * @param skill the skill
     *
     * @return an {@link Optional} containing the {@link SkillProgression} object for the specified skill
     */
    public Optional<SkillProgression> getSkillProgression(@NotNull AbstractSkill skill) {
        return getSkillProgression(skill.getId());
    }

    /**
     * Gets the {@link LivingEntity} that this object maps to
     *
     * @return The {@link LivingEntity} that this object maps to or {@code null} if invalid/dead/offline
     */
    @Override
    public @Nullable Player getEntity() {
        return (Player) super.getEntity();
    }
}