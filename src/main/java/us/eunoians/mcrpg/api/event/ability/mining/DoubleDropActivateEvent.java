package us.eunoians.mcrpg.api.event.ability.mining;

import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import us.eunoians.mcrpg.ability.Ability;
import us.eunoians.mcrpg.ability.impl.mining.doubledrop.DoubleDrop;
import us.eunoians.mcrpg.api.AbilityHolder;
import us.eunoians.mcrpg.api.event.ability.AbilityActivateEvent;

import java.util.Set;
import java.util.UUID;

/**
 * This event is called whenever {@link DoubleDrop} is about to activate.
 * <p>
 * The items in {@link #getItemsToDrop()} have not been multiplied yet and are mapped to the
 * {@link UUID} of the {@link org.bukkit.entity.Item} that is being dropped.
 * <p>
 * Removal of the {@link UUID} key will also remove the item from being dropped at all in the final
 * result of the activation.
 *
 * @author DiamondDagger590
 */
public class DoubleDropActivateEvent extends AbilityActivateEvent {

    private final Set<Item> itemsToDrop;
    private final Block block;
    private int multiplier = 2;

    /**
     * @param abilityHolder The {@link AbilityHolder} that is activating the event
     * @param ability       The {@link Ability} being activated
     */
    public DoubleDropActivateEvent(@NotNull AbilityHolder abilityHolder, @NotNull DoubleDrop ability, @NotNull Set<Item> itemsToDrop, @NotNull Block block) {
        super(abilityHolder, ability, AbilityEventType.RECREATIONAL);
        this.itemsToDrop = itemsToDrop;
        this.block = block;
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
     * Gets the {@link Set} of {@link Item}s that will be dropped if the event is uncancelled.
     * <p>
     * This {@link Set} contains items with the unmodified amounts which will be applied post event using the
     * multiplier specified in {@link #getMultiplier()}.
     *
     * @return The {@link Set} of {@link ItemStack}s being doubled
     */
    @NotNull
    public Set<Item> getItemsToDrop() {
        return itemsToDrop;
    }

    /**
     * Gets the {@link Block} that is dropping the items
     *
     * @return The {@link Block} that is dropping the items
     */
    public Block getBlock() {
        return block;
    }

    /**
     * Gets the amount that the drops in {@link #getItemsToDrop()} will be multiplied by whenever this event ends
     *
     * @return A positive, zero inclusive multiplier that will be applied to the drops contained in {@link #getItemsToDrop()}
     * once this event ends.
     */
    public int getMultiplier() {
        return multiplier;
    }

    /**
     * Sets the amount that the drops in {@link #getItemsToDrop()} will be multiplied by whenever this event ends.
     *
     * @param multiplier A positive, zero inclusive multiplier that will be applied to the drops contains in {@link #getItemsToDrop()}
     *                   once this event ends.
     */
    public void setMultiplier(int multiplier) {
        this.multiplier = Math.max(0, multiplier);
    }
}
