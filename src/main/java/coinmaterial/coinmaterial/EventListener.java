package coinmaterial.coinmaterial;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import java.util.function.BiFunction;

import coinmaterial.coinmaterial.Hash.Hashmapper;


/**
 * Implements: EventListener for CoinMaterial plugin
 * Usage:        Pickup method
 * Requirements: none
 */
public class EventListener implements Listener {
    @EventHandler
    public void Pickup(EntityPickupItemEvent e) {
        // EventHandler method for player pickup on emeralds

        if (e.getEntity() instanceof Player && e.getItem().getItemStack().getType() == Material.EMERALD) {
        	// Entity picking up item is Player and the item type is Emerald
        	
            Integer amount = e.getItem().getItemStack().getAmount();
            if (amount > 0) {
            	// Temporary save player
            	Player player = (Player) e.getEntity();
            	
                // Send a message, play a sound
                player.sendMessage(ChatColor.BOLD + "Вы получили " + amount + ChatColor.GOLD + "ﷻ");
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

                // Cancel event, destroy item
                e.setCancelled(true);
                e.getItem().remove();

                // Save wallet to json
                BiFunction<Double, Double, Double> bFuncSum = (oldValue, newValue) -> oldValue + newValue;
                Hashmapper.playerCoin.put(player.getName(), Hashmapper.playerCoin.merge(player.getName(), Double.valueOf(amount), bFuncSum));
                Hashmapper.SaveCoin();
            }
        }
    }
}


