package coinmaterial.coinmaterial;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.function.BiFunction;

import coinmaterial.coinmaterial.Hash.Hashmapper;
import coinmaterial.coinmaterial.Model.PlayerModel;


/**
 * Implements: EventListener for CoinMaterial plugin
 * Usage:        Pickup method
 * Requirements: bukkit
*/
public class EventListener implements Listener {
    @EventHandler
    public void Pickup(PlayerPickupItemEvent e) {
		// EventHandler method for player pickup on emeralds
        Material type = e.getItem().getItemStack().getType();
		
        switch (type) {
            case EMERALD: {
				Integer ammount = e.getItem().getItemStack().getAmount();
				
                if (ammount > 0) {
					// Send a message, play a sound
                    e.getPlayer().sendMessage(ChatColor.BOLD + "Вы получили " + ammount + ChatColor.GOLD + "ﷻ");
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
					
					// Cancel event, destroy item
                    e.setCancelled(true);
                    e.getItem().remove();
					
					// Save wallet to json
					Hashmapper coinsaveload = null;
					BiFunction<Double, Double, Double> bFuncSum = (oldValue, newValue) -> oldValue + newValue;
                    Hashmapper.playerCoin.put(e.getPlayer().getName(), Hashmapper.playerCoin.merge(e.getPlayer().getName(), Double.valueOf(e.getItem().getItemStack().getAmount()), bFuncSum));
                    coinsaveload.SaveCoin();
                }
            }
        }
    }
}


