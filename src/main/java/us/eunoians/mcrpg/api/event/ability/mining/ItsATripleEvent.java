package us.eunoians.mcrpg.api.event.ability.mining;

import org.jetbrains.annotations.NotNull;
import us.eunoians.mcrpg.ability.Ability;
import us.eunoians.mcrpg.ability.impl.mining.itsatriple.ItsATriple;
import us.eunoians.mcrpg.api.AbilityHolder;
import us.eunoians.mcrpg.api.event.ability.AbilityActivateEvent;

/**
 * This event is called whenever {@link ItsATriple} activates.
 *
 * If this event remains uncancelled, then it will call {@link DoubleDropActivateEvent#setMultiplier(int)} and set it
 * to 3 IF the current multiplier is 2.
 *
 * @author DiamondDagger590
 */
public class ItsATripleEvent extends AbilityActivateEvent {

    /**
     * @param abilityHolder    The {@link AbilityHolder} that is activating the event
     * @param ability          The {@link Ability} being activated
     */
    public ItsATripleEvent(@NotNull AbilityHolder abilityHolder, @NotNull ItsATriple ability) {
        super(abilityHolder, ability, AbilityEventType.RECREATIONAL);
    }

    /**
     * The {@link Ability} that is being activated by this event
     *
     * @return The {@link Ability} that is being activated by this event
     */
    @Override
    public @NotNull ItsATriple getAbility() {
        return (ItsATriple) super.getAbility();
    }
}
