package us.eunoians.mcrpg.ability.impl.mining.superbreaker;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import us.eunoians.mcrpg.McRPG;
import us.eunoians.mcrpg.ability.Ability;
import us.eunoians.mcrpg.player.McRPGPlayer;
import us.eunoians.mcrpg.util.configuration.FileManager;
import us.eunoians.mcrpg.util.fake.FakeBlockBreakEvent;

import java.util.Optional;

/**
 * This listener is used to handle breaking the blocks for players who are currently super breaking
 *
 * @author DiamondDagger590
 */
public class SuperBreakerListener implements Listener{

    private static final NamespacedKey SUPER_BREAKER_KEY = McRPG.getNamespacedKey("super_breaker");

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handleInteract(PlayerInteractEvent playerInteractEvent){

        Block block = playerInteractEvent.getClickedBlock();
        ItemStack itemInHand = playerInteractEvent.getItem();
        Optional<McRPGPlayer> mcRPGPlayerOptional = McRPG.getInstance().getPlayerContainer().getPlayer(playerInteractEvent.getPlayer());

        if(block != null && block.getType() != Material.AIR
               && playerInteractEvent.getAction() == Action.LEFT_CLICK_BLOCK
               && itemInHand != null && itemInHand.getType().name().endsWith("_PICKAXE")
               && mcRPGPlayerOptional.isPresent()){

            McRPGPlayer mcRPGPlayer = mcRPGPlayerOptional.get();

            Ability ability = mcRPGPlayer.getAbilityFromLoadout(SUPER_BREAKER_KEY);

            if(ability != null){

                SuperBreaker superBreaker = (SuperBreaker) ability;

                if(superBreaker.isSuperBreaking()){

                    FileConfiguration miningFile = McRPG.getInstance().getFileManager().getFile(FileManager.Files.MINING_CONFIG);

                    //TODO store these somehow
                    if(miningFile.getStringList("exp-awarded-per-block").contains(playerInteractEvent.getClickedBlock().getType().name())){

                        SuperBreakerFakeBlockBreakEvent superBreakerFakeBlockBreakEvent = new SuperBreakerFakeBlockBreakEvent(block, mcRPGPlayer.getEntity());

                        Bukkit.getPluginManager().callEvent(superBreakerFakeBlockBreakEvent);

                        if(!superBreakerFakeBlockBreakEvent.isCancelled()){
                            block.breakNaturally(itemInHand);
                        }
                    }
                }
            }
        }
    }

    public class SuperBreakerFakeBlockBreakEvent extends FakeBlockBreakEvent{

        public SuperBreakerFakeBlockBreakEvent(@NotNull Block theBlock, @NotNull Player player){
            super(theBlock, player);
        }
    }
}
