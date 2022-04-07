package coinmaterial.coinmaterial;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

import coinmaterial.coinmaterial.CoinSerializer.CoinSerializer;
import coinmaterial.coinmaterial.command.CoinMaterialCommand;
import coinmaterial.coinmaterial.command.MoneyCommand;
import coinmaterial.coinmaterial.command.PayCommand;
import coinmaterial.coinmaterial.command.WalletCommand;

/**
 * Implements CoinMaterial plugin
 * Usage:        add coinmaterial.coinmaterial package
 * Requirements: org.bukkit, com.google.gson
 */
public final class CoinMaterial extends JavaPlugin {

	private static CoinMaterial instance;

	public static boolean guildedInstalled = false;
	public Logger log = getLogger();

	@Override
	public void onEnable() {
		// Enables plugin - loads coins from .json, initializes all Commands, registers
		log.info("CoinMaterial Start");
		
		// Save config file, load coins
		saveDefaultConfig();
		CoinSerializer.LoadCoin();
		
		// Seamless integrations with plugins
		if (getConfig().getString("settings.general.integrateDW").equalsIgnoreCase("enabled")) {
			for (final File fileEntry : new File("plugins/").listFiles()) {
				if (!fileEntry.isDirectory()) {
					String fileName = fileEntry.getName().toLowerCase();
					if (fileName.contains("guilded") && fileName.endsWith(".jar")) {
						guildedInstalled = true;
						break;
					}
				}
			}
			
			// Notify server admin in console
			if(guildedInstalled) {
				log.info("DoubleWhale Guilded plugin is found. DW Plugin Integration is active.");
			} else {
				log.info("Check out our other DoubleWhale plugin 'Guilded' at https://github.com/whaleopop/Guilded");
				log.info("If you see this and have Guilded plugin and integrateDW enabled in config.yml, reffer to https://github.com/whaleOpop/CoinMaterial README.md under Any DoubleWhale plugin integration");
			}
		} else {
			log.info("integrateDW is disabled in config.yml, skipping integrations.");
			guildedInstalled = false;
		}
		
		// Singleton
		instance = this;

		// Register commands
		new CoinMaterialCommand();
		new MoneyCommand();
		new WalletCommand();
		new PayCommand();
		//TODO: /top command
		
		// Register EventListener
		Bukkit.getPluginManager().registerEvents(new EventListener(), this);
	}

	public static CoinMaterial getInstance() {
		// Simple Singleton implementation
		return instance;
	}

	@Override
	public void onDisable() {
		// Disables plugin - saves coins to .json file
		CoinSerializer.SaveCoin();
	}
}
