package yusama1251718.man10agriculture;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static yusama1251718.man10agriculture.Function.getItem;
import static yusama1251718.man10agriculture.Man10Agriculture.recipes;

public class GUI {
    public static void easyrecipeGUI(Player p){
        Inventory inv = Bukkit.createInventory(null,45, Component.text("[MAgri]Easy Recipe"));
        for (int i = 0;i < 5;i++){
            int j = i * 9;
            if (j == 0) j = 1;
            inv.setItem(j + 7,getItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE,1,"水",1));
            inv.setItem(j + 8,getItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE,1,"水",1));
            inv.setItem(j,getItem(Material.BROWN_STAINED_GLASS_PANE,1,"肥料",1));
            inv.setItem(j - 1,getItem(Material.BROWN_STAINED_GLASS_PANE,1,"肥料",1));
            if (i == 2){
                inv.setItem(j + 1,getItem(Material.BROWN_STAINED_GLASS_PANE,1,"",1));
                inv.setItem(j + 3,getItem(Material.QUARTZ,1,"",62));
                inv.setItem(j + 5,getItem(Material.BROWN_STAINED_GLASS_PANE,1,"",1));
            }else for (int k = 1;k < 6;k++) inv.setItem(j + k,getItem(Material.BROWN_STAINED_GLASS_PANE,1,"",1));
        }
        inv.setItem(40,getItem(Material.BLACK_STAINED_GLASS_PANE,1,"追加",1));
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
}
