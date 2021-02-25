package us.eunoians.mcrpg.ability;

import us.eunoians.mcrpg.api.AbilityHolder;
import us.eunoians.mcrpg.api.Methods;

/**
 * This interface represents an {@link Ability} that has a chance to be activated.
 * <p>
 * This chance will be calculated before {@link Ability#activate(AbilityHolder, Object...)} is
 * called, meaning a direct call to that method will result in the usual activation code without a
 * check for activation.
 * <p>
 * This interface serves to introduce a method to expose the activation chance and to streamline
 * activation chance checking.
 *
 * @author DiamondDagger590
 */
public interface ActivatableChanceAbility extends Ability {

    /**
     * Gets the chance for this {@link ActivatableChanceAbility} to activate whenever
     * an "activatable action" is performed.
     * <p>
     * ie) Breaking an ore with double drop
     *
     * @return A positive zero inclusive chance for this {@link ActivatableChanceAbility} to activate.
     */
    public double getActivationChance();

    /**
     * Checks the implemented {@link #getActivationChance()} using {@link Methods#calculateChance(double)}
     * to see if the ability should be activated.
     *
     * @return {@code true} if the ability should be activated as a result from this method call.
     */
    public default boolean tryActivationChance() {
        return Methods.calculateChance(getActivationChance());
    }
}
