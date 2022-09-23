package yusama1251718.man10agriculture;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import static yusama1251718.man10agriculture.Man10Agriculture.easylist;

public class Event implements Listener {
    public Event(Man10Agriculture plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void eASYGUIClick(InventoryClickEvent e){
        if (!e.getView().title().equals(Component.text("[MAgri]Easy Recipe")) || easylist == null || easylist.containsKey(e.getWhoClicked())) return;
        if (!e.getWhoClicked().hasPermission("magri.op")) {
            e.setCancelled(true);
            return;
        }
        if (e.getCurrentItem() == null) return;
    }
}
