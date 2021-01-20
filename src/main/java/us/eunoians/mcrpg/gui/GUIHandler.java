package us.eunoians.mcrpg.gui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * GUIHandler
 *
 * @author OxKitsune
 */
public class GUIHandler {


    /**
     * A player's {@link UUID} mapped to the gui that they have open currently
     */
    private final Map<UUID, GUIMenu> registeredMenus;

    /**
     * Construct a new {@link GUIHandler} that handles all GUI related stuff.
     */
    public GUIHandler() {

        // Create listener
        new GUIListener(this);

        this.registeredMenus = new HashMap<>();
    }

    /**
     * Register a new {@link GUIMenu} for the specified {@link Player}.
     *
     * @param uuid the uuid of the player
     * @param menu the menu that's being opened by the player
     */
    public void registerGUI(@NotNull UUID uuid, @NotNull GUIMenu menu) {

        // Remove the old registered menu for the player
        getGUI(uuid).ifPresent(oldMenu -> registeredMenus.remove(uuid));

        registeredMenus.put(uuid, menu);
    }

    /**
     * Get the GUI that's currently open for the {@link Player}.
     *
     * @param uuid the uuid of the player.
     * @return an {@link Optional} that contains the {@link GUIMenu} instance of the GUI that the player has open.
     */
    public Optional<GUIMenu> getGUI(@NotNull UUID uuid) {
        if (registeredMenus.containsKey(uuid)) return Optional.empty();
        return Optional.of(registeredMenus.get(uuid));
    }

    /**
     * Get the GUI that's currently open for the {@link Player}.
     *
     * @param player the player
     * @return an {@link Optional} that contains the {@link GUIMenu} instance of the GUI that the player has open.
     */
    public Optional<GUIMenu> getGUI(@NotNull Player player) {
        return getGUI(player.getUniqueId());
    }

}