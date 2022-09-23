package yusama1251718.man10agriculture;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Integer.parseInt;
import static yusama1251718.man10agriculture.GUI.easyrecipeGUI;
import static yusama1251718.man10agriculture.Man10Agriculture.*;

public class Command implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("magri.p")) return true;
        switch (args.length) {
            case 0:
                GUI.OpenRecipe((Player) sender,1);
                return true;

            case 1:
                switch (args[0]){
                    case "help":
                        sender.sendMessage("§a§l[Man10Agriculture] §7/magri §rレシピを表示します");
                        sender.sendMessage("§a§l[Man10Agriculture] §7/magri lock §r栽培キットをロックします");
                        sender.sendMessage("§a§l[Man10Agriculture] §7/magri unlock §r栽培キットのロックを解除します");
                        if (sender.hasPermission("magri.op")){
                            sender.sendMessage("§a§l[Man10Agriculture] §7/magri on/off §rシステムをon/offします");
                            sender.sendMessage("§a§l[Man10Agriculture] §7/magri item §r栽培キットを自分に付与します");
                            sender.sendMessage("§a§l[Man10Agriculture] §7/magri setitem §r今持っているアイテムの名前とLoreを栽培キットの名前とLoreにします");
                            sender.sendMessage("§a§l[Man10Agriculture] §7/magri addeasy [名前] [時間(分)] §rレシピを追加します(簡易版)");
                            sender.sendMessage("§a§l[Man10Agriculture] §7/magri delete [名前] §rレシピを削除します");
                            sender.sendMessage("§a§l[Man10Agriculture] §7/magri addworld [ワールド名] §r設置できるワールドを追加します");
                            sender.sendMessage("§a§l[Man10Agriculture] §7/magri deleteworld [ワールド名] §r設置できるワールドを削除します");
                        }
                        if (!system){
                            sender.sendMessage("§a§l[Man10Agriculture] §rシステムは現在OFFです");
                        }
                        return true;

                    case "lock":
                        if (!system){
                            sender.sendMessage("§a§l[Man10Agriculture] §rシステムは現在OFFです");
                            return true;
                        }
                        unlockuser.remove((Player) sender);
                        if (!lockuser.contains((Player) sender)) lockuser.add((Player) sender);
                        sender.sendMessage("§a§l[Man10Agriculture] §rロックしたい栽培キットを壊してください");
                        return true;

                    case "unlock":
                        if (!system){
                            sender.sendMessage("§a§l[Man10Agriculture] §rシステムは現在OFFです");
                            return true;
                        }
                        lockuser.remove((Player) sender);
                        if (!unlockuser.contains((Player) sender)) unlockuser.add((Player) sender);
                        sender.sendMessage("§a§l[Man10Agriculture] §r解除したい栽培キットを壊してください");
                        return true;

                    case "on":
                        if (!sender.hasPermission("magri.op")){
                            sender.sendMessage("§a§l[Man10Agriculture] §r/magri help でhelpを表示");
                            return true;
                        }
                        if (system){
                            sender.sendMessage("§a§l[Man10Agriculture] §rすでにONです");
                            return true;
                        }
                        system = true;
                        magri.getConfig().set("system",system);
                        magri.saveConfig();
                        sender.sendMessage("§a§l[Man10Agriculture] §rONにしました");
                        return true;

                    case "off":
                        if (!sender.hasPermission("magri.op")){
                            sender.sendMessage("§a§l[Man10Agriculture] §r/magri help でhelpを表示");
                            return true;
                        }
                        if (!system){
                            sender.sendMessage("§a§l[Man10Agriculture] §rすでにONです");
                            return true;
                        }
                        system = false;
                        magri.getConfig().set("system",system);
                        magri.saveConfig();
                        sender.sendMessage("§a§l[Man10Agriculture] §rOFFにしました");
                        return true;

                    case "item":
                        if (!sender.hasPermission("magri.op")){
                            sender.sendMessage("§a§l[Man10Agriculture] §r/magri help でhelpを表示");
                            return true;
                        }
                        if (!system){
                            sender.sendMessage("§a§l[Man10Agriculture] §rシステムは現在OFFです");
                            return true;
                        }
                        ((Player) sender).getInventory().addItem(Function.CreateItem());
                        sender.sendMessage("§a§l[Man10Agriculture] §r付与しました");
                        return true;

                    case "setitem":
                        if (!sender.hasPermission("magri.op")){
                            sender.sendMessage("§a§l[Man10Agriculture] §r/magri help でhelpを表示");
                            return true;
                        }
                        if (!system){
                            sender.sendMessage("§a§l[Man10Agriculture] §rシステムは現在OFFです");
                            return true;
                        }
                        itemlore.clear();
                        ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
                        if (!item.hasItemMeta()){
                            itemlore.add(Component.text("農業ができる水耕栽培キット"));
                            itemname = Component.text("農業キット");
                            sender.sendMessage("§a§l[Man10Agriculture] §rItemMetaが存在しないので初期値を設定しました。");
                            return true;
                        }
                        if (item.getItemMeta().hasDisplayName()) itemname = item.getItemMeta().displayName();
                        else itemname = Component.text("農業キット");
                        if (item.getItemMeta().hasLore()) itemlore = item.getItemMeta().lore();
                        else itemlore.add(Component.text("農業ができる水耕栽培キット"));
                        magri.getConfig().set("itemname",itemname.toString());
                        magri.getConfig().set("itemlore",itemlore.toString());
                        magri.saveConfig();
                        sender.sendMessage("§a§l[Man10Agriculture] §r設定しました。");
                        return true;

                    default:
                        sender.sendMessage("§a§l[Man10Agriculture] §r/magri help でhelpを表示");
                        return true;
                }

            case 2:
                if (!sender.hasPermission("magri.op")){
                    sender.sendMessage("§a§l[Man10Agriculture] §r/magri help でhelpを表示");
                    return true;
                }
                switch (args[0]) {
                    case "delete":
                        Data.Recipe target = null;
                        for (Data.Recipe data : recipes) {
                            if (Objects.equals(data.name, args[1])) {
                                target = data;
                                break;
                            }
                        }
                        if (target == null){
                            sender.sendMessage("§a§l[Man10Agriculture] §rその名前のレシピは存在しません");
                            return true;
                        }
                        for (File file : configfile.listFiles()){
                            if (!file.getName().equals(args[2] + ".yml")) continue;
                            if (file.delete()) {
                                recipes.remove(target);
                                sender.sendMessage("§a§l[Man10Agriculture] §r削除しました");
                            }else{
                                sender.sendMessage("§a§l[Man10Agriculture] §rファイルの削除に失敗しました");
                            }
                            return true;
                        }
                        sender.sendMessage("§a§l[Man10Agriculture] §rファイルが見つかりませんでした");
                        return true;

                    case "addworld":
                        List<String> worlds = new ArrayList<>();
                        for (World w : Bukkit.getWorlds()) worlds.add(w.getName());
                        if (!worlds.contains(args[1])){
                            sender.sendMessage("§a§l[Man10Agriculture] §r指定されたワールドが見つかりませんでした");
                            return true;
                        }
                        if (allowworld.contains(args[1])){
                            sender.sendMessage("§a§l[Man10Agriculture] §r指定されたワールドは既に追加されています");
                            return true;
                        }
                        allowworld.add(args[1]);
                        magri.getConfig().set("worlds",allowworld);
                        magri.saveConfig();
                        sender.sendMessage("§a§l[Man10Agriculture] §r追加しました");
                        return true;

                    case "deleteworld":
                        List<String> worlds1 = new ArrayList<>();
                        for (World w : Bukkit.getWorlds()) worlds1.add(w.getName());
                        if (!worlds1.contains(args[1])){
                            sender.sendMessage("§a§l[Man10Agriculture] §r指定されたワールドが見つかりませんでした");
                            return true;
                        }
                        if (!allowworld.contains(args[1])){
                            sender.sendMessage("§a§l[Man10Agriculture] §r指定されたワールドはリストに存在しません");
                            return true;
                        }
                        allowworld.remove(args[1]);
                        magri.getConfig().set("worlds",allowworld);
                        magri.saveConfig();
                        sender.sendMessage("§a§l[Man10Agriculture] §r削除しました");
                        return true;

                    case "itemcmd":
                        boolean isNumeric = args[1].matches("-?\\d+");
                        if (!isNumeric){
                            sender.sendMessage("§a§l[Man10Agriculture] §r数字が無効です");
                            return true;
                        }
                        itemcmd = parseInt(args[1]);
                        magri.getConfig().set("itemcmd",itemcmd);
                        magri.saveConfig();
                        sender.sendMessage("§a§l[Man10Agriculture] §r設定しました。");
                        return true;
                }

            case 3:
                if (!args[0].equals("addeasy") || !sender.hasPermission("magri.op")){
                    sender.sendMessage("§a§l[Man10Agriculture] §r/magri help でhelpを表示");
                    return true;
                }

                List<String> returnlist = new ArrayList<>();
                for (Data.Recipe p : recipes) returnlist.add(p.name);
                if (returnlist.contains(args[1])){
                    sender.sendMessage("§a§l[Man10Agriculture] §rその名前は既に使われています");
                    return true;
                }
                boolean isNumeric = args[2].matches("-?\\d+");
                if (!isNumeric){
                    sender.sendMessage("§a§l[Man10Agriculture] §r時間が無効です");
                    return true;
                }
                easylist.put((Player) sender, new Data.easyrecipe(args[1],parseInt(args[2])));
                easyrecipeGUI((Player) sender);
                return true;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
