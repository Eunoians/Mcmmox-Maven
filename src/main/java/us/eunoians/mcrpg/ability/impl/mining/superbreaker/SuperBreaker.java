package us.eunoians.mcrpg.ability.impl.mining.superbreaker;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import us.eunoians.mcrpg.McRPG;
import us.eunoians.mcrpg.ability.Ability;
import us.eunoians.mcrpg.ability.ActiveAbility;
import us.eunoians.mcrpg.ability.PlayerAbility;
import us.eunoians.mcrpg.ability.ReadyableAbility;
import us.eunoians.mcrpg.ability.configurable.ConfigurableBaseActiveAbility;
import us.eunoians.mcrpg.ability.creation.AbilityCreationData;
import us.eunoians.mcrpg.annotation.AbilityIdentifier;
import us.eunoians.mcrpg.api.AbilityHolder;
import us.eunoians.mcrpg.api.error.AbilityConfigurationNotFoundException;
import us.eunoians.mcrpg.player.McRPGPlayer;
import us.eunoians.mcrpg.util.configuration.FileManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This ability is an active mining ability which instant breaks blocks for a short time span
 *
 * @author DiamondDagger590
 */
@AbilityIdentifier(id = "super_breaker", abilityCreationData = SuperBreakerCreationData.class)
public class SuperBreaker extends ConfigurableBaseActiveAbility implements ReadyableAbility, ActiveAbility, PlayerAbility{

    private final static Set<Material> ACTIVATION_MATERIALS = new HashSet<>();

    private long superBreakEndTime;
    private BukkitTask superBreakEndTask;

    static {
        for (Material material : Material.values()) {
            if (material.toString().contains("_PICKAXE")) {
                ACTIVATION_MATERIALS.add(material);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public SuperBreaker(@NotNull AbilityCreationData abilityCreationData){
        super(abilityCreationData);

        if (abilityCreationData instanceof SuperBreakerCreationData superBreakerCreationData) {
            this.tier = superBreakerCreationData.getTier();
            this.toggled = superBreakerCreationData.isToggled();
            this.unlocked = superBreakerCreationData.isUnlocked();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull NamespacedKey getSkill(){
        return McRPG.getNamespacedKey("mining");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activate(AbilityHolder activator, Object... optionalData){

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<Listener> createListeners(){
        return Collections.singletonList(new SuperBreakerListener());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull FileConfiguration getAbilityConfigurationFile() {
        return McRPG.getInstance().getFileManager().getFile(FileManager.Files.MINING_CONFIG);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ConfigurationSection getAbilityConfigurationSection() throws AbilityConfigurationNotFoundException {

        ConfigurationSection configurationSection = getAbilityConfigurationFile().getConfigurationSection("super-breaker-config");

        if (configurationSection == null) {
            throw new AbilityConfigurationNotFoundException("Configuration section known as: 'super-breaker-config' is missing from the " + FileManager.Files.MINING_CONFIG.getFileName() + " file.", getAbilityID());
        }
        return configurationSection;
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public Set<Material> getActivatableMaterials(){
        return ACTIVATION_MATERIALS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean readyFromBlock(){
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean readyFromEntity(){
        return true;
    }

    /**
     * Gets the {@link McRPGPlayer} that this {@link Ability} belongs to.
     *
     * @return The {@link McRPGPlayer} that this {@link Ability} belongs to
     */
    @Override
    public @NotNull McRPGPlayer getPlayer() {
        return getAbilityHolder();
    }

    /**
     * Gets the {@link AbilityHolder} that owns this {@link Ability}
     *
     * @return THe {@link AbilityHolder} that owns this {@link Ability}
     */
    @Override
    public @NotNull McRPGPlayer getAbilityHolder() {
        return (McRPGPlayer) super.getAbilityHolder();
    }

    /**
     * Checks to see if the {@link #getAbilityHolder()} is currently using this ability
     * @return {@code true} if the {@link #getAbilityHolder()} is currently using this ability
     */
    public boolean isSuperBreaking(){
        return superBreakEndTime != -1 && superBreakEndTime > System.currentTimeMillis();
    }

    /**
     * Ends this ability for the {@link #getAbilityHolder()} that has this ability
     * activated.
     */
    public void endSuperBreak(){

        if(isSuperBreaking() && superBreakEndTask != null){
            superBreakEndTask.cancel();
            superBreakEndTask = null;
            superBreakEndTime = -1;

            sendSuperBreakEndMessage();
        }
    }

    public void sendSuperBreakEndMessage(){

    }
}
