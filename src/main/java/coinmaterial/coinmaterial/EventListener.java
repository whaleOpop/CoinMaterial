package coinmaterial.coinmaterial;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import java.util.function.BiFunction;

import coinmaterial.coinmaterial.CoinSerializer.CoinSerializer;

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
				Player player = (Player)e.getEntity();

				// Send a message, play a sound
				String msg = ChatColor.BOLD + getLocal("general", "messageGotCoins");
				msg = msg.replace("{amount}", amount.toString());
				msg = msg.replace("{coinSymbol}", ChatColor.GOLD + getSettings("currency", "currencySymbol") + ChatColor.RESET);
				player.sendMessage(msg);
				player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

				// Cancel event, destroy item
				e.setCancelled(true);
				e.getItem().remove();

				// Add to wallet, save wallet
				BiFunction<Double, Double, Double> bFuncSum = (oldValue, newValue) -> oldValue + newValue;
				CoinSerializer.performCoinOperation(player.getName(), Double.valueOf(amount), bFuncSum);
				CoinSerializer.SaveCoin();
			}
		}
	}

	private String readConfig(String ns, String key) {
		// readConfig method - reads value from config for given namespace and key pair
		return CoinMaterial.getInstance().getConfig().getString(ns + "." + key);
	}
	
	public String getLocal(String commandNS, String localize) {
		// getLocal method - returns localized string from config.yml under 'localization' namespace
		return readConfig("localization", commandNS + "." + localize);
	}
	
	public String getSettings(String settingsType, String settingName) {
		// getSettings method - returns settings values from config.yml under 'settings' namespace
		return readConfig("settings", settingsType + "." + settingName);
	}
}
