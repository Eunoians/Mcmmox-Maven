package us.eunoians.mcrpg.ability.impl.mining.doubledrop;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import us.eunoians.mcrpg.ability.Ability;
import us.eunoians.mcrpg.ability.creation.AbilityCreationData;
import us.eunoians.mcrpg.ability.creation.ToggleableCreationData;
import us.eunoians.mcrpg.api.AbilityHolder;

/**
 * This class is used to create an instance of {@link DoubleDrop}
 */
public class DoubleDropCreationData extends AbilityCreationData implements ToggleableCreationData {

    private final boolean toggled;

    public DoubleDropCreationData(@NotNull AbilityHolder abilityHolder, @NotNull JsonObject jsonObject) {
        super(abilityHolder, jsonObject);
        this.toggled = jsonObject.has("toggled") && jsonObject.get("toggled").getAsBoolean();
    }

    /**
     * Gets if the {@link Ability} represented by the {@link AbilityCreationData}
     * is toggled
     *
     * @return {@code true} if the {@link Ability} represented by the {@link AbilityCreationData}
     * is toggled
     */
    @Override
    public boolean isToggled() {
        return toggled;
    }
}
