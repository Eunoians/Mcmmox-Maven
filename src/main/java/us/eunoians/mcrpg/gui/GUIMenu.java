package us.eunoians.mcrpg.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * GUIMenu
 *
 * @author OxKitsune
 */
public abstract class GUIMenu {

    private Inventory inventory;

    /**
     * Called whenever the {@link GUIMenu} is closed.
     *
     * @param player the player that closed the gui menu
     */
    public void onClose (Player player) {};

    public Inventory getInventory() {
        return inventory;
    }
}