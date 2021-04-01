package us.eunoians.mcrpg.api.event.ability.mining;

import org.jetbrains.annotations.NotNull;
import us.eunoians.mcrpg.ability.Ability;
import us.eunoians.mcrpg.ability.impl.mining.doubledrop.DoubleDrop;
import us.eunoians.mcrpg.api.AbilityHolder;
import us.eunoians.mcrpg.api.event.ability.AbilityEvent;

/**
 * This event is called before {@link DoubleDrop} activates ad calls {@link DoubleDropActivateEvent}.
 *
 * This event primarily exists as a way for {@link us.eunoians.mcrpg.ability.impl.mining.richerores.RicherOres} to modify
 * the activation rate for {@link DoubleDrop} while still adhering to the event driven ability activation style this iteration
 * of McRPG is using.
 *
 * @author DiamondDagger590
 */
public class DoubleDropPreActivateEvent extends AbilityEvent {

    private double activationChance;

    public DoubleDropPreActivateEvent(@NotNull AbilityHolder abilityHolder, @NotNull DoubleDrop ability, double activationChance) {
        super(abilityHolder, ability);
        this.activationChance = Math.max(0, activationChance);
    }

    /**
     * The {@link Ability} that is being activated by this event
     *
     * @return The {@link Ability} that is being activated by this event
     */
    @Override
    public @NotNull DoubleDrop getAbility() {
        return (DoubleDrop) super.getAbility();
    }

    /**
     * Gets the activation chance of this ability activating with 100 representing 100%
     *
     * @return The positive, zero inclusive activation chance
     */
    public double getActivationChance() {
        return activationChance;
    }

    /**
     * Sets the activation chance of this ability with 100 representing 100%
     *
     * @param activationChance The new activation chance of this ability
     */
    public void setActivationChance(double activationChance) {
        this.activationChance = Math.max(0, activationChance);
    }
}
