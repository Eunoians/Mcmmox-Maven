package us.eunoians.mcrpg.api.event.ability.mining;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import us.eunoians.mcrpg.ability.Ability;
import us.eunoians.mcrpg.ability.impl.mining.doubledrop.DoubleDrop;
import us.eunoians.mcrpg.api.AbilityHolder;
import us.eunoians.mcrpg.api.event.ability.AbilityActivateEvent;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This event is called whenever {@link DoubleDrop} is about to activate.
 *
 * The items in {@link #getItemsToDrop()} have been multiplied already and are mapped to the
 * {@link UUID} of the {@link org.bukkit.entity.Item} that is being dropped.
 *
 * Removal of the {@link UUID} key will also remove the item from being dropped at all in the final
 * result of the activation.
 *
 * @author DiamondDagger590
 */
public class DoubleDropActivateEvent extends AbilityActivateEvent {

    private final Map<UUID, ItemStack> itemsToDrop;
    private final Block block;

    /**
     * @param abilityHolder The {@link AbilityHolder} that is activating the event
     * @param ability       The {@link Ability} being activated
     */
    public DoubleDropActivateEvent(@NotNull AbilityHolder abilityHolder, @NotNull DoubleDrop ability, @NotNull Map<UUID, ItemStack> itemsToDrop, @NotNull Block block) {
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
     * Gets the {@link Map} of {@link ItemStack}s that will be dropped if the event is uncancelled
     *
     * @return The {@link List} of {@link ItemStack}s being doubled
     */
    @NotNull
    public Map<UUID, ItemStack> getItemsToDrop() {
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
}
