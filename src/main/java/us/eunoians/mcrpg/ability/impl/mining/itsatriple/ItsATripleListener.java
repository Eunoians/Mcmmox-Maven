package us.eunoians.mcrpg.ability.impl.mining.itsatriple;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import us.eunoians.mcrpg.ability.Ability;
import us.eunoians.mcrpg.api.AbilityHolder;
import us.eunoians.mcrpg.api.Methods;
import us.eunoians.mcrpg.api.event.ability.mining.DoubleDropActivateEvent;
import us.eunoians.mcrpg.player.McRPGPlayer;

/**
 * This listener handles activation of {@link ItsATriple}
 *
 * @author DiamondDagger590
 */
public class ItsATripleListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void handleRicherOres(DoubleDropActivateEvent doubleDropActivateEvent){

        AbilityHolder abilityHolder = doubleDropActivateEvent.getAbilityHolder();
        NamespacedKey id = Ability.getId(ItsATriple.class);
        ItsATriple itsATriple = (ItsATriple) abilityHolder.getAbility(id);

        if(abilityHolder instanceof McRPGPlayer && ((McRPGPlayer) abilityHolder).getLoadout().contains(itsATriple)){

            double chance = itsATriple.getActivationChance();

            if(itsATriple.isToggled() && Methods.calculateChance(chance)){
                itsATriple.activate(abilityHolder, doubleDropActivateEvent);
            }
        }
    }
}
