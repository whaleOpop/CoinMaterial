package coinmaterial.coinmaterial.Hash;

import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Hashmapper {


    static Gson gson = new Gson();
    public static HashMap<String, Double> playerCoin = new HashMap<>();
    public static void LoadCoin() {
        try {
            FileReader reader = new FileReader("plugins/Coin.json");
            playerCoin=gson.fromJson(reader,HashMap.class);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void SaveCoin() {

        String json = gson.toJson(playerCoin);


        try {
            FileWriter file = new FileWriter("plugins/Coin.json");
            file.write(json);
            file.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
