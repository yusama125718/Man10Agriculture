package yusama1251718.man10agriculture;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import static java.lang.Integer.parseInt;
import static yusama1251718.man10agriculture.Config.CreateRecipe;
import static yusama1251718.man10agriculture.Function.CountFertilizer;
import static yusama1251718.man10agriculture.Function.CountWater;
import static yusama1251718.man10agriculture.GUI.*;
import static yusama1251718.man10agriculture.Man10Agriculture.*;

public class Event implements Listener {
    public Event(Man10Agriculture plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void EASYGUIClick(InventoryClickEvent e){
        if (!e.getView().title().equals(Component.text("[MAgri]Easy Recipe")) || easylist == null || easylist.containsKey(e.getWhoClicked())) return;
        if (!e.getWhoClicked().hasPermission("magri.op")) {
            e.setCancelled(true);
            return;
        }
        if (e.getCurrentItem() == null) return;
        e.setCancelled(true);
        if (e.getRawSlot() == 40){      //done
            if (!e.getCurrentItem().getType().equals(Material.RED_STAINED_GLASS_PANE)) return;
            if (e.getInventory().getItem(21) == null || e.getInventory().getItem(23) == null){
                e.getWhoClicked().sendMessage("§a§l[Man10Agriculture] §rアイテムが不足しています！");
                return;
            }
            byte water = CountWater(e.getInventory()), fertilizer = CountFertilizer(e.getInventory());
            Data.easyrecipe addrecipe = easylist.get(e.getWhoClicked());
            Data.Recipe r = new Data.Recipe(addrecipe.name, e.getInventory().getItem(23), addrecipe.time, water, fertilizer, e.getInventory().getItem(21), e.getInventory().getItem(23));
            CreateRecipe(r);
            recipes.add(r);
        }
        else if (e.getRawSlot() == 0 || e.getRawSlot() == 1 || e.getRawSlot() == 9 || e.getRawSlot() == 10 || e.getRawSlot() == 18 || e.getRawSlot() == 19 || e.getRawSlot() == 27 || e.getRawSlot() == 28 || e.getRawSlot() == 36 || e.getRawSlot() == 37){         //fertilizer
            if (e.getCurrentItem().getType().equals(Material.BROWN_STAINED_GLASS_PANE)) e.getInventory().setItem(e.getRawSlot(), Function.CreateFrtilizer());
            else if (e.getCurrentItem().getType().equals(fertilizermate)) e.getInventory().setItem(e.getRawSlot(), Function.getItem(Material.BROWN_STAINED_GLASS_PANE,1,"肥料",1));
        }
        else if (e.getRawSlot() == 7 || e.getRawSlot() == 8 || e.getRawSlot() == 16 || e.getRawSlot() == 17 || e.getRawSlot() == 25 || e.getRawSlot() == 26 || e.getRawSlot() == 34 || e.getRawSlot() == 35 || e.getRawSlot() == 43 || e.getRawSlot() == 44){         //water
            if (e.getCurrentItem().getType().equals(Material.LIGHT_BLUE_STAINED_GLASS_PANE)) e.getInventory().setItem(e.getRawSlot(), new ItemStack(Material.WATER_BUCKET));
            else if (e.getCurrentItem().getType().equals(Material.WATER_BUCKET)) e.getInventory().setItem(e.getRawSlot(), Function.getItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE,1,"水",1));
        }
    }

    @EventHandler
    public void EasyGUIClose(InventoryCloseEvent e){        //簡易追加画面close処理
        if (!e.getView().title().equals(Component.text("[MAgri]Easy Recipe"))) return;
        if (easylist == null || !easylist.containsKey(e.getPlayer())) return;
        easylist.remove(e.getPlayer());
    }

    @EventHandler
    public void RecipeGUIClick(InventoryClickEvent e) {     //レシピ確認用
        if (e.getInventory().getSize() != 54) return;
        String title = null;
        Component component = e.getView().title();
        if (component instanceof TextComponent text) title = text.content();
        if (title == null || title.length() != 19 || !title.startsWith("[MAgri]Recipe List")) return;
        if (e.getCurrentItem() == null) {
            e.setCancelled(true);
            return;
        }
        boolean isNumeric = title.substring(18).matches("-?\\d+");
        if (!isNumeric) return;
        int page = parseInt(title.substring(18));
        if (51 <= e.getRawSlot() && e.getRawSlot() <= 53 && e.getCurrentItem().getType().equals(Material.BLUE_STAINED_GLASS_PANE)){    //次のページへ
            if ((double) recipes.size() / 45 > page) OpenRecipe((Player) e.getWhoClicked(),page + 1);
            e.setCancelled(true);
            return;
        }
        if (45 <= e.getRawSlot() && e.getRawSlot() <= 47 && e.getCurrentItem().getType().equals(Material.RED_STAINED_GLASS_PANE)){     //前のページへ
            if (page != 1) OpenRecipe((Player) e.getWhoClicked(),page - 1);
            e.setCancelled(true);
            return;
        }
        if (45 <= e.getRawSlot() && e.getRawSlot() <= 53 || e.getRawSlot() + 45 * (page - 1) >= recipes.size()) {
            e.setCancelled(true);
            return;
        }
        RecipeExample((Player) e.getWhoClicked(), e.getRawSlot() + 45 * (page - 1));
        e.setCancelled(true);
    }

    @EventHandler
    public void ExampleGUIClick(InventoryClickEvent e) {     //レシピ詳細確認用
        if (e.getInventory().getSize() != 18) return;
        String title = null;
        Component component = e.getView().title();
        if (component instanceof TextComponent text) title = text.content();
        if (title == null || !title.startsWith("[MAgri Recipe]")) return;
        e.setCancelled(true);
    }

//    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)       //1.19用設置処理
//    public void ItemPlace(PlayerItemFrameChangeEvent e){
//
//    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)         //設置処理
    public void ItemPlace(HangingPlaceEvent e){
        if (e.getEntity() instanceof ItemFrame item){
            if (!item.getItem().hasItemMeta() || item.getItem().getItemMeta().getPersistentDataContainer().isEmpty() || !item.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(magri , "Man10Agriculture"), PersistentDataType.STRING)) return;
            if (!system) {
                e.getPlayer().sendMessage("§a§l[Man10Agriculture] §r現在システムがoffのため設置できません");
                e.setCancelled(true);
                return;
            }
            if (!e.getPlayer().hasPermission("magri.p")){
                e.getPlayer().sendMessage("§a§l[Man10Agriculture] §r権限がありません");
                e.setCancelled(true);
                return;
            }
            if (!allowworld.contains(e.getPlayer().getWorld().toString())){
                e.getPlayer().sendMessage("§a§l[Man10Agriculture] §rこのワールドでは設置できません");
                e.setCancelled(true);
                return;
            }
            item.getItem().getItemMeta().getPersistentDataContainer().set(new NamespacedKey(magri , "Man10Agriculture"), PersistentDataType.STRING, e.getPlayer().getUniqueId().toString());
            item.getItem().getItemMeta().getPersistentDataContainer().set(new NamespacedKey(magri , "MAgriLock"), PersistentDataType.BYTE, (byte) 0);
            e.getPlayer().sendMessage("§a§l[Man10Agriculture] §r設置しました");
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)         //キットメニューOpen処理
    public void ItemClick(PlayerInteractEntityEvent e){
        if (e.getRightClicked() instanceof ItemFrame item){
            if (!item.getItem().hasItemMeta() || item.getItem().getItemMeta().getPersistentDataContainer().isEmpty() || !item.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(magri , "Man10Agriculture"), PersistentDataType.STRING) || !item.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(magri , "MAgriLock"), PersistentDataType.BYTE) || e.getHand().equals(EquipmentSlot.OFF_HAND)) return;
            e.setCancelled(true);
            if (!system) {
                e.getPlayer().sendMessage("§a§l[Man10Agriculture] §r現在システムはoffです");
                return;
            }
            if (!e.getPlayer().hasPermission("magri.p")){
                e.getPlayer().sendMessage("§a§l[Man10Agriculture] §r権限がありません");
                return;
            }
            if (!allowworld.contains(e.getPlayer().getWorld().toString())){
                e.getPlayer().sendMessage("§a§l[Man10Agriculture] §rこのワールドでは使えません");
                return;
            }
            if (item.getItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(magri , "MAgriLock"), PersistentDataType.BYTE) == 1 && !item.getItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(magri , "Man10Agriculture"), PersistentDataType.STRING).equals(e.getPlayer().getUniqueId().toString()) && !e.getPlayer().hasPermission("magri.op")){
                e.getPlayer().sendMessage("§a§l[Man10Agriculture] §rロックされています");
                return;
            }
            if (activeitem.containsKey(e.getPlayer())) activeitem.remove(e.getPlayer());
            for (Player p : activeitem.keySet()){
                if (e.getRightClicked().getLocation().equals(activeitem.get(p).getLocation())){
                    e.getPlayer().sendMessage("§a§l[Man10Agriculture] §rそのアイテムは開かれています");
                    return;
                }
            }
            activeitem.put(e.getPlayer(), item);
            OpenMenu(e.getPlayer(), item);
        }
    }
}
