package yusama1251718.man10agriculture;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import static yusama1251718.man10agriculture.Man10Agriculture.*;

public class Function {

    public static void ReloadConfig(){

    }

    public static ItemStack CreateItem(){             //発酵樽作成
        ItemStack item = new ItemStack(Material.STONE_HOE);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(magri , "Man10Agriculture"), PersistentDataType.STRING,"kit");
        meta.displayName(itemname);
        meta.lore(itemlore);
        meta.setCustomModelData(itemcmd);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getItem(Material mate,Integer amount,String name,Integer cmd){
        ItemStack item = new ItemStack(mate,amount);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name));
        meta.setCustomModelData(cmd);
        item.setItemMeta(meta);
        return item;
    }
}
