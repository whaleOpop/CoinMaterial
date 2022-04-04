package coinmaterial.coinmaterial;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

import coinmaterial.coinmaterial.Hash.Hashmapper;
import coinmaterial.coinmaterial.command.CoinMaterialCommand;
import coinmaterial.coinmaterial.command.MoneyCommand;
import coinmaterial.coinmaterial.command.PayCommand;
import coinmaterial.coinmaterial.command.WalletCommand;

/**
 * Implements CoinMaterial plugin Usage: add coinmaterial.coinmaterial package
 * Requirements: org.bukkit, com.google.gson
 */
public final class CoinMaterial extends JavaPlugin {
	Logger log = getLogger();
	private static CoinMaterial instance;

	@Override
	public void onEnable() {
		// Enables plugin - loads coins from .json, initializes all Commands, registers
		log.info("CoinMaterial Start");
		
		// Load wallet data
		Hashmapper.LoadCoin();
		
		// Singleton
		instance = this;

		// Register commands
		new CoinMaterialCommand();
		new MoneyCommand();
		new WalletCommand();
		new PayCommand();

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
		Hashmapper.SaveCoin();
	}

	/**
	 * Helpful links (bukkit events, rubukkit links)
	 * http://rubukkit.org/threads/spisok-bukkit-events.125435/
	 * http://www.rubukkit.org/threads/pomosch-novichkam-i-tem-kto-malo-pisal-plaginy-lifehacki.54085/#post-714498
	 * 
	 */
}
