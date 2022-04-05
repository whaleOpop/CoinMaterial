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
 * Usage:        Pickup Item method listener on Entity
 * Requirements: none
 */
public class EventListener implements Listener {
	@EventHandler
	public void Pickup(EntityPickupItemEvent e) {
		// EventHandler method for player pickup on emeralds

		if (e.getEntity() instanceof Player
				&& e.getItem().getItemStack().getType() == Material.getMaterial(readConfig("coin", "COIN_MATERIAL"))) {
			// Entity picking up item is Player and the item type is Emerald

			Integer amount = e.getItem().getItemStack().getAmount();
			if (amount > 0) {
				// Temporary save player
				Player player = (Player) e.getEntity();

				// Send a message, play a sound
				String msg = readConfig("msg", "messagePickup");
				msg = msg.replace("{amount}", amount.toString());
				msg = msg.replace("{coinSymbol}", ChatColor.GOLD + readConfig("coin", "coinSymbol") + ChatColor.RESET);
				player.sendMessage(ChatColor.BOLD + msg);
				player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

				// Cancel event, destroy item
				e.setCancelled(true);
				e.getItem().remove();

				// Save wallet to json
				BiFunction<Double, Double, Double> bFuncSum = (oldValue, newValue) -> oldValue + newValue;
				Hashmapper.playerCoin.put(player.getName(),
						Hashmapper.playerCoin.merge(player.getName(), Double.valueOf(amount), bFuncSum));
				Hashmapper.SaveCoin();
			}
		}
	}

	public String readConfig(String ns, String key) {
		// readConfig method - reads value from config for given namespace and key pair
		return CoinMaterial.getInstance().getConfig().getString(ns + "." + key);
	}
}
