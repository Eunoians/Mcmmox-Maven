package us.eunoians.mcrpg.ability.impl.mining.doubledrop;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import us.eunoians.mcrpg.ability.Ability;
import us.eunoians.mcrpg.api.AbilityHolder;
import us.eunoians.mcrpg.player.McRPGPlayer;

/**
 * This listener handles activation of {@link DoubleDrop}
 *
 * @author DiamondDagger590
 */
public class DoubleDropListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void handleDoubleDrop(BlockDropItemEvent blockDropItemEvent){

        AbilityHolder abilityHolder = new AbilityHolder(blockDropItemEvent.getPlayer());
        NamespacedKey id = Ability.getId(DoubleDrop.class);
        DoubleDrop doubleDrop = (DoubleDrop) abilityHolder.getAbility(id);

        if(DoubleDrop.BLOCKS_TO_DOUBLE.contains(blockDropItemEvent.getBlock().getType()) &&
                abilityHolder instanceof McRPGPlayer && ((McRPGPlayer) abilityHolder).getLoadout().contains(doubleDrop)){

            if(doubleDrop.isToggled()){
                doubleDrop.activate(abilityHolder, blockDropItemEvent);
            }
        }
    }
}
