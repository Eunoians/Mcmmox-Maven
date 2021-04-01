package us.eunoians.mcrpg.api.event.ability.mining;

import org.jetbrains.annotations.NotNull;
import us.eunoians.mcrpg.ability.Ability;
import us.eunoians.mcrpg.ability.impl.mining.richerores.RicherOres;
import us.eunoians.mcrpg.api.AbilityHolder;
import us.eunoians.mcrpg.api.event.ability.AbilityActivateEvent;

/**
 * This event is called whenever {@link RicherOres} attempts to activate following the event call of
 * {@link DoubleDropPreActivateEvent}.
 *
 * @author DiamondDagger590
 */
public class RicherOresActivateEvent extends AbilityActivateEvent {

    private double extraChance;

    /**
     * @param abilityHolder The {@link AbilityHolder} that is activating the event
     * @param ability       The {@link Ability} being activated
     */
    public RicherOresActivateEvent(@NotNull AbilityHolder abilityHolder, @NotNull RicherOres ability, double extraChance) {
        super(abilityHolder, ability, AbilityEventType.RECREATIONAL);
        this.extraChance = Math.max(0, extraChance);
    }

    /**
     * The {@link Ability} that is being activated by this event
     *
     * @return The {@link Ability} that is being activated by this event
     */
    @Override
    public @NotNull RicherOres getAbility() {
        return (RicherOres) super.getAbility();
    }

    /**
     * Gets the extra percent to be added to {@link DoubleDropPreActivateEvent#getActivationChance()}
     *
     * @return A positive zero inclusive percent to be added to {@link DoubleDropPreActivateEvent#getActivationChance()}
     */
    public double getExtraChance() {
        return extraChance;
    }

    /**
     * Sets the extra activation chance for {@link DoubleDropPreActivateEvent#getActivationChance()} to be checked to see
     * if it should activate.
     *
     * @param extraChance The extra percent to be added to {@link DoubleDropPreActivateEvent#getActivationChance()}
     */
    public void setExtraChance(double extraChance) {
        this.extraChance = Math.max(0, extraChance);
    }
}
