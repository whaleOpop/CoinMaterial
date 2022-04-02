package coinmaterial.coinmaterial;

import coinmaterial.coinmaterial.Hash.Hashmapper;
import coinmaterial.coinmaterial.Model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;

public class EventListener implements Listener {
    @EventHandler
    public void onPickUpPlayer(PlayerBedLeaveEvent Bed) {
    }

    @EventHandler
    public void Pickup(PlayerPickupItemEvent e) {
        Material type = e.getItem().getItemStack().getType();
        Integer ammount = e.getItem().getItemStack().getAmount();
        Hashmapper coinsaveload = null;
        switch (type) {
            case EMERALD: {
                BiFunction<Double, Double, Double> bFunc = (oldValue, newValue) -> oldValue + newValue;
                if (ammount > 0) {
                    e.getPlayer().sendMessage(ChatColor.BOLD + "Вы получили " + ammount + ChatColor.GOLD + "ﷻ");
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    e.setCancelled(true);
                    e.getItem().remove();
                    Hashmapper.playerCoin.put(e.getPlayer().getName(), Hashmapper.playerCoin.merge(e.getPlayer().getName(), Double.valueOf(e.getItem().getItemStack().getAmount()), bFunc));

                    coinsaveload.SaveCoin();
                }
            }
        }
    }
}


