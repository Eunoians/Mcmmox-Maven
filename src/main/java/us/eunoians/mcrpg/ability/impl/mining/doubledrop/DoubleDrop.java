package us.eunoians.mcrpg.ability.impl.mining.doubledrop;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import us.eunoians.mcrpg.McRPG;
import us.eunoians.mcrpg.ability.Ability;
import us.eunoians.mcrpg.ability.BaseAbility;
import us.eunoians.mcrpg.ability.ConfigurableAbility;
import us.eunoians.mcrpg.ability.DefaultAbility;
import us.eunoians.mcrpg.ability.PlayerAbility;
import us.eunoians.mcrpg.ability.ToggleableAbility;
import us.eunoians.mcrpg.ability.configurable.ConfigurableAbilityDisplayItem;
import us.eunoians.mcrpg.ability.configurable.ConfigurableEnableableAbility;
import us.eunoians.mcrpg.ability.creation.AbilityCreationData;
import us.eunoians.mcrpg.annotation.AbilityIdentifier;
import us.eunoians.mcrpg.api.AbilityHolder;
import us.eunoians.mcrpg.api.error.AbilityConfigurationNotFoundException;
import us.eunoians.mcrpg.api.event.ability.mining.DoubleDropActivateEvent;
import us.eunoians.mcrpg.util.configuration.FileManager;
import us.eunoians.mcrpg.util.parser.Parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This ability has a chance that scales with the mining level to double the drops of a blocks
 *
 * @author DiamondDagger590
 */
@AbilityIdentifier(id = "double_drop", abilityCreationData = DoubleDropCreationData.class)
public class DoubleDrop extends BaseAbility implements DefaultAbility, ToggleableAbility, ConfigurableEnableableAbility,
        ConfigurableAbilityDisplayItem, PlayerAbility {

    private final static String DOUBLE_DROP_CHANCE_EQUATION_KEY = "double-drop-chance-activation-equation";
    public final static Set<Material> BLOCKS_TO_DOUBLE = new HashSet<>();

    static {
        for (Material material : Material.values()) {
            if (material.toString().contains("_ORE")) {
                BLOCKS_TO_DOUBLE.add(material);
            }
        }
    }

    private boolean toggled;

    /**
     * This assumes that the required extension of {@link AbilityCreationData}. Implementations of this will need
     * to sanitize the input.
     *
     * @param abilityCreationData The {@link AbilityCreationData} that is used to create this {@link Ability}
     */
    public DoubleDrop(@NotNull AbilityCreationData abilityCreationData) {
        super(abilityCreationData);

        if (abilityCreationData instanceof DoubleDropCreationData) {
            this.toggled = ((DoubleDropCreationData) abilityCreationData).isToggled();
        }
    }

    /**
     * Gets the {@link NamespacedKey} that this {@link Ability} belongs to
     *
     * @return The {@link NamespacedKey} that this {@link Ability} belongs to
     */
    @Override
    public @NotNull NamespacedKey getSkill() {
        return McRPG.getNamespacedKey("mining");
    }

    /**
     * @param activator    The {@link AbilityHolder} that is activating this {@link Ability}
     * @param optionalData Any objects that should be passed in. It is up to the implementation of the
     *                     ability to sanitize this input but this is here as there is no way to allow a
     *                     generic activation method without providing access for all types of ability
     */
    @Override
    public void activate(AbilityHolder activator, Object... optionalData) {

        if (optionalData.length > 0 && optionalData[0] instanceof BlockDropItemEvent) {

            BlockDropItemEvent blockDropItemEvent = (BlockDropItemEvent) optionalData[0];

            List<Item> itemsToDrop = blockDropItemEvent.getItems();
            Block block = blockDropItemEvent.getBlock();

            Set<Item> itemStacksToDrop = new HashSet<>(itemsToDrop);

            DoubleDropActivateEvent doubleDropActivateEvent = new DoubleDropActivateEvent(getAbilityHolder(), this, itemStacksToDrop, block);
            Bukkit.getPluginManager().callEvent(doubleDropActivateEvent);

            if(!doubleDropActivateEvent.isCancelled()){

                List<Item> newItemsToDrop = new ArrayList<>();

                for(Item item : doubleDropActivateEvent.getItemsToDrop()){

                    ItemStack itemStack = item.getItemStack();
                    itemStack.setAmount(itemStack.getAmount() * doubleDropActivateEvent.getMultiplier());
                    item.setItemStack(itemStack);

                    newItemsToDrop.add(item);
                }

                blockDropItemEvent.getItems().clear();
                blockDropItemEvent.getItems().addAll(newItemsToDrop);
            }
        }
    }

    /**
     * Abstract method that can be used to create listeners for this specific ability.
     * Note: This should only return a {@link List} of {@link Listener} objects. These shouldn't be registered yet!
     * This will be done automatically.
     *
     * @return a list of listeners for this {@link Ability}
     */
    @Override
    protected List<Listener> createListeners() {
        return Collections.singletonList(new DoubleDropListener());
    }

    /**
     * Gets the {@link FileConfiguration} that is used to configure this {@link ConfigurableAbility}
     *
     * @return The {@link FileConfiguration} that is used to configure this {@link ConfigurableAbility}
     */
    @Override
    public @NotNull FileConfiguration getAbilityConfigurationFile() {
        return McRPG.getInstance().getFileManager().getFile(FileManager.Files.MINING_CONFIG);
    }

    /**
     * Gets the exact {@link ConfigurationSection} that is used to configure this {@link ConfigurableAbility}.
     *
     * @return The exact {@link ConfigurationSection} that is used to configure this {@link ConfigurableAbility}.
     * @throws AbilityConfigurationNotFoundException Whenever the {@link ConfigurationSection} pulled is null
     */
    @Override
    public @NotNull ConfigurationSection getAbilityConfigurationSection() throws AbilityConfigurationNotFoundException {

        ConfigurationSection configurationSection = getAbilityConfigurationFile().getConfigurationSection("double-drop-config");

        if (configurationSection == null) {
            throw new AbilityConfigurationNotFoundException("Configuration section known as: 'double-drop-config' is missing from the " + FileManager.Files.MINING_CONFIG.getFileName() + " file.", getAbilityID());
        }
        return configurationSection;
    }

    /**
     * Gets the {@link Parser} that represents the equation needed to activate this ability.
     * <p>
     * Provided that there is an invalid equation offered in the configuration file, the equation will
     * always result in 0.
     *
     * @return The {@link Parser} that represents the equation needed to activate this ability
     */
    @Override
    public @NotNull Parser getActivationEquation() {
        try {
            return new Parser(getAbilityConfigurationSection().getString(DOUBLE_DROP_CHANCE_EQUATION_KEY));
        } catch (AbilityConfigurationNotFoundException e) {
            e.printStackTrace();
            return new Parser("");
        }
    }

    /**
     * This method checks to see if the {@link ToggleableAbility} is currently toggled on
     *
     * @return {@code true} if the {@link ToggleableAbility} is currently toggled on
     */
    @Override
    public boolean isToggled() {
        return this.toggled;
    }

    /**
     * This method inverts the current toggled state of the ability and returns the result.
     * <p>
     * This is more of a lazy way of calling {@link #setToggled(boolean)} without also needing to call
     * {@link #isToggled()} to invert
     *
     * @return The stored result of the inverted version of {@link #isToggled()}
     */
    @Override
    public boolean toggle() {
        this.toggled = !this.toggled;
        return this.toggled;
    }

    /**
     * This method sets the toggled status of the ability
     *
     * @param toggled {@code true} if the ability should be toggled on
     */
    @Override
    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }
}
