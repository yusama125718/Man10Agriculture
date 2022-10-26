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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.ClickEvent.suggestCommand;
import static yusama1251718.man10agriculture.Config.CreateRecipe;
import static yusama1251718.man10agriculture.Function.*;
import static yusama1251718.man10agriculture.GUI.*;
import static yusama1251718.man10agriculture.Man10Agriculture.*;

public class Event implements Listener {
    public Event(Man10Agriculture plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void EASYGUIClick(InventoryClickEvent e){        //簡易追加画面click処理
        if (!e.getView().title().equals(Component.text("[MAgri]Easy Recipe")) || easylist == null || !easylist.containsKey(e.getWhoClicked())) return;
        if (!e.getWhoClicked().hasPermission("magri.op")) {
            e.setCancelled(true);
            return;
        }
        switch (e.getRawSlot()) {
            case 0, 1, 9, 10, 18, 19, 27, 28, 36, 37 -> {
                e.setCancelled(true);
                if (e.getCurrentItem().getType().equals(Material.BROWN_STAINED_GLASS_PANE)) e.getInventory().setItem(e.getRawSlot(), Function.CreateFrtilizer());
                else if (e.getCurrentItem().getType().equals(fertilizermate)) e.getInventory().setItem(e.getRawSlot(), Function.getItem(Material.BROWN_STAINED_GLASS_PANE,1,"肥料",1));
            }

            case 7, 8, 16, 17, 25, 26, 34, 35, 43, 44 -> {
                e.setCancelled(true);
                if (e.getCurrentItem().getType().equals(Material.LIGHT_BLUE_STAINED_GLASS_PANE)) e.getInventory().setItem(e.getRawSlot(), new ItemStack(Material.WATER_BUCKET));
                else if (e.getCurrentItem().getType().equals(Material.WATER_BUCKET)) e.getInventory().setItem(e.getRawSlot(), Function.getItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE,1,"水",1));
            }

            case 40 -> {
                e.setCancelled(true);
                if (!e.getCurrentItem().getType().equals(Material.RED_STAINED_GLASS_PANE)) return;
                if (e.getInventory().getItem(21) == null || e.getInventory().getItem(23) == null) {
                    e.getWhoClicked().sendMessage("§a§l[Man10Agriculture] §rアイテムが不足しています！");
                    return;
                }
                byte water = CountWater(e.getInventory()), fertilizer = CountFertilizer(e.getInventory());
                Data.easyrecipe addrecipe = easylist.get(e.getWhoClicked());
                List<Data.Result> result = new ArrayList<>();
                result.add(new Data.Result(e.getInventory().getItem(23), 1d));
                Data.Recipe r = new Data.Recipe(addrecipe.name, e.getInventory().getItem(23), addrecipe.time, water, fertilizer, e.getInventory().getItem(21), result);
                CreateRecipe(r);
                recipes.add(r);
                e.getWhoClicked().sendMessage("§a§l[Man10Agriculture] §r追加しました");
                e.getWhoClicked().closeInventory();
            }

            default -> {
                if (e.getCurrentItem() == null || !e.getCurrentItem().getType().equals(Material.WHITE_STAINED_GLASS_PANE) || !e.getCurrentItem().hasItemMeta() || e.getCurrentItem().getItemMeta().getCustomModelData() != 1) return;
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void EasyGUIClose(InventoryCloseEvent e){        //簡易追加画面close処理
        if (!e.getView().title().equals(Component.text("[MAgri]Easy Recipe"))) return;
        if (easylist == null || !easylist.containsKey(e.getPlayer())) return;
        easylist.remove(e.getPlayer());
    }

    @EventHandler
    public void ADVChangeGUIClick(InventoryClickEvent e) {        //Advance追加画面click処理
        if (!e.getView().title().equals(Component.text("[MAgri]Adv Change")) || advlist == null || !advlist.containsKey(e.getWhoClicked())) return;
        if (advlist.get((Player) e.getWhoClicked()).progression != 0) return;
        if (!e.getWhoClicked().hasPermission("magri.op")) {
            e.setCancelled(true);
            return;
        }
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getType().equals(Material.WHITE_STAINED_GLASS_PANE) || e.getCurrentItem().getType().equals(Material.BARRIER)){
            if (!e.getCurrentItem().hasItemMeta() || !e.getCurrentItem().getItemMeta().displayName().equals(Component.text(""))) return;
            e.setCancelled(true);
        }
        else if (e.getCurrentItem().getType().equals(Material.RED_STAINED_GLASS_PANE) && e.getRawSlot() == 40){
            e.setCancelled(true);
            Data.advrecipe target = advlist.get(e.getWhoClicked());
            for (int i = 0; i < target.section; i++){
                if (e.getInventory().getItem(i) == null){
                    if (target.change != null) target.change.clear();
                    e.getWhoClicked().sendMessage("§a§l[Man10Agriculture] §r空きスロットがあります");
                    return;
                }
                target.change.add(e.getInventory().getItem(i));
            }
            target.progression = 1;
            e.getWhoClicked().sendMessage("§a§l[Man10Agriculture] §r/magri advres に続けて結果の確率を入力してください");
            e.getWhoClicked().sendMessage("§a§l[Man10Agriculture] §r入力例：/magri advres 0.8 0.15 0.05");
            e.getWhoClicked().sendMessage(text("§a§l[ここをクリックで自動入力する]").clickEvent(suggestCommand("/magri advres ")));
            e.getWhoClicked().closeInventory();
        }
    }

    @EventHandler
    public void ADVResultGUIClick(InventoryClickEvent e) {        //Advance追加画面click処理
        if (e.getInventory().getSize() != 9 || e.getCurrentItem() == null || advlist == null || !advlist.containsKey(e.getWhoClicked())) return;
        if (advlist.get((Player) e.getWhoClicked()).progression != 2) return;
        String title = null;
        Component component = e.getView().title();
        if (component instanceof TextComponent text) title = text.content();
        if (title == null || title.length() != 18 || !title.startsWith("[MAgri]Adv Result")) return;
        if (e.getRawSlot() == 8){
            e.setCancelled(true);
            if (e.getInventory().getItem(4) == null){
                e.getWhoClicked().sendMessage("§a§l[Man10Agriculture] §rアイテムを入れてください");
                return;
            }
            boolean isNumeric = title.substring(17).matches("-?\\d+");
            if (!isNumeric) return;
            int section = parseInt(title.substring(17));
            if (section == advlist.get(e.getWhoClicked()).chance.size() - 1){
                advlist.get(e.getWhoClicked()).result.add(new Data.Result(e.getInventory().getItem(4), advlist.get(e.getWhoClicked()).chance.get(section)));
                e.getWhoClicked().closeInventory();
                advlist.get(e.getWhoClicked()).progression = 3;
                AdvIconGUI((Player) e.getWhoClicked());
            } else {
                advlist.get(e.getWhoClicked()).result.add(new Data.Result(e.getInventory().getItem(4), advlist.get(e.getWhoClicked()).chance.get(section)));
                e.getWhoClicked().closeInventory();
                AdvResultGUI((Player) e.getWhoClicked(), section + 1);
            }
        } else if ((e.getCurrentItem().getType().equals(Material.WHITE_STAINED_GLASS_PANE) || e.getCurrentItem().getType().equals(Material.BLACK_STAINED_GLASS_PANE)) && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().displayName().equals(Component.text(""))) e.setCancelled(true);
    }

    @EventHandler
    public void ADVIconGUIClick(InventoryClickEvent e) {        //Advance追加画面click処理
        if (e.getInventory().getSize() != 45 || e.getCurrentItem() == null || advlist == null || !advlist.containsKey(e.getWhoClicked())) return;
        if (!e.getView().title().equals(Component.text("[MAgri]Adv Icon")) || advlist == null || !advlist.containsKey(e.getWhoClicked())) return;
        if (advlist.get((Player) e.getWhoClicked()).progression != 3) return;
        if (!e.getWhoClicked().hasPermission("magri.op")) {
            e.setCancelled(true);
            return;
        }
        switch (e.getRawSlot()) {
            case 0, 1, 9, 10, 18, 19, 27, 28, 36, 37 -> {
                e.setCancelled(true);
                if (e.getCurrentItem().getType().equals(Material.BROWN_STAINED_GLASS_PANE)) e.getInventory().setItem(e.getRawSlot(), Function.CreateFrtilizer());
                else if (e.getCurrentItem().getType().equals(fertilizermate)) e.getInventory().setItem(e.getRawSlot(), Function.getItem(Material.BROWN_STAINED_GLASS_PANE,1,"肥料",1));
            }

            case 7, 8, 16, 17, 25, 26, 34, 35, 43, 44 -> {
                e.setCancelled(true);
                if (e.getCurrentItem().getType().equals(Material.LIGHT_BLUE_STAINED_GLASS_PANE)) e.getInventory().setItem(e.getRawSlot(), new ItemStack(Material.WATER_BUCKET));
                else if (e.getCurrentItem().getType().equals(Material.WATER_BUCKET)) e.getInventory().setItem(e.getRawSlot(), Function.getItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE,1,"水",1));
            }

            case 40 -> {
                e.setCancelled(true);
                if (!e.getCurrentItem().getType().equals(Material.RED_STAINED_GLASS_PANE)) return;
                if (e.getInventory().getItem(21) == null || e.getInventory().getItem(23) == null) {
                    e.getWhoClicked().sendMessage("§a§l[Man10Agriculture] §rアイテムが不足しています！");
                    return;
                }
                Data.advrecipe t = advlist.get((Player) e.getWhoClicked());
                Data.Recipe r = new Data.Recipe(t.name, e.getInventory().getItem(23),t.time ,Function.CountWater(e.getInventory()), Function.CountFertilizer(e.getInventory()), e.getInventory().getItem(21),t.result, t.change);
                recipes.add(r);
                CreateRecipe(r);
                e.getWhoClicked().closeInventory();
                e.getWhoClicked().sendMessage("§a§l[Man10Agriculture] §r追加しました");
            }

            default -> {
                if (e.getCurrentItem() == null || (!e.getCurrentItem().getType().equals(Material.WHITE_STAINED_GLASS_PANE) && !e.getCurrentItem().getType().equals(Material.BLACK_STAINED_GLASS_PANE)) || !e.getCurrentItem().hasItemMeta() || e.getCurrentItem().getItemMeta().getCustomModelData() != 1) return;
                e.setCancelled(true);
            }
        }
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
    public void ItemPlace(PlayerInteractEntityEvent e){
        if (e.getRightClicked() instanceof ItemFrame i){
            ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
            if (!item.hasItemMeta() || item.getItemMeta().getPersistentDataContainer().isEmpty() || !item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(magri , "Man10Agriculture"), PersistentDataType.STRING)) return;
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
            if (!allowworld.contains(e.getPlayer().getWorld().getName())){
                e.getPlayer().sendMessage("§a§l[Man10Agriculture] §rこのワールドでは設置できません");
                e.setCancelled(true);
                return;
            }
            ItemMeta meta = e.getPlayer().getInventory().getItemInMainHand().getItemMeta();
            meta.getPersistentDataContainer().set(new NamespacedKey(magri , "Man10Agriculture"), PersistentDataType.STRING, e.getPlayer().getUniqueId().toString());
            meta.getPersistentDataContainer().set(new NamespacedKey(magri , "MAgriLock"), PersistentDataType.BYTE, (byte) 0);
            e.getPlayer().getInventory().getItemInMainHand().setItemMeta(meta);
            e.getPlayer().sendMessage("§a§l[Man10Agriculture] §r設置しました");
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)         //キットメニューOpen処理
    public void ItemClick(PlayerInteractEntityEvent e){
        if (e.getRightClicked() instanceof ItemFrame item){
            if (!item.getItem().hasItemMeta() || item.getItem().getItemMeta().getPersistentDataContainer().isEmpty() || !item.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(magri , "Man10Agriculture"), PersistentDataType.STRING) || !item.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(magri , "MAgriLock"), PersistentDataType.BYTE) || !e.getHand().equals(EquipmentSlot.HAND) || lockuser.contains(e.getPlayer()) || unlockuser.contains(e.getPlayer())) return;
            e.setCancelled(true);
            if (!system) {
                e.getPlayer().sendMessage("§a§l[Man10Agriculture] §r現在システムはoffです");
                return;
            }
            if (!e.getPlayer().hasPermission("magri.p")){
                e.getPlayer().sendMessage("§a§l[Man10Agriculture] §r権限がありません");
                return;
            }
            if (!allowworld.contains(e.getPlayer().getWorld().getName())){
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

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)         //キット破壊処理
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent e){
        if (!e.getDamager().getType().equals(EntityType.PLAYER)) return;
        if (e.getEntity() instanceof ItemFrame item){
            if (!item.getItem().hasItemMeta() || item.getItem().getItemMeta().getPersistentDataContainer().isEmpty() || !item.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(magri , "Man10Agriculture"), PersistentDataType.STRING)) return;
            if (!system) {
                e.getDamager().sendMessage("§a§l[Man10Agriculture] §r現在システムがoffのため破壊できません");
                e.setCancelled(true);
                return;
            }
            if (!e.getDamager().hasPermission("magri.p")){
                e.getDamager().sendMessage("§a§l[Man10Agriculture] §r権限がありません");
                e.setCancelled(true);
                return;
            }
            if (!item.getItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(magri , "Man10Agriculture"), PersistentDataType.STRING).equals(e.getDamager().getUniqueId().toString())){
                if (item.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(magri , "MAgriLock"), PersistentDataType.BYTE) && item.getItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(magri , "MAgriLock"), PersistentDataType.BYTE) == 1 && !e.getDamager().hasPermission("magri.op")){
                    e.getDamager().sendMessage("§a§l[Man10Agriculture] §rロックされています");
                    e.setCancelled(true);
                    return;
                }
            }
            ItemMeta meta = item.getItem().getItemMeta();
            if (meta.getPersistentDataContainer().has(new NamespacedKey(magri , "Man10Agriculture"), PersistentDataType.STRING)) meta.getPersistentDataContainer().set(new NamespacedKey(magri , "Man10Agriculture"), PersistentDataType.STRING, "kit");
            if (meta.getPersistentDataContainer().has(new NamespacedKey(magri , "MAgriLock"), PersistentDataType.BYTE)) meta.getPersistentDataContainer().set(new NamespacedKey(magri , "MAgriLock"), PersistentDataType.BYTE, (byte) 0);
            if (meta.getPersistentDataContainer().has(new NamespacedKey(magri , "MAgriDate"), PersistentDataType.STRING)) meta.getPersistentDataContainer().remove(new NamespacedKey(magri , "MAgriDate"));
            if (meta.getPersistentDataContainer().has(new NamespacedKey(magri , "MAgriRes"), PersistentDataType.INTEGER)) meta.getPersistentDataContainer().remove(new NamespacedKey(magri , "MAgriRes"));
            if (meta.getPersistentDataContainer().has(new NamespacedKey(magri , "MAgriRecipe"), PersistentDataType.STRING)) meta.getPersistentDataContainer().remove(new NamespacedKey(magri , "MAgriRecipe"));
            item.getItem().setItemMeta(meta);
            e.getDamager().sendMessage("§a§l[Man10Agriculture] §r破壊しました");
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)         //ロック処理
    public void ItemLock(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof ItemFrame item){
            if (!lockuser.contains(e.getPlayer()) || !item.getItem().hasItemMeta() || item.getItem().getItemMeta().getPersistentDataContainer().isEmpty() || !item.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(magri , "Man10Agriculture"), PersistentDataType.STRING) || !item.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(magri , "MAgriLock"), PersistentDataType.BYTE) || !e.getHand().equals(EquipmentSlot.HAND)) return;
            lockuser.remove(e.getPlayer());
            e.setCancelled(true);
            if (!system) {
                e.getPlayer().sendMessage("§a§l[Man10Agriculture] §r現在システムがoffのためロックできません");
                return;
            }
            if (!e.getPlayer().hasPermission("magri.p")) {
                e.getPlayer().sendMessage("§a§l[Man10Agriculture] §r権限がありません");
                return;
            }
            if (!item.getItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(magri, "Man10Agriculture"), PersistentDataType.STRING).equals(e.getPlayer().getUniqueId().toString())) {
                e.getPlayer().sendMessage("§a§l[Man10Agriculture] §rあなたはオーナーでないのでロックできません");
                return;
            }
            ItemStack setitem = item.getItem();
            ItemMeta meta = setitem.getItemMeta();
            meta.getPersistentDataContainer().set(new NamespacedKey(magri , "MAgriLock"), PersistentDataType.BYTE, (byte) 1);
            setitem.setItemMeta(meta);
            item.setItem(setitem);
            e.getPlayer().sendMessage("§a§l[Man10Agriculture] §rロックしました");
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)         //アンロック処理
    public void ItemUnLock(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof ItemFrame item) {
            if (!unlockuser.contains(e.getPlayer()) || !item.getItem().hasItemMeta() || item.getItem().getItemMeta().getPersistentDataContainer().isEmpty() || !item.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(magri , "Man10Agriculture"), PersistentDataType.STRING) || !item.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(magri , "MAgriLock"), PersistentDataType.BYTE) || !e.getHand().equals(EquipmentSlot.HAND)) return;
            unlockuser.remove(e.getPlayer());
            e.setCancelled(true);
            if (!system) {
                e.getPlayer().sendMessage("§a§l[Man10Agriculture] §r現在システムがoffのため解除できません");
                return;
            }
            if (!e.getPlayer().hasPermission("magri.p")) {
                e.getPlayer().sendMessage("§a§l[Man10Agriculture] §r権限がありません");
                return;
            }
            if (!item.getItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(magri, "Man10Agriculture"), PersistentDataType.STRING).equals(e.getPlayer().getUniqueId().toString())) {
                e.getPlayer().sendMessage("§a§l[Man10Agriculture] §rあなたはオーナーでないのでロック解除できません");
                return;
            }
            ItemStack setitem = item.getItem();
            ItemMeta meta = setitem.getItemMeta();
            meta.getPersistentDataContainer().set(new NamespacedKey(magri , "MAgriLock"), PersistentDataType.BYTE, (byte) 0);
            setitem.setItemMeta(meta);
            item.setItem(setitem);
            e.getPlayer().sendMessage("§a§l[Man10Agriculture] §r解除しました");
        }
    }

    @EventHandler
    public void KitGUIClick(InventoryClickEvent e) {     //キットメニュークリック用
        if (e.getInventory().getSize() != 45 || activeitem == null || !activeitem.containsKey(e.getWhoClicked())) return;
        if (!e.getView().title().equals(Component.text("[Man10Agriculture]"))) return;
        if (!e.getWhoClicked().hasPermission("magri.p")) {
            e.setCancelled(true);
            return;
        }
        if (e.getCurrentItem() == null) return;
        ItemStack cursor = e.getCursor();
        switch (e.getRawSlot()){
            case 0, 1, 9, 10, 18, 19, 27, 28, 36, 37:
                e.setCancelled(true);
                if (!e.getCurrentItem().getType().equals(Material.BROWN_STAINED_GLASS_PANE)) return;
                if (!e.getCursor().hasItemMeta() || e.getCursor().getItemMeta().getPersistentDataContainer().isEmpty() || !e.getCursor().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(magri , "Man10Agriculture"), PersistentDataType.STRING) || !e.getCursor().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(magri , "Man10Agriculture"), PersistentDataType.STRING).equals("fertilizer")) return;
                e.getInventory().setItem(e.getRawSlot(), Function.CreateFrtilizer());
                cursor.setAmount(e.getCursor().getAmount() - 1);
                e.getWhoClicked().setItemOnCursor(cursor);
                return;

            case 7, 8, 16, 17, 25, 26, 34, 35, 43, 44:
                e.setCancelled(true);
                if (!e.getCurrentItem().getType().equals(Material.LIGHT_BLUE_STAINED_GLASS_PANE)) return;
                if (!e.getCursor().getType().equals(Material.WATER_BUCKET)) return;
                e.getInventory().setItem(e.getRawSlot(), new ItemStack(Material.WATER_BUCKET));
                e.getWhoClicked().setItemOnCursor(new ItemStack(Material.BUCKET));
                return;

            case 40:
                e.setCancelled(true);
                if (!e.getCurrentItem().getType().equals(Material.RED_STAINED_GLASS_PANE) || !e.getCurrentItem().hasItemMeta() || e.getCurrentItem().getItemMeta().getCustomModelData() != 1) return;
                if (e.getCurrentItem().getItemMeta().displayName().equals(Component.text("開始"))){
                    if (e.getInventory().getItem(22) == null) {
                        e.getWhoClicked().sendMessage("§a§l[Man10Agriculture] §rアイテムをセットしてください");
                        return;
                    }
                    Data.Recipe target = null;
                    for (Data.Recipe r : recipes){
                        if (!e.getInventory().getItem(22).equals(r.material)) continue;
                        target = r;
                        break;
                    }
                    if (target == null) {
                        e.getWhoClicked().sendMessage("§a§l[Man10Agriculture] §rアイテムが違います");
                        return;
                    }
                    if (CountWater(e.getInventory()) < target.water){
                        e.getWhoClicked().sendMessage("§a§l[Man10Agriculture] §r水をセットしてください");
                        return;
                    }
                    if (CountFertilizer(e.getInventory()) < target.fertilizer){
                        e.getWhoClicked().sendMessage("§a§l[Man10Agriculture] §r肥料をセットしてください");
                        return;
                    }
                    byte water = target.water, fertilizer = target.fertilizer;
                    for (int i = 0; i <= 36; i = i + 9){
                        if (water == 0) break;
                        if (e.getInventory().getItem(i + 7).equals(new ItemStack(Material.WATER_BUCKET))) {
                            e.getInventory().setItem(i + 7, getItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, "水", 1));
                            water--;
                        }
                        if (water == 0) break;
                        if (e.getInventory().getItem(i + 8).equals(new ItemStack(Material.WATER_BUCKET))) {
                            e.getInventory().setItem(i + 8, getItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, "水", 1));
                            water--;
                        }
                    }
                    for (int i = 0; i <= 36; i = i + 9) {
                        if (fertilizer == 0) break;
                        if (e.getInventory().getItem(i).equals(Function.CreateFrtilizer())) {
                            e.getInventory().setItem(i, getItem(Material.BROWN_STAINED_GLASS_PANE, 1, "肥料", 1));
                            fertilizer--;
                        }
                        if (fertilizer == 0) break;
                        if (e.getInventory().getItem(i + 1).equals(Function.CreateFrtilizer())) {
                            e.getInventory().setItem(i + 1, getItem(Material.BROWN_STAINED_GLASS_PANE, 1, "肥料", 1));
                            fertilizer--;
                        }
                    }
                    ItemStack item = activeitem.get(e.getWhoClicked()).getItem();
                    ItemMeta data = item.getItemMeta();
                    data.getPersistentDataContainer().set(new NamespacedKey(magri, "MAgriRecipe"), PersistentDataType.STRING, target.name);
                    data.getPersistentDataContainer().set(new NamespacedKey(magri, "MAgriDate"), PersistentDataType.STRING, LocalDateTime.now().toString());
                    item.setItemMeta(data);
                    activeitem.get(e.getWhoClicked()).setItem(item);
                    e.getInventory().close();
                    e.getWhoClicked().sendMessage("§a§l[Man10Agriculture] §r栽培を開始します");
                    return;
                }
                else if (e.getCurrentItem().getItemMeta().displayName().equals(Component.text("キャンセル"))){
                    if (e.getWhoClicked().getInventory().firstEmpty() == -1){
                        e.getWhoClicked().sendMessage("§a§l[Man10Agriculture] §rインベントリが満杯のためキャンセルできません");
                        return;
                    }
                    ItemStack item = activeitem.get(e.getWhoClicked()).getItem();
                    ItemMeta data = item.getItemMeta();
                    Data.Recipe target = null;
                    for (Data.Recipe r : recipes) if (r.name.equals(data.getPersistentDataContainer().get(new NamespacedKey(magri , "MAgriRecipe"), PersistentDataType.STRING))) target = r;
                    if (target == null) return;
                    e.getWhoClicked().getInventory().addItem(target.material);
                    data.getPersistentDataContainer().remove(new NamespacedKey(magri , "MAgriDate"));
                    data.getPersistentDataContainer().remove(new NamespacedKey(magri , "MAgriRecipe"));
                    item.setItemMeta(data);
                    activeitem.get(e.getWhoClicked()).setItem(item);
                    e.getInventory().close();
                    e.getWhoClicked().sendMessage("§a§l[Man10Agriculture] §rキャンセルしました");
                    return;
                }
                else if (e.getCurrentItem().getItemMeta().displayName().equals(Component.text("受け取り"))){
                    if (e.getWhoClicked().getInventory().firstEmpty() == -1){
                        e.getWhoClicked().sendMessage("§a§l[Man10Agriculture] §rインベントリが満杯のため受け取りできません");
                        return;
                    }
                    e.getWhoClicked().getInventory().addItem(e.getInventory().getItem(22));
                    ItemStack item = activeitem.get(e.getWhoClicked()).getItem();
                    ItemMeta data = item.getItemMeta();
                    data.getPersistentDataContainer().remove(new NamespacedKey(magri , "MAgriDate"));
                    data.getPersistentDataContainer().remove(new NamespacedKey(magri , "MAgriRecipe"));
                    data.getPersistentDataContainer().remove(new NamespacedKey(magri , "MAgriRes"));
                    item.setItemMeta(data);
                    activeitem.get(e.getWhoClicked()).setItem(item);
                    e.getInventory().close();
                    e.getWhoClicked().sendMessage("§a§l[Man10Agriculture] §r受け取りました");
                    return;
                }

            default:
                if (!e.getCurrentItem().getType().equals(Material.WHITE_STAINED_GLASS_PANE) || !e.getCurrentItem().hasItemMeta() || e.getCurrentItem().getItemMeta().getCustomModelData() != 1) return;
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void Closekit(InventoryCloseEvent e){        //kitClose処理
        if (e.getInventory().getSize() != 45 || activeitem == null || !activeitem.containsKey(e.getPlayer())) return;
        if (!e.getView().title().equals(Component.text("[Man10Agriculture]"))) return;
        ItemMeta meta = activeitem.get(e.getPlayer()).getItem().getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(magri,"MAgriWater"), PersistentDataType.BYTE, CountWater(e.getInventory()));
        meta.getPersistentDataContainer().set(new NamespacedKey(magri,"MAgriFertilizer"), PersistentDataType.BYTE, CountFertilizer(e.getInventory()));
        ItemStack item = activeitem.get(e.getPlayer()).getItem();
        item.setItemMeta(meta);
        activeitem.get(e.getPlayer()).setItem(item);
        activeitem.remove(e.getPlayer());
    }
}
