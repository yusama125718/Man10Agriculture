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
import java.util.*;

import static java.lang.Integer.parseInt;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.ClickEvent.suggestCommand;
import static yusama1251718.man10agriculture.GUI.easyrecipeGUI;
import static yusama1251718.man10agriculture.Man10Agriculture.*;

public class Command implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("magri.p")) return true;
        if (args.length != 0 && sender.hasPermission("magri,op") && advlist != null && advlist.containsKey((Player) sender) && advlist.get((Player) sender).progression == 1){
            if (args[0].equals("advres")){
                Data.advrecipe target = advlist.get((Player) sender);
                if (target.chance != null) target.chance.clear();
                double sum = 0;
                for (int i = 1; i < args.length; i++){
                    boolean isNumber = args[i].matches("^([1-9]\\d*|0)(\\.\\d+)?$");
                    if (!isNumber){
                        sender.sendMessage("§a§l[Man10Agriculture] §r確率が無効です");
                        return true;
                    }
                    double d = Double.parseDouble(args[i]);
                    if (d > 1 || 0 > d){
                        sender.sendMessage("§a§l[Man10Agriculture] §r値は0~1の範囲にしてください");
                        return true;
                    }
                    sum += d;
                    target.chance.add(d);
                }
                if (sum != 1) {
                    sender.sendMessage("§a§l[Man10Agriculture] §r値の合計が1になるようにしてください");
                    return true;
                }
                target.progression = 2;
                GUI.AdvResultGUI((Player) sender, 0);
                return true;
            }
        }
        switch (args.length) {
            case 0:
                GUI.OpenRecipe((Player) sender,1);
                return true;

            case 1:
                switch (args[0]) {
                    case "help" -> {
                        sender.sendMessage("§a§l[Man10Agriculture] §7/magri §rレシピを表示します");
                        sender.sendMessage("§a§l[Man10Agriculture] §7/magri lock §r栽培キットをロックします");
                        sender.sendMessage("§a§l[Man10Agriculture] §7/magri unlock §r栽培キットのロックを解除します");
                        if (sender.hasPermission("magri.op")) {
                            sender.sendMessage("§a§l[Man10Agriculture] §7/magri on/off §rシステムをon/offします");
                            sender.sendMessage("§a§l[Man10Agriculture] §7/magri item §r栽培キットを自分に付与します");
                            sender.sendMessage("§a§l[Man10Agriculture] §7/magri fertilizer §r肥料を自分に付与します");
                            sender.sendMessage("§a§l[Man10Agriculture] §7/magri setitem §r今持っているアイテムの名前とLoreを栽培キットの名前とLoreにします");
                            sender.sendMessage("§a§l[Man10Agriculture] §7/magri addeasy [名前] [時間(分)] §rレシピを追加します(簡易版)");
                            sender.sendMessage("§a§l[Man10Agriculture] §7/magri addadv [名前] [時間(分)] [セクション数] §rレシピを追加します(Advance版)");
                            sender.sendMessage("§a§l[Man10Agriculture] §7/magri delete [名前] §rレシピを削除します");
                            sender.sendMessage("§a§l[Man10Agriculture] §7/magri addworld [ワールド名] §r設置できるワールドを追加します");
                            sender.sendMessage("§a§l[Man10Agriculture] §7/magri deleteworld [ワールド名] §r設置できるワールドを削除します");
                        }
                        if (!system) {
                            sender.sendMessage("§a§l[Man10Agriculture] §rシステムは現在OFFです");
                        }
                        return true;
                    }
                    case "lock" -> {
                        if (!system) {
                            sender.sendMessage("§a§l[Man10Agriculture] §rシステムは現在OFFです");
                            return true;
                        }
                        unlockuser.remove((Player) sender);
                        if (!lockuser.contains((Player) sender)) lockuser.add((Player) sender);
                        sender.sendMessage("§a§l[Man10Agriculture] §rロックしたい栽培キットをクリックしてください");
                        return true;
                    }
                    case "unlock" -> {
                        if (!system) {
                            sender.sendMessage("§a§l[Man10Agriculture] §rシステムは現在OFFです");
                            return true;
                        }
                        lockuser.remove((Player) sender);
                        if (!unlockuser.contains((Player) sender)) unlockuser.add((Player) sender);
                        sender.sendMessage("§a§l[Man10Agriculture] §r解除したい栽培キットをクリックしてください");
                        return true;
                    }
                    case "on" -> {
                        if (!sender.hasPermission("magri.op")) {
                            sender.sendMessage("§a§l[Man10Agriculture] §r/magri help でhelpを表示");
                            return true;
                        }
                        if (system) {
                            sender.sendMessage("§a§l[Man10Agriculture] §rすでにONです");
                            return true;
                        }
                        system = true;
                        magri.getConfig().set("system", system);
                        magri.saveConfig();
                        sender.sendMessage("§a§l[Man10Agriculture] §rONにしました");
                        return true;
                    }
                    case "off" -> {
                        if (!sender.hasPermission("magri.op")) {
                            sender.sendMessage("§a§l[Man10Agriculture] §r/magri help でhelpを表示");
                            return true;
                        }
                        if (!system) {
                            sender.sendMessage("§a§l[Man10Agriculture] §rすでにONです");
                            return true;
                        }
                        system = false;
                        magri.getConfig().set("system", system);
                        magri.saveConfig();
                        sender.sendMessage("§a§l[Man10Agriculture] §rOFFにしました");
                        return true;
                    }
                    case "item" -> {
                        if (!sender.hasPermission("magri.op")) {
                            sender.sendMessage("§a§l[Man10Agriculture] §r/magri help でhelpを表示");
                            return true;
                        }
                        ((Player) sender).getInventory().addItem(Function.CreateItem());
                        sender.sendMessage("§a§l[Man10Agriculture] §r付与しました");
                        return true;
                    }
                    case "fertilizer" -> {
                        if (!sender.hasPermission("magri.op")) {
                            sender.sendMessage("§a§l[Man10Agriculture] §r/magri help でhelpを表示");
                            return true;
                        }
                        ((Player) sender).getInventory().addItem(Function.CreateFrtilizer());
                        sender.sendMessage("§a§l[Man10Agriculture] §r付与しました");
                        return true;
                    }
                    case "setitem" -> {
                        if (!sender.hasPermission("magri.op")) {
                            sender.sendMessage("§a§l[Man10Agriculture] §r/magri help でhelpを表示");
                            return true;
                        }
                        itemlore.clear();
                        ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
                        if (!item.hasItemMeta()) {
                            itemlore.add(text("農業ができる水耕栽培キット"));
                            itemname = text("農業キット");
                            sender.sendMessage("§a§l[Man10Agriculture] §rItemMetaが存在しないので初期値を設定しました。");
                            return true;
                        }
                        if (item.getItemMeta().hasDisplayName()) itemname = item.getItemMeta().displayName();
                        else itemname = text("農業キット");
                        if (item.getItemMeta().hasLore()) itemlore = item.getItemMeta().lore();
                        else itemlore.add(text("農業ができる水耕栽培キット"));
                        magri.getConfig().set("itemname", itemname.toString());
                        magri.getConfig().set("itemlore", itemlore.toString());
                        magri.saveConfig();
                        sender.sendMessage("§a§l[Man10Agriculture] §r設定しました。");
                        return true;
                    }
                    default -> {
                        sender.sendMessage("§a§l[Man10Agriculture] §r/magri help でhelpを表示");
                        return true;
                    }
                }

            case 2:
                if (!sender.hasPermission("magri.op")){
                    sender.sendMessage("§a§l[Man10Agriculture] §r/magri help でhelpを表示");
                    return true;
                }
                switch (args[0]) {
                    case "delete" -> {
                        Data.Recipe target = null;
                        for (Data.Recipe data : recipes) {
                            if (Objects.equals(data.name, args[1])) {
                                target = data;
                                break;
                            }
                        }
                        if (target == null) {
                            sender.sendMessage("§a§l[Man10Agriculture] §rその名前のレシピは存在しません");
                            return true;
                        }
                        for (File file : configfile.listFiles()) {
                            if (!file.getName().equals(args[1] + ".yml")) continue;
                            if (file.delete()) {
                                recipes.remove(target);
                                sender.sendMessage("§a§l[Man10Agriculture] §r削除しました");
                            } else {
                                sender.sendMessage("§a§l[Man10Agriculture] §rファイルの削除に失敗しました");
                            }
                            return true;
                        }
                        sender.sendMessage("§a§l[Man10Agriculture] §rファイルが見つかりませんでした");
                        return true;
                    }
                    case "addworld" -> {
                        List<String> worlds = new ArrayList<>();
                        for (World w : Bukkit.getWorlds()) worlds.add(w.getName());
                        if (!worlds.contains(args[1])) {
                            sender.sendMessage("§a§l[Man10Agriculture] §r指定されたワールドが見つかりませんでした");
                            return true;
                        }
                        if (allowworld.contains(args[1])) {
                            sender.sendMessage("§a§l[Man10Agriculture] §r指定されたワールドは既に追加されています");
                            return true;
                        }
                        allowworld.add(args[1]);
                        magri.getConfig().set("worlds", allowworld);
                        magri.saveConfig();
                        sender.sendMessage("§a§l[Man10Agriculture] §r追加しました");
                        return true;
                    }
                    case "deleteworld" -> {
                        List<String> worlds1 = new ArrayList<>();
                        for (World w : Bukkit.getWorlds()) worlds1.add(w.getName());
                        if (!worlds1.contains(args[1])) {
                            sender.sendMessage("§a§l[Man10Agriculture] §r指定されたワールドが見つかりませんでした");
                            return true;
                        }
                        if (!allowworld.contains(args[1])) {
                            sender.sendMessage("§a§l[Man10Agriculture] §r指定されたワールドはリストに存在しません");
                            return true;
                        }
                        allowworld.remove(args[1]);
                        magri.getConfig().set("worlds", allowworld);
                        magri.saveConfig();
                        sender.sendMessage("§a§l[Man10Agriculture] §r削除しました");
                        return true;
                    }
                    case "itemcmd" -> {
                        boolean isNumeric = args[1].matches("-?\\d+");
                        if (!isNumeric) {
                            sender.sendMessage("§a§l[Man10Agriculture] §r数字が無効です");
                            return true;
                        }
                        itemcmd = parseInt(args[1]);
                        magri.getConfig().set("itemcmd", itemcmd);
                        magri.saveConfig();
                        sender.sendMessage("§a§l[Man10Agriculture] §r設定しました。");
                        return true;
                    }
                }

            case 3:
                if (args[0].equals("addeasy") && sender.hasPermission("magri.op")){
                    if (easylist.containsKey((Player) sender)) easylist.remove((Player) sender);
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
                } else sender.sendMessage("§a§l[Man10Agriculture] §r/magri help でhelpを表示");
                return true;

            case 4:
                if (args[0].equals("addadv") && sender.hasPermission("magri.op")){
                    if (advlist.containsKey((Player) sender)) advlist.remove((Player) sender);
                    List<String> returnlist = new ArrayList<>();
                    for (Data.Recipe p : recipes) returnlist.add(p.name);
                    if (returnlist.contains(args[1])){
                        sender.sendMessage("§a§l[Man10Agriculture] §rその名前は既に使われています");
                        return true;
                    }
                    boolean isNumeric0 = args[2].matches("-?\\d+");
                    if (!isNumeric0){
                        sender.sendMessage("§a§l[Man10Agriculture] §r時間が無効です");
                        return true;
                    }
                    boolean isNumeric1 = args[3].matches("-?\\d+");
                    if (!isNumeric1){
                        sender.sendMessage("§a§l[Man10Agriculture] §rセクション数が無効です");
                        return true;
                    }
                    int section = parseInt(args[3]);
                    if (section > 35){
                        sender.sendMessage("§a§l[Man10Agriculture] §rセクション数は35が上限です");
                        return true;
                    }
                    advlist.put((Player) sender, new Data.advrecipe(args[1], parseInt(args[2]), section));
                    if (section == 0){
                        advlist.get((Player) sender).progression = 1;
                        sender.sendMessage("§a§l[Man10Agriculture] §r/magri advres に続けて結果の確率を入力してください");
                        sender.sendMessage("§a§l[Man10Agriculture] §r入力例：/magri advres 0.8 0.15 0.05");
                        sender.sendMessage(text("§a§l[ここをクリックで自動入力する]").clickEvent(suggestCommand("/magri advres ")));
                    } else GUI.AdvChangeGUI((Player) sender, section);

                } else sender.sendMessage("§a§l[Man10Agriculture] §r/magri help でhelpを表示");
                return true;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!sender.hasPermission("magri.p")) return null;
        if(command.getName().equalsIgnoreCase("magri")) {
            if (args.length == 1){
                if (args[0].length() == 0) {
                    if (sender.hasPermission("mferm.op")) return Arrays.asList("addadv", "addeasy", "addworld", "delete", "deleteworld", "fertilizer", "help", "item", "lock", "on", "off", "setitem", "unlock");
                    else return Arrays.asList("lock", "unlock");
                }
                else {
                    if (sender.hasPermission("mferm.op")) {
                        if ("addadv".startsWith(args[0]) && "addeasy".startsWith(args[0]) && "addworld".startsWith(args[0])) {
                            return Arrays.asList("addadv", "addeasy", "addworld");
                        }
                        else if ("addadv".startsWith(args[0])) {
                            return Collections.singletonList("addadv");
                        }
                        else if ("addeasy".startsWith(args[0])) {
                            return Collections.singletonList("addeasy");
                        }
                        else if ("addworld".startsWith(args[0])) {
                            return Collections.singletonList("addworld");
                        }
                        else if ("delete".startsWith(args[0]) && "deleteworld".startsWith(args[0])) {
                            return Arrays.asList("delete", "deleteworld");
                        }
                        else if ("deleteworld".startsWith(args[0])) {
                            return Collections.singletonList("deleteworld");
                        }
                        else if ("fertilizer".startsWith(args[0])) {
                            return Collections.singletonList("fertilizer");
                        }
                        else if ("item".startsWith(args[0])) {
                            return Collections.singletonList("item");
                        }
                        else if ("on".startsWith(args[0]) && "off".startsWith(args[0])) {
                            return Arrays.asList("on", "off");
                        }
                        else if ("on".startsWith(args[0])) {
                            return Collections.singletonList("on");
                        }
                        else if ("off".startsWith(args[0])) {
                            return Collections.singletonList("off");
                        }
                        else if ("setitem".startsWith(args[0])) {
                            return Collections.singletonList("setitem");
                        }
                    }
                    if ("help".startsWith(args[0])) {
                        return Collections.singletonList("help");
                    }
                    if ("lock".startsWith(args[0])) {
                        return Collections.singletonList("lock");
                    }
                    else if ("unlock".startsWith(args[0])) {
                        return Collections.singletonList("unlock");
                    }
                }
            }
            else if (args.length == 2 && sender.hasPermission("magri.op")) {
                switch (args[0]) {
                    case "addworld":
                    case "deleteworld":
                        ArrayList<String> w = new ArrayList<>();
                        for (World world : Bukkit.getWorlds()) w.add(world.getName());
                        return w;
                    case "addadv":
                    case "addeasy":
                        return Collections.singletonList("[レシピ名]");
                    case "delete":
                        ArrayList<String> namelist = new ArrayList<>();
                        for (Data.Recipe r : recipes) namelist.add(r.name);
                        return namelist;
                }
            }
            else if (args.length == 3 && sender.hasPermission("magri.op")){
                if (args[0].equals("addadv") || args[0].equals("addeasy")) {
                    return Collections.singletonList("[時間(分)]");
                }
            }
            else if (args.length == 4 && sender.hasPermission("magri.op")){
                if (args[0].equals("addadv")) {
                    return Collections.singletonList("[セクション数]");
                }
            }
        }
        return null;
    }
}
