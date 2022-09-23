package yusama1251718.man10agriculture;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Man10Agriculture extends JavaPlugin {

    public static JavaPlugin magri;
    public static List<Data.Recipe> recipes = new ArrayList<>();
    public static Boolean system = false;
    public static File configfile;
    public static Boolean system;
    public static List<Player> lockuser = new ArrayList<>();
    public static List<Player> unlockuser = new ArrayList<>();
    public static Component itemname;
    public static List<Component> itemlore = new ArrayList<>();
    public static int itemcmd;
    public static List<String> allowworld = new ArrayList<>();
    public static HashMap<Player, Data.easyrecipe> easylist = new ArrayList<>();

    @Override
    public void onEnable() {
        getCommand("magri").setExecutor(new Command());
        new Event(this);
        magri = this;
        Function.ReloadConfig();
    }
}
