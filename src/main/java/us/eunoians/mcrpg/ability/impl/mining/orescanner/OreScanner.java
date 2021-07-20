package us.eunoians.mcrpg.ability.impl.mining.orescanner;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import us.eunoians.mcrpg.McRPG;
import us.eunoians.mcrpg.ability.Ability;
import us.eunoians.mcrpg.ability.ActiveAbility;
import us.eunoians.mcrpg.ability.ConfigurableAbility;
import us.eunoians.mcrpg.ability.PlayerAbility;
import us.eunoians.mcrpg.ability.ReadyableAbility;
import us.eunoians.mcrpg.ability.configurable.ConfigurableBaseActiveAbility;
import us.eunoians.mcrpg.ability.creation.AbilityCreationData;
import us.eunoians.mcrpg.api.AbilityHolder;
import us.eunoians.mcrpg.api.error.AbilityConfigurationNotFoundException;
import us.eunoians.mcrpg.util.configuration.FileManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OreScanner extends ConfigurableBaseActiveAbility implements ReadyableAbility, ActiveAbility, PlayerAbility{

    private final static Set<Material> ACTIVATION_MATERIALS = new HashSet<>();

    static {
        for (Material material : Material.values()) {
            if (material.toString().contains("_PICKAXE")) {
                ACTIVATION_MATERIALS.add(material);
            }
        }
    }

    /**
     * This assumes that the required extension of {@link AbilityCreationData}. Implementations of this will need
     * to sanitize the input.
     *
     * @param abilityCreationData The {@link AbilityCreationData} that is used to create this {@link Ability}
     */
    public OreScanner(@NotNull AbilityCreationData abilityCreationData){
        super(abilityCreationData);

        if (abilityCreationData instanceof OreScannerCreationData oreScannerCreationData) {
            this.tier = oreScannerCreationData.getTier();
            this.toggled = oreScannerCreationData.isToggled();
            this.unlocked = oreScannerCreationData.isUnlocked();
        }
    }

    /**
     * Gets the {@link NamespacedKey} that this {@link Ability} belongs to
     *
     * @return The {@link NamespacedKey} that this {@link Ability} belongs to
     */
    @Override
    public @NotNull NamespacedKey getSkill(){
        return McRPG.getNamespacedKey("mining");
    }

    /**
     * @param activator    The {@link AbilityHolder} that is activating this {@link Ability}
     * @param optionalData Any objects that should be passed in. It is up to the implementation of the
     *                     ability to sanitize this input but this is here as there is no way to allow a
     *                     generic activation method without providing access for all types of ability
     */
    @Override
    public void activate(AbilityHolder activator, Object... optionalData){

    }

    /**
     * Abstract method that can be used to create listeners for this specific ability.
     * Note: This should only return a {@link List} of {@link Listener} objects. These shouldn't be registered yet!
     * This will be done automatically.
     *
     * @return a list of listeners for this {@link Ability}
     */
    @Override
    protected List<Listener> createListeners(){
        return Collections.singletonList(new OreScannerListener());
    }

    /**
     * Gets the {@link FileConfiguration} that is used to configure this {@link ConfigurableAbility}
     *
     * @return The {@link FileConfiguration} that is used to configure this {@link ConfigurableAbility}
     */
    @Override
    public @NotNull FileConfiguration getAbilityConfigurationFile(){
        return McRPG.getInstance().getFileManager().getFile(FileManager.Files.MINING_CONFIG);
    }

    /**
     * Gets the exact {@link ConfigurationSection} that is used to configure this {@link ConfigurableAbility}.
     *
     * @return The exact {@link ConfigurationSection} that is used to configure this {@link ConfigurableAbility}.
     * @throws AbilityConfigurationNotFoundException Whenever the {@link ConfigurationSection} pulled is null
     */
    @Override
    public @NotNull ConfigurationSection getAbilityConfigurationSection() throws AbilityConfigurationNotFoundException{
        ConfigurationSection configurationSection = getAbilityConfigurationFile().getConfigurationSection("ore-scanner-config");

        if (configurationSection == null) {
            throw new AbilityConfigurationNotFoundException("Configuration section known as: 'ore-scanner-config' is missing from the " + FileManager.Files.MINING_CONFIG.getFileName() + " file.", getAbilityID());
        }
        return configurationSection;
    }

    /**
     * Handles parsing an {@link Event} to see if this ability should enter "ready" status.
     * <p>
     *
     * @param event The {@link Event} that needs to be parsed
     * @return {@code true} if the {@link ReadyableAbility} should enter "ready" status from this method call
     */
    @Override
    public boolean handleReadyAttempt(Event event){

        if (!isReady() && event instanceof PlayerInteractEvent && ((PlayerInteractEvent) event).getItem() != null &&
                getActivatableMaterials().contains(((PlayerInteractEvent) event).getItem().getType())) {
            return true;
        }

        return false;
    }

    /**
     * Gets a {@link Set} of all {@link Material}s that can activate this {@link ReadyableAbility}
     *
     * @return A {@link Set} of all {@link Material}s that can activate this {@link ReadyableAbility}
     */
    @Override
    public Set<Material> getActivatableMaterials(){
        return ACTIVATION_MATERIALS;
    }

    /**
     * Checks to see if this {@link ReadyableAbility} can be set to a ready status by interacting with a block.
     * <p>
     * If this returns {@code false}, then {@link #isValidReadyableBlock(Block)} will not be called.
     *
     * @return {@code true} if this ability can be ready'd from interacting with a {@link Block}
     */
    @Override
    public boolean readyFromBlock(){
        return true;
    }

    /**
     * Checks to see if this {@link ReadyableAbility} can be set to a ready status by interacting with an entity.
     * <p>
     * If this returns {@code false}, then {@link #isValidReadyableEntity(Entity)}  will not be called.
     *
     * @return {@code true} if this ability can be ready'd from interacting with a {@link Entity}
     */
    @Override
    public boolean readyFromEntity(){
        return true;
    }

    /**
     * Checks to see if interacting with the provided {@link Block} can trigger a ready status
     * for this ability.
     * <p>
     * This method will not be called unless {@link #readyFromBlock()} returns {@code true}
     *
     * @param block The {@link Block} to check for ready status
     * @return {@code true} if the provided {@link Block} is valid for this ability to enter ready status
     */
    @Override
    public boolean isValidReadyableBlock(Block block){
        return ReadyableAbility.super.isValidReadyableBlock(block);
    }

    /**
     * Checks to see if interacting with the provided {@link Entity} can trigger a ready status
     * for this ability.
     * <p>
     * This method will not be called unless {@link #readyFromEntity()} returns {@code true}
     *
     * @param entity The {@link Entity} to be checked
     * @return {@code true} if the {@link Entity} is valid for triggering ready status
     */
    @Override
    public boolean isValidReadyableEntity(Entity entity){
        return ReadyableAbility.super.isValidReadyableEntity(entity);
    }
}
