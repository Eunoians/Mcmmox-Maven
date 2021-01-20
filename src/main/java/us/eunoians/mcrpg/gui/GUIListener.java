package us.eunoians.mcrpg.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import us.eunoians.mcrpg.McRPG;

/**
 * GUIListener
 *
 * @author OxKitsune
 */
public class GUIListener implements Listener {

    /**
     * The gui handler
     */
    private final GUIHandler guiHandler;

    /**
     * Construct a new {@link GUIListener} for the specified {@link GUIHandler}.
     *
     * @param guiHandler the GUIHandler
     */
    public GUIListener(GUIHandler guiHandler) {
        this.guiHandler = guiHandler;

        Bukkit.getPluginManager().registerEvents(this, McRPG.getInstance());
    }


    @EventHandler (ignoreCancelled = true)
    public void onInventoryClickEvent (InventoryClickEvent e) {

        GUIMenu guiMenu = guiHandler.getGUI(e.getWhoClicked().getUniqueId()).orElse(null);
        if (guiMenu == null) {
            return;
        }

        // TODO:
        // Figure out what button the player clicked
        // and call the click method

    }

    @EventHandler
    public void onInventoryCloseEvent (InventoryCloseEvent e) {

        GUIMenu guiMenu = guiHandler.getGUI(e.getPlayer().getUniqueId()).orElse(null);
        if (guiMenu == null) {
            return;
        }

        if (e.getInventory().equals(guiMenu.getInventory())) {
            guiMenu.onClose((Player) e.getPlayer());
        }
    }
}