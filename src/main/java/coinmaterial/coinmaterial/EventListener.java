package coinmaterial.coinmaterial;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import doublewhaleapi.dwapi.DataModels.CoinModel;
import doublewhaleapi.dwapi.Serializers.CoinSerializer;

/**
 * Main EventListener for CoinMaterial plugin
 * 
 * @author WhaleOpop, BlackWarlow
 * 
 */
public class EventListener implements Listener {
	/**
	 * EventHandler for Pickup<br>
	 * Monitors if player have picked up COIN_MATERIAL ItemMaterial stack (read from
	 * config.yml settings) if so, adds coins to his wallet and destroys event item.
	 * 
	 * @param e EntityPickupEvent event
	 */
	@EventHandler
	public void Pickup(EntityPickupItemEvent e) {
		if ((e.getEntity() instanceof Player) && (e.getItem().getItemStack().getType() == Material
				.getMaterial(getSettings("currency", "COIN_MATERIAL")))) {
			// Entity picking up item is Player and the item type is COIN_MATERIAL defined
			// in config.yml settings.currency

			Integer amount = e.getItem().getItemStack().getAmount();
			if (amount > 0) {
				// Temporary saves player
				Player player = (Player) e.getEntity();
				String playerName = player.getName();

				// Sends a message, plays a sound
				player.sendMessage(ChatColor.BOLD + getLocal("general", "messageGotCoins")
						.replace("{amount}", amount.toString()).replace("{currencySymbol}",
								ChatColor.GOLD + getSettings("currency", "currencySymbol") + ChatColor.RESET));
				player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

				// Cancels event, destroys item
				e.setCancelled(true);
				e.getItem().remove();

				// Adds to coinStorage, saves to .json file
				CoinSerializer coinStorage = CoinMaterial.getInstance().core.coinStorage;
				CoinModel wallet = coinStorage.getWalletByName(playerName, false);
				if (wallet == null)
					coinStorage.createEmptyWallet(playerName, false);
				wallet.setBalance(wallet.getBalance() + amount);
			}
		}
	}

	/**
	 * Reads config.yml file for strings under namespace
	 * 
	 * @param ns  Namespace to read under
	 * @param key String to read
	 * @return String from config.yml
	 */
	private String readConfig(String ns, String key) {
		return CoinMaterial.getInstance().getConfig().getString(ns + "." + key);
	}

	/**
	 * Reads config.yml file for strings under localization.namespace
	 * 
	 * @param commandNS Command to get l10n string to
	 * @param localize  String to read
	 * @return String localized command string from config.yml
	 */
	public String getLocal(String commandNS, String localize) {
		return readConfig("localization", commandNS + "." + localize);
	}

	/**
	 * Reads config.yml file for strings under settings.namespace
	 * 
	 * @param settingsType Settings type under namespace settings.
	 * @param settingName  String to read
	 * @return String setting from config.yml
	 */
	public String getSettings(String settingsType, String settingName) {
		return readConfig("settings", settingsType + "." + settingName);
	}
}
