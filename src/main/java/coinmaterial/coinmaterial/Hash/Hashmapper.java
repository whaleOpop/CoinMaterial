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

    public static boolean playerExists(String name) {
        // playerExists method - return true if player with given name is found in
        // playerCoin HashMap
        return playerCoin.get(name) != null;
    }

    public static Double getPlayerCoin(String name) {
        // getPlayerCoin method - handles getting wallet value of Player, if Player with
        // given name doesn`t have a wallet - create if with 0 value
        if (playerExists(name)) {
            return playerCoin.get(name);
        } else {
            playerCoin.put(name, 0.0);
            return 0.0;
        }
    }

    @SuppressWarnings("unchecked") // Could not do anything to .fromJson to fix Infer Generic Type...
    public static void LoadCoin() {
        // Loads wallet data playerCoin from .json file
        try {
            FileReader reader = new FileReader("plugins/Coin.json");
            playerCoin = gson.fromJson(reader, HashMap.class);
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
            file.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
