package us.eunoians.mcrpg.util.fake;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents some sort of block break event that is being called
 * as a 'permission' check, and requires McRPG to be aware that this event is fake
 * as to not count it for various trackings.
 *
 * @author DiamondDagger590
 */
public class FakeBlockBreakEvent extends BlockBreakEvent{

    public FakeBlockBreakEvent(@NotNull Block theBlock, @NotNull Player player){
        super(theBlock, player);
    }
}
