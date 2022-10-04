package yusama1251718.man10agriculture;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class Data {
    public static class Recipe{
        public String name;
        public ItemStack icon;
        public Integer time;
        public Byte water;
        public Byte fertilizer;
        public ItemStack material;
        public List<Result> result;
        public Boolean dochange;
        public List<ItemStack> change;

        public Recipe(String NAME, ItemStack ICON, Integer TIME, Byte WATER,Byte FERTILIZER, ItemStack MATERIAL, ItemStack RESULT){
            name = NAME;
            icon = ICON;
            time = TIME;
            water = WATER;
            fertilizer = FERTILIZER;
            material = MATERIAL;
            result.add(new Result(RESULT, 1f));
            dochange = false;
        }
        public Recipe(String NAME, ItemStack ICON, Integer TIME, Byte WATER,Byte FERTILIZER, ItemStack MATERIAL, List<Result> RESULT, List<ItemStack> CHANGE){
            name = NAME;
            icon = ICON;
            time = TIME;
            water = WATER;
            fertilizer = FERTILIZER;
            material = MATERIAL;
            result = RESULT;
            dochange = true;
            change = CHANGE;
        }
    }

    public static class easyrecipe{
        public String name;
        public Integer time;

        public easyrecipe(String NAME, Integer TIME){
            name = NAME;
            time = TIME;
        }
    }

    public static class Result{
        public ItemStack item;
        public Float chance;

        public Result(ItemStack ITEM, Float CHANCE){
            item = ITEM;
            chance = CHANCE;
        }
    }
}
