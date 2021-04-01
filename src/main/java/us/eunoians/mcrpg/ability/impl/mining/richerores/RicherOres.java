package us.eunoians.mcrpg.ability.impl.mining.richerores;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import us.eunoians.mcrpg.McRPG;
import us.eunoians.mcrpg.ability.Ability;
import us.eunoians.mcrpg.ability.ActivatableChanceAbility;
import us.eunoians.mcrpg.ability.ConfigurableAbility;
import us.eunoians.mcrpg.ability.configurable.ConfigurableBaseAbility;
import us.eunoians.mcrpg.ability.creation.AbilityCreationData;
import us.eunoians.mcrpg.annotation.AbilityIdentifier;
import us.eunoians.mcrpg.api.AbilityHolder;
import us.eunoians.mcrpg.api.error.AbilityConfigurationNotFoundException;
import us.eunoians.mcrpg.api.event.ability.mining.DoubleDropPreActivateEvent;
import us.eunoians.mcrpg.api.event.ability.mining.RicherOresActivateEvent;
import us.eunoians.mcrpg.util.configuration.FileManager;

import java.util.Collections;
import java.util.List;

/**
 * This ability increases the odds that {@link us.eunoians.mcrpg.ability.impl.mining.doubledrop.DoubleDrop} can activate.
 *
 * @author DiamondDagger590
 */
@AbilityIdentifier(id = "richer_ores", abilityCreationData = RicherOresCreationData.class)
public class RicherOres extends ConfigurableBaseAbility implements ActivatableChanceAbility {

    private static final String ACTIVATION_CHANCE_KEY = "activation-chance";
    private static final String DOUBLE_DROP_BOOST_KEY = "activation-boost";

    /**
     * This assumes that the required extension of {@link AbilityCreationData}. Implementations of this will need
     * to sanitize the input.
     *
     * @param abilityCreationData The {@link AbilityCreationData} that is used to create this {@link Ability}
     */
    public RicherOres(@NotNull AbilityCreationData abilityCreationData) {
        super(abilityCreationData);

        if (abilityCreationData instanceof RicherOresCreationData) {

            RicherOresCreationData richerOresCreationData = (RicherOresCreationData) abilityCreationData;

            this.tier = richerOresCreationData.getTier();
            this.unlocked = richerOresCreationData.isUnlocked();
            this.toggled = richerOresCreationData.isToggled();
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

        if (optionalData.length > 0 && optionalData[0] instanceof DoubleDropPreActivateEvent) {
            DoubleDropPreActivateEvent doubleDropPreActivateEvent = (DoubleDropPreActivateEvent) optionalData[0];
            ConfigurationSection configurationSection;

            try {
                configurationSection = getSpecificTierSection(getTier());
            } catch (AbilityConfigurationNotFoundException e) {
                e.printStackTrace();
                return;
            }

            double activationBoost = configurationSection.getDouble(DOUBLE_DROP_BOOST_KEY, 5.0);

            RicherOresActivateEvent richerOresActivateEvent = new RicherOresActivateEvent(activator, this, activationBoost);
            Bukkit.getPluginManager().callEvent(richerOresActivateEvent);

            if(!richerOresActivateEvent.isCancelled()) {
                doubleDropPreActivateEvent.setActivationChance(doubleDropPreActivateEvent.getActivationChance() + activationBoost);
            }
        }
    }

    /**
     * Gets the chance for this {@link ActivatableChanceAbility} to activate whenever
     * an "activatable action" is performed.
     * <p>
     * ie) Breaking an ore with double drop
     *
     * @return A positive zero inclusive chance for this {@link ActivatableChanceAbility} to activate.
     */
    @Override
    public double getActivationChance() {
        ConfigurationSection configurationSection;

        try {
            configurationSection = getSpecificTierSection(getTier());
        } catch (AbilityConfigurationNotFoundException e) {
            e.printStackTrace();
            return 0;
        }

        double activationChance = configurationSection.getDouble(ACTIVATION_CHANCE_KEY);

        return Math.max(0d, activationChance);    }

    /**
     * Abstract method that can be used to create listeners for this specific ability.
     * Note: This should only return a {@link List} of {@link Listener} objects. These shouldn't be registered yet!
     * This will be done automatically.
     *
     * @return a list of listeners for this {@link Ability}
     */
    @Override
    protected List<Listener> createListeners() {
        return Collections.singletonList(new RicherOresListener());
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
        ConfigurationSection configurationSection = getAbilityConfigurationFile().getConfigurationSection("richer-ores-config");

        if (configurationSection == null) {
            throw new AbilityConfigurationNotFoundException("Configuration section known as: 'richer-ores-config' is missing from the " + FileManager.Files.SWORDS_CONFIG.getFileName() + " file.", getAbilityID());
        }
        return configurationSection;
    }
}
