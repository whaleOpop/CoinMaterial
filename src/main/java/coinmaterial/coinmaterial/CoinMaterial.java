package coinmaterial.coinmaterial;

import coinmaterial.coinmaterial.Hash.Hashmapper;
import coinmaterial.coinmaterial.Model.PlayerModel;
import coinmaterial.coinmaterial.command.CoinMaterialCommand;
import coinmaterial.coinmaterial.command.MoneyCommand;
import coinmaterial.coinmaterial.command.PayCommand;
import coinmaterial.coinmaterial.command.WalletCommand;
import org.bukkit.Bukkit;

import org.bukkit.event.EventHandler;

import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class CoinMaterial extends JavaPlugin{

    public static PlayerModel plmodel = new PlayerModel();

    Logger log = getLogger();
    private static CoinMaterial instance;





    @Override
    public void onEnable() {

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
        return instance;
    }

    @Override
    public void onDisable() {
        Hashmapper.SaveCoin();
    }

//http://rubukkit.org/threads/spisok-bukkit-events.125435/
    //http://www.rubukkit.org/threads/pomosch-novichkam-i-tem-kto-malo-pisal-plaginy-lifehacki.54085/#post-714498

}
