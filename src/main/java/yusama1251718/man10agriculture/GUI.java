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
            if (i == 2){
                inv.setItem(i + 2,getItem(Material.WHITE_STAINED_GLASS_PANE,1,"",1));
                inv.setItem(i + 4,getItem(Material.QUARTZ,1,"",62));
                inv.setItem(i + 6,getItem(Material.WHITE_STAINED_GLASS_PANE,1,"",1));
            }else for (int j = 1;j < 6;j++) inv.setItem(i + j,getItem(Material.WHITE_STAINED_GLASS_PANE,1,"",1));
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
            ItemMeta meta = item.getItemMeta();
            if (meta.hasLore()) meta.lore().clear();
            item.setItemMeta(meta);
            inv.setItem(i,item);
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
        inv.setItem(5,target.icon);
        inv.setItem(0,getItem(fertilizermate, Integer.valueOf(target.fertilizer),"§4肥料",fertilizercmd));
        inv.setItem(8,getItem(Material.WATER_BUCKET, Integer.valueOf(target.water),"水",1));
        if (target.dochange){
            String section = ChangeTime(target.time / target.change.size());
            inv.setItem(9,getItem(Material.BLACK_STAINED_GLASS_PANE, 1,section + "ごとの必要数",1));
            inv.setItem(17,getItem(Material.BLACK_STAINED_GLASS_PANE, 1,section + "ごとの必要数",1));
        } else {
            inv.setItem(9,getItem(Material.BLACK_STAINED_GLASS_PANE, 1,"必要数",1));
            inv.setItem(17,getItem(Material.BLACK_STAINED_GLASS_PANE, 1,"必要数",1));
        }
        p.openInventory(inv);
    }

    public static void OpenMenu(Player p, ItemFrame item){      //キットメニュー
        p.closeInventory();
        boolean isrecipe = false;
        Data.Recipe recipe = null;
        PersistentDataContainer data = item.getItem().getItemMeta().getPersistentDataContainer()
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
            boolean finish = between > (float) recipe.time;
            for (int i = 0; i <= 36; i = i + 9) {
                inv.setItem(i + 7, getItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, "水", 1));
                inv.setItem(i + 8, getItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, "水", 1));
                inv.setItem(i + 1, getItem(Material.BROWN_STAINED_GLASS_PANE, 1, "肥料", 1));
                inv.setItem(i, getItem(Material.BROWN_STAINED_GLASS_PANE, 1, "肥料", 1));
                if (i == 9 || i == 18 || i == 27) {
                    inv.setItem(i + 2, getItem(Material.WHITE_STAINED_GLASS_PANE, 1, "", 1));
                    inv.setItem(i + 6, getItem(Material.WHITE_STAINED_GLASS_PANE, 1, "", 1));
                    if (finish){

                    }
                    else if (recipe.dochange){
                        int section = (int) Math.floor(between) / (recipe.time / recipe.change.size());
                        inv.setItem(i + 3, recipe.change.get(section));
                        inv.setItem(i + 4, recipe.change.get(section));
                        inv.setItem(i + 5, recipe.change.get(section));
                    } else {
                        inv.setItem(i + 3, recipe.material);
                        inv.setItem(i + 4, recipe.material);
                        inv.setItem(i + 5, recipe.material);
                    }
                } else for (int j = 1; j < 6; j++) inv.setItem(i + j, getItem(Material.WHITE_STAINED_GLASS_PANE, 1, "", 1));
            }
            if (finish) inv.setItem(40,getItem(Material.RED_STAINED_GLASS_PANE,1,"受け取り",1));
            else inv.setItem(40,getItem(Material.RED_STAINED_GLASS_PANE,1,"キャンセル",1));
        } else {
            for (int i = 0; i <= 36; i = i + 9){
                inv.setItem(i + 7,getItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE,1,"水",1));
                inv.setItem(i + 8,getItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE,1,"水",1));
                inv.setItem(i + 1,getItem(Material.BROWN_STAINED_GLASS_PANE,1,"肥料",1));
                inv.setItem(i,getItem(Material.BROWN_STAINED_GLASS_PANE,1,"肥料",1));
                if (i == 18){
                    inv.setItem(i + 2,getItem(Material.WHITE_STAINED_GLASS_PANE,1,"",1));
                    inv.setItem(i + 3,getItem(Material.WHITE_STAINED_GLASS_PANE,1,"",1));
                    inv.setItem(i + 5,getItem(Material.WHITE_STAINED_GLASS_PANE,1,"",1));
                    inv.setItem(i + 6,getItem(Material.WHITE_STAINED_GLASS_PANE,1,"",1));
                }else for (int j = 1;j < 6;j++) inv.setItem(i + j,getItem(Material.WHITE_STAINED_GLASS_PANE,1,"",1));
            }
            inv.setItem(40,getItem(Material.RED_STAINED_GLASS_PANE,1,"開始",1));
        }
        p.openInventory(inv);
    }
}
