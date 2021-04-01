package us.eunoians.mcrpg.ability.impl.mining.richerores;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import us.eunoians.mcrpg.ability.Ability;
import us.eunoians.mcrpg.ability.impl.mining.doubledrop.DoubleDrop;
import us.eunoians.mcrpg.api.AbilityHolder;
import us.eunoians.mcrpg.api.Methods;
import us.eunoians.mcrpg.api.event.ability.mining.DoubleDropPreActivateEvent;
import us.eunoians.mcrpg.player.McRPGPlayer;

public class RicherOresListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void handleRicherOres(DoubleDropPreActivateEvent doubleDropPreActivateEvent){

        AbilityHolder abilityHolder = doubleDropPreActivateEvent.getAbilityHolder();
        NamespacedKey id = Ability.getId(DoubleDrop.class);
        RicherOres richerOres = (RicherOres) abilityHolder.getAbility(id);

        if(abilityHolder instanceof McRPGPlayer && ((McRPGPlayer) abilityHolder).getLoadout().contains(richerOres)){

            double chance = richerOres.getActivationChance();

            if(richerOres.isToggled() && Methods.calculateChance(chance)){
                richerOres.activate(abilityHolder, doubleDropPreActivateEvent);
            }
        }


    }
}
