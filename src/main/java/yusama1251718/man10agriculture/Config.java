package yusama1251718.man10agriculture;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static yusama1251718.man10agriculture.Man10Agriculture.magri;
import static yusama1251718.man10agriculture.Man10Agriculture.configfile;

public class Config {
    private static final File folder = new File(magri.getDataFolder().getAbsolutePath() + File.separator + "recipes");

    public static void LoadFile(){
        if (magri.getDataFolder().listFiles() != null){
            for (File file : Objects.requireNonNull(magri.getDataFolder().listFiles())) {
                if (file.getName().equals("recipes")) {
                    configfile = file;
                    return;
                }
            }
        }
        if (folder.mkdir()) {
            Bukkit.broadcast("§a§l[Man10Agriculture] §rレシピフォルダを作成しました", "magri.op");
            configfile = folder;
        } else {
            Bukkit.broadcast("§a§l[Man10Agriculture] §rレシピフォルダの作成に失敗しました", "magri.op");
        }
    }

    public static Data.Recipe getConfig(YamlConfiguration config, File file){
        if (!Function.checknull(config)) {
            Bukkit.broadcast("§a§l[Man10Agriculture] §r" + file.getName() + "の読み込みに失敗しました","magri.op");
            return null;
        }
        ItemStack material = config.getItemStack("material");
        ItemStack icon = config.getItemStack("icon");
        if (config.getBoolean("dochange")){
            List<Data.Result> result = (List<Data.Result>) config.getList("result");
            List<ItemStack> change = (List<ItemStack>) config.getList("change");
            return new Data.Recipe(config.getString("name"), icon, config.getInt("time"), (byte) config.getInt("water"), (byte) config.getInt("fertilizer"), material, result, change);
        } else {
            ItemStack result = config.getItemStack("result");
            return new Data.Recipe(config.getString("name"), icon, config.getInt("time"), (byte) config.getInt("water"), (byte) config.getInt("fertilizer"), material, result);
        }
    }

    public static void CreateRecipe(Data.Recipe r){
        File folder = new File(configfile.getAbsolutePath() + File.separator + r.name + ".yml");
        YamlConfiguration yml = new YamlConfiguration();        //config作成
        yml.set("name", r.name);
        yml.set("icon", r.icon);
        yml.set("time", r.time);
        yml.set("water", r.water);
        yml.set("fertilizer", r.fertilizer);
        yml.set("material", r.material);
        yml.set("result", r.result);
        yml.set("dochange", r.dochange);
        yml.set("change", r.change);
        try {
            yml.save(folder);
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.broadcast(Component.text("§a§l[Man10Agriculture] §r" + r.name + "の保存に失敗しました"),"magri.op");
        }
    }
}
