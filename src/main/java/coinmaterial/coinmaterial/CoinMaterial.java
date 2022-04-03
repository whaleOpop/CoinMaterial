package coinmaterial.coinmaterial;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

import coinmaterial.coinmaterial.Hash.Hashmapper;
import coinmaterial.coinmaterial.Model.PlayerModel;
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

    public static PlayerModel plmodel = new PlayerModel();
	
    Logger log = getLogger();
    private static CoinMaterial instance;


    @Override
    public void onEnable() {
		// Enables plubin - loads coins from json, inits all Commands, registers PickupEvent
        Hashmapper.LoadCoin();
        log.info("CoinMaterial Start");
		
        instance=this;
		
        new CoinMaterialCommand();
        new MoneyCommand();
        new WalletCommand();
        new PayCommand();
		
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
    }

    public static CoinMaterial getInstance(){
		//Simple Singleton implementation
        return instance;
    }

    @Override
    public void onDisable() {
		//Disables plubin - saves coins to json file
        Hashmapper.SaveCoin();
    }

/** Helpful links (bukkit events, rubukkit links)

 * http://rubukkit.org/threads/spisok-bukkit-events.125435/
 * http://www.rubukkit.org/threads/pomosch-novichkam-i-tem-kto-malo-pisal-plaginy-lifehacki.54085/#post-714498

*/
}
