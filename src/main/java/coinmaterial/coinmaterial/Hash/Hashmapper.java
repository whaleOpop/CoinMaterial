package coinmaterial.coinmaterial.Hash;

import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;


/**
 * Implements Hashmapper to work with player wallets in .json files
 * Usage:        load/save HashMap of user wallets
 * Requirements: com.google.gson
*/
public class Hashmapper {
	
    static Gson gson = new Gson();
    public static HashMap<String, Double> playerCoin = new HashMap<>();
	
    public static void LoadCoin() {
		// Loads wallet data playerCoin from .json file
        try {
            FileReader reader = new FileReader("plugins/Coin.json");
            playerCoin = gson.fromJson(reader, HashMap.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void SaveCoin() {
		// Saves wallet data playerCoin to .json file
        try {
            FileWriter file = new FileWriter("plugins/Coin.json");
			String json = gson.toJson(playerCoin);
            file.write(json);
            file.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
