package yusama1251718.man10agriculture;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static yusama1251718.man10agriculture.Config.getConfig;
import static yusama1251718.man10agriculture.Man10Agriculture.*;

public class Function {

    public static void ReloadConfig(){
        recipes.clear();
        system = false;
        allowworld.clear();
        itemlore.clear();
        magri.saveDefaultConfig();
        system = magri.getConfig().getBoolean("system");
        allowworld.addAll(magri.getConfig().getStringList("worlds"));
        itemname = Component.text(magri.getConfig().getString("itemname"));
        List<String> addlist = new ArrayList<>();
        addlist.addAll(magri.getConfig().getStringList("itemlore"));
        for (int i = 0; i < addlist.size(); i++) itemlore.add(Component.text(addlist.get(i)));
        itemmate = Material.getMaterial(magri.getConfig().getString("itemmate"));
        itemcmd = magri.getConfig().getInt("itemcmd");
        fertilizermate = Material.getMaterial(magri.getConfig().getString("fertilizermate"));
        fertilizercmd = magri.getConfig().getInt("fertilizercmd");
        Config.LoadFile();
        if (configfile.listFiles() != null){
            for (File file : configfile.listFiles()){
                if (getConfig(YamlConfiguration.loadConfiguration(file),file) != null) recipes.add(getConfig(YamlConfiguration.loadConfiguration(file),file));
            }
        }
    }

    public static ItemStack CreateFrtilizer(){
        ItemStack item = new ItemStack(fertilizermate);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(magri , "Man10Agriculture"), PersistentDataType.STRING,"fertilizer");
        meta.displayName(Component.text("§4肥料"));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("§7水耕栽培で使える肥料。作物の成長を促進させる。"));
        meta.lore(lore);
        meta.setCustomModelData(fertilizercmd);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack CreateItem(){             //栽培キット作成
        ItemStack item = new ItemStack(itemmate);
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

    public static String ChangeTime(Integer time){
        int min, hour, day = 0;
        if (time >= 1440){
            day = time / 1440;
            hour = (time % 1440) / 60;
            min = time % 60;
            return day + "日" + hour + "時間" + min + "分";
        } else if (time >= 60) {
            hour = time / 60;
            min = time % 60;
            return hour + "時間" + min + "分";
        } else return time + "分";
    }

    public static Boolean checknull(YamlConfiguration config){              //ロード用
        return (config.getString("name") != null && config.getString("time") != null && config.getItemStack("material") != null && config.getList("result.item") != null && config.getList("result.chance") != null && config.getItemStack("icon") != null && config.getString("water") != null && config.getString("fertilizer") != null && config.getString("dochange") != null);
    }

    public static Byte CountWater(Inventory inv){
        byte water = 0;
        for (int i = 0; i <= 36; i = i + 9){
            if (inv.getItem(i + 7).equals(new ItemStack(Material.WATER_BUCKET))) water++;
            if (inv.getItem(i + 8).equals(new ItemStack(Material.WATER_BUCKET))) water++;
        }
        return water;
    }

    public static Byte CountFertilizer(Inventory inv){
        byte fertilizer = 0;
        for (int i = 0; i <= 36; i = i + 9){
            if (inv.getItem(i).equals(Function.CreateFrtilizer())) fertilizer++;
            if (inv.getItem(i + 1).equals(Function.CreateFrtilizer())) fertilizer++;
        }
        return fertilizer;
    }
}
