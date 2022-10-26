package yusama1251718.man10agriculture;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static yusama1251718.man10agriculture.Function.ChangeTime;
import static yusama1251718.man10agriculture.Function.getItem;
import static yusama1251718.man10agriculture.Man10Agriculture.*;

public class GUI {
    public static void easyrecipeGUI(Player p){         //簡易追加画面
        Inventory inv = Bukkit.createInventory(null,45, Component.text("[MAgri]Easy Recipe"));
        for (int i = 0; i <= 36; i = i + 9){
            inv.setItem(i + 7,getItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE,1,"水",1));
            inv.setItem(i + 8,getItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE,1,"水",1));
            inv.setItem(i + 1,getItem(Material.BROWN_STAINED_GLASS_PANE,1,"肥料",1));
            inv.setItem(i,getItem(Material.BROWN_STAINED_GLASS_PANE,1,"肥料",1));
            if (i == 18){
                inv.setItem(i + 2,getItem(Material.WHITE_STAINED_GLASS_PANE,1,"",1));
                inv.setItem(i + 4,getItem(Material.QUARTZ,1,"",62));
                inv.setItem(i + 6,getItem(Material.WHITE_STAINED_GLASS_PANE,1,"",1));
            }else for (int j = 2;j < 7;j++) inv.setItem(i + j,getItem(Material.WHITE_STAINED_GLASS_PANE,1,"",1));
        }
        inv.setItem(40,getItem(Material.RED_STAINED_GLASS_PANE,1,"追加",1));
        p.openInventory(inv);
    }

    public static void AdvChangeGUI(Player p, Integer section){
        Inventory inv = Bukkit.createInventory(null,45, Component.text("[MAgri]Adv Change"));
        for (int i = 0; i < 36; i++) if (i + 1 > section) inv.setItem(i, getItem(Material.BARRIER,1,"",0));
        for (int i = 36; i < 45; i++) {
            if (i == 40) inv.setItem(i, getItem(Material.RED_STAINED_GLASS_PANE,1,"決定",1));
            else inv.setItem(i, getItem(Material.WHITE_STAINED_GLASS_PANE,1,"",1));
        }
        p.openInventory(inv);
    }

    public static void AdvResultGUI(Player p,Integer section){
        Inventory inv = Bukkit.createInventory(null, 9, Component.text("[MAgri]Adv Result" + section));
        for (int i = 0; i < 9;i++){
            if (i == 0) inv.setItem(i,getItem(Material.BLACK_STAINED_GLASS_PANE, 1, "このアイテムの確率" + advlist.get(p).chance.get(section), 1));
            else if (i == 8) inv.setItem(i, getItem(Material.RED_STAINED_GLASS_PANE,1,"決定",1));
            else if (i != 4) inv.setItem(i, getItem(Material.WHITE_STAINED_GLASS_PANE,1,"",1));
        }
        p.openInventory(inv);
    }

    public static void AdvIconGUI(Player p){
        Inventory inv = Bukkit.createInventory(null,45, Component.text("[MAgri]Adv Icon"));
        for (int i = 0; i <= 36; i = i + 9){
            inv.setItem(i + 7,getItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE,1,"水",1));
            inv.setItem(i + 8,getItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE,1,"水",1));
            inv.setItem(i + 1,getItem(Material.BROWN_STAINED_GLASS_PANE,1,"肥料",1));
            inv.setItem(i,getItem(Material.BROWN_STAINED_GLASS_PANE,1,"肥料",1));
            if (i == 18){
                inv.setItem(i + 2,getItem(Material.WHITE_STAINED_GLASS_PANE,1,"",1));
                inv.setItem(i + 4,getItem(Material.BLACK_STAINED_GLASS_PANE,1,"右にアイコン、左に材料を入れる",1));
                inv.setItem(i + 6,getItem(Material.WHITE_STAINED_GLASS_PANE,1,"",1));
            }else for (int j = 2;j < 7;j++) inv.setItem(i + j,getItem(Material.WHITE_STAINED_GLASS_PANE,1,"",1));
        }
        inv.setItem(40,getItem(Material.RED_STAINED_GLASS_PANE,1,"追加",1));
        p.openInventory(inv);
    }

    public static void OpenRecipe(Player p, int page){      //レシピリスト
        Inventory inv = Bukkit.createInventory(null,54, Component.text("[MAgri]Recipe List" + page));
        for (int i = 51;i < 54;i++){
            inv.setItem(i,getItem(Material.BLUE_STAINED_GLASS_PANE,1,"次のページへ",1));
            inv.setItem(i - 3,getItem(Material.WHITE_STAINED_GLASS_PANE,1,"",1));
            inv.setItem(i - 6,getItem(Material.RED_STAINED_GLASS_PANE,1,"前のページへ",1));
        }
        for (int i = 0;i < recipes.size();i++){
            if (i == 45 || recipes.size() == i + 45 * (page - 1)){
                p.openInventory(inv);
                return;
            }
            Data.Recipe list;
            if (page == 1){
                list = recipes.get(i);
            }else{
                list = recipes.get(i + 45 * (page - 1));
            }
            ItemStack item = new ItemStack(list.icon);
            inv.setItem(i, item);
        }
        p.openInventory(inv);
    }

    public static void RecipeExample(Player p,Integer index){       //レシピ詳細
        Data.Recipe target = recipes.get(index);
        Inventory inv = Bukkit.createInventory(null,18,Component.text("[MAgri Recipe]" + target.name));
        for (int i = 0;i < 18;i++) inv.setItem(i,getItem(Material.WHITE_STAINED_GLASS_PANE,1,"",1));
        String time = ChangeTime(target.time);
        inv.setItem(12,getItem(Material.RED_STAINED_GLASS_PANE,1,"植えるもの",1));
        inv.setItem(14,getItem(Material.BLUE_STAINED_GLASS_PANE,1,"収穫品",1));
        inv.setItem(4,getItem(Material.QUARTZ,1,time,62));
        inv.setItem(3,target.material);
        inv.setItem(5,target.result.get(0).item);
        inv.setItem(0,getItem(fertilizermate, Integer.valueOf(target.fertilizer),"§4肥料",fertilizercmd));
        inv.setItem(8,getItem(Material.WATER_BUCKET, Integer.valueOf(target.water),"水",1));
        inv.setItem(9,getItem(Material.BLACK_STAINED_GLASS_PANE, 1,"必要数",1));
        inv.setItem(17,getItem(Material.BLACK_STAINED_GLASS_PANE, 1,"必要数",1));
        p.openInventory(inv);
    }

    public static void OpenMenu(Player p, ItemFrame item){      //キットメニュー
        p.closeInventory();
        boolean isrecipe = false;
        Data.Recipe recipe = null;
        PersistentDataContainer data = item.getItem().getItemMeta().getPersistentDataContainer();
        if (data.has(new NamespacedKey(magri , "MAgriRecipe"), PersistentDataType.STRING)){
            for (Data.Recipe r : recipes){
                if (r.name.equals(data.get(new NamespacedKey(magri , "MAgriRecipe"), PersistentDataType.STRING))){
                    isrecipe = true;
                    recipe = r;
                    break;
                }
            }
        }
        Inventory inv = Bukkit.createInventory(null,45, Component.text("[Man10Agriculture]"));
        if (isrecipe){
            LocalDateTime start = LocalDateTime.parse(data.get(new NamespacedKey(magri , "MAgriDate"), PersistentDataType.STRING));
            float between = ChronoUnit.MINUTES.between(start,LocalDateTime.now());
            boolean finish = between >= (float) recipe.time;
            byte water = 0, fertilizer = 0, c = 0;
            if (data.has(new NamespacedKey(magri,"MAgriWater"), PersistentDataType.BYTE)) water = data.get(new NamespacedKey(magri,"MAgriWater"), PersistentDataType.BYTE);
            if (data.has(new NamespacedKey(magri,"MAgriFertilizer"), PersistentDataType.BYTE)) fertilizer = data.get(new NamespacedKey(magri,"MAgriFertilizer"), PersistentDataType.BYTE);
            int r = 0;
            for (int i = 0; i <= 36; i = i + 9) {
                if (c < water) inv.setItem(i + 7, new ItemStack(Material.WATER_BUCKET));
                else inv.setItem(i + 7, getItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, "水", 1));
                if (c < fertilizer) inv.setItem(i, Function.CreateFrtilizer());
                else inv.setItem(i, getItem(Material.BROWN_STAINED_GLASS_PANE, 1, "肥料", 1));
                c++;
                if (c < water) inv.setItem(i + 8, new ItemStack(Material.WATER_BUCKET));
                else inv.setItem(i + 8, getItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, "水", 1));
                if (c < fertilizer)inv.setItem(i + 1, Function.CreateFrtilizer());
                else inv.setItem(i + 1, getItem(Material.BROWN_STAINED_GLASS_PANE, 1, "肥料", 1));
                c++;
                if (i == 9 || i == 18 || i == 27) {
                    inv.setItem(i + 2, getItem(Material.WHITE_STAINED_GLASS_PANE, 1, "", 1));
                    inv.setItem(i + 6, getItem(Material.WHITE_STAINED_GLASS_PANE, 1, "", 1));
                    if (finish){
                        if (data.has(new NamespacedKey(magri , "MAgriRes"), PersistentDataType.INTEGER)){
                            r = data.get(new NamespacedKey(magri , "MAgriRes"), PersistentDataType.INTEGER);
                            inv.setItem(i + 3, recipe.result.get(r).item);
                            inv.setItem(i + 4, recipe.result.get(r).item);
                            inv.setItem(i + 5, recipe.result.get(r).item);
                        } else {
                            double n = 0;
                            double number = Math.random();
                            for (int j = 0; j < recipe.result.size(); j++){
                                if (i == 9){
                                    if (number <= n + recipe.result.get(j).chance){
                                        inv.setItem(i + 3, recipe.result.get(j).item);
                                        inv.setItem(i + 4, recipe.result.get(j).item);
                                        inv.setItem(i + 5, recipe.result.get(j).item);
                                        r = j;
                                        ItemStack setitem = item.getItem();
                                        ItemMeta meta = setitem.getItemMeta();
                                        meta.getPersistentDataContainer().set(new NamespacedKey(magri , "MAgriRes"), PersistentDataType.INTEGER, j);
                                        setitem.setItemMeta(meta);
                                        item.setItem(setitem);
                                        break;
                                    }else n += recipe.result.get(j).chance;
                                } else {
                                    inv.setItem(i + 3, recipe.result.get(r).item);
                                    inv.setItem(i + 4, recipe.result.get(r).item);
                                    inv.setItem(i + 5, recipe.result.get(r).item);
                                }

                            }
                        }
                    }
                    else if (recipe.dochange){
                        int section = (int) Math.floor(between) / (recipe.time / recipe.change.size());
                        ItemStack setitem = new ItemStack(recipe.change.get(section));
                        ItemMeta setmeta = setitem.getItemMeta();
                        LocalDateTime finishtime = start.plusMinutes(recipe.time);
                        DateTimeFormatter f = DateTimeFormatter.ofPattern("MM/dd HH:mm");
                        setmeta.displayName(Component.text("完成予定時刻：" + finishtime.format(f)));
                        setitem.setItemMeta(setmeta);
                        inv.setItem(i + 3, setitem);
                        inv.setItem(i + 4, setitem);
                        inv.setItem(i + 5, setitem);
                    } else {
                        ItemStack setitem = new ItemStack(recipe.material);
                        ItemMeta setmeta = setitem.getItemMeta();
                        LocalDateTime finishtime = start.plusMinutes(recipe.time);
                        DateTimeFormatter f = DateTimeFormatter.ofPattern("MM/dd HH:mm");
                        setmeta.displayName(Component.text("完成予定時刻：" + finishtime.format(f)));
                        setitem.setItemMeta(setmeta);
                        inv.setItem(i + 3, setitem);
                        inv.setItem(i + 4, setitem);
                        inv.setItem(i + 5, setitem);
                    }
                } else for (int j = 2; j < 7; j++) inv.setItem(i + j, getItem(Material.WHITE_STAINED_GLASS_PANE, 1, "", 1));
            }
            if (finish) inv.setItem(40,getItem(Material.RED_STAINED_GLASS_PANE,1,"受け取り",1));
            else inv.setItem(40,getItem(Material.RED_STAINED_GLASS_PANE,1,"キャンセル",1));
        } else {
            byte water = 0, fertilizer = 0, c = 0;
            if (data.has(new NamespacedKey(magri,"MAgriWater"), PersistentDataType.BYTE)) water = data.get(new NamespacedKey(magri,"MAgriWater"), PersistentDataType.BYTE);
            if (data.has(new NamespacedKey(magri,"MAgriFertilizer"), PersistentDataType.BYTE)) fertilizer = data.get(new NamespacedKey(magri,"MAgriFertilizer"), PersistentDataType.BYTE);
            for (int i = 0; i <= 36; i = i + 9){
                if (c < water) inv.setItem(i + 7, new ItemStack(Material.WATER_BUCKET));
                else inv.setItem(i + 7, getItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, "水", 1));
                if (c < fertilizer) inv.setItem(i, Function.CreateFrtilizer());
                else inv.setItem(i, getItem(Material.BROWN_STAINED_GLASS_PANE, 1, "肥料", 1));
                c++;
                if (c < water) inv.setItem(i + 8, new ItemStack(Material.WATER_BUCKET));
                else inv.setItem(i + 8, getItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, "水", 1));
                if (c < fertilizer)inv.setItem(i + 1, Function.CreateFrtilizer());
                else inv.setItem(i + 1, getItem(Material.BROWN_STAINED_GLASS_PANE, 1, "肥料", 1));
                c++;
                if (i == 18){
                    inv.setItem(i + 2,getItem(Material.WHITE_STAINED_GLASS_PANE,1,"",1));
                    inv.setItem(i + 3,getItem(Material.WHITE_STAINED_GLASS_PANE,1,"",1));
                    inv.setItem(i + 5,getItem(Material.WHITE_STAINED_GLASS_PANE,1,"",1));
                    inv.setItem(i + 6,getItem(Material.WHITE_STAINED_GLASS_PANE,1,"",1));
                }else for (int j = 2;j < 7;j++) inv.setItem(i + j,getItem(Material.WHITE_STAINED_GLASS_PANE,1,"",1));
            }
            inv.setItem(40,getItem(Material.RED_STAINED_GLASS_PANE,1,"開始",1));
        }
        p.openInventory(inv);
    }
}
