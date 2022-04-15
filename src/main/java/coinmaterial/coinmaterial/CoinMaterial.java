package coinmaterial.coinmaterial;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

import javax.annotation.Nonnull;

import coinmaterial.coinmaterial.command.CoinMaterialCommand;
import coinmaterial.coinmaterial.command.MoneyCommand;
import coinmaterial.coinmaterial.command.PayCommand;
import coinmaterial.coinmaterial.command.WalletCommand;
import doublewhaleapi.dwapi.DWAPI;

/**
 * CoinMaterial Bukkit Plugin
 * 
 * @author WhaleOpop, BlackWarlow
 *
 */
public final class CoinMaterial extends JavaPlugin {
	/**
	 * Singleton implementation.
	 */
	private static CoinMaterial instance;

	/**
	 * Plugin logger.
	 */
	public Logger logger = Bukkit.getLogger();

	/**
	 * Public available DWAPI core and integration settings.
	 */
	public DWAPI core;
	public Boolean guildedInstalled = false;

	/**
	 * Enables plugin - finds DWAPI core, reloads config file, initializes all
	 * Commands, registers EventListener
	 */
	@Override
	public void onEnable() {
		// Singleton
		instance = this;

		logger.info("CoinMaterial Start");

		// Get DWAPI core
		Plugin loadCore = Bukkit.getPluginManager().getPlugin("DWAPI");
		if (loadCore == null) {
			logger.severe("CoinMaterial requires DWAPI core plugin to work, please install it!");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		core = (DWAPI) loadCore;
		logger.info("coinStorage was successfuly loaded from DWAPI core.");

		// Save config file
		saveDefaultConfig();

		// Seamless integrations with plugins
		if (getConfig().getString("settings.general.integrateDW").equalsIgnoreCase("enabled")) {
			guildedInstalled = core.getAddonEnabled("Guilded");

			// Notify plugin enabled status
			if (guildedInstalled) {
				logger.info("DoubleWhale Guilded plugin is found. DW Plugin Integration is active.");
			} else {
				logger.info("Check out our other DoubleWhale plugin 'Guilded' at https://github.com/whaleopop/Guilded");
				logger.info(
						"If you see this and have Guilded plugin and integrateDW enabled in config.yml, reffer to https://github.com/whaleOpop/CoinMaterial README.md under Any DoubleWhale plugin integration");
			}
		} else
			logger.info("integrateDW is disabled in config.yml, skipping integration.");

		// Register commands
		new CoinMaterialCommand();
		new MoneyCommand();
		new WalletCommand();
		new PayCommand();

		// Register EventListener
		Bukkit.getPluginManager().registerEvents(new EventListener(), this);
	}

	/**
	 * Singleton implementation.
	 * 
	 * @return CoinMaterial Plugin instance
	 */
	@Nonnull
	public static CoinMaterial getInstance() {
		// Simple Singleton implementation
		return instance;
	}

	/**
	 * Handles plugin shutdown:<br>
	 * Saves coinStorage data to .json file.
	 */
	@Override
	public void onDisable() {
	}
}
