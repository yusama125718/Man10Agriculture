package yusama1251718.man10agriculture;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Data {
    public static class Recipe{
        public String name;
        public ItemStack icon;
        public Integer time;
        public Byte water;
        public Byte fertilizer;
        public ItemStack material;
        public HashMap<ItemStack, Float> result;
        public Boolean dochange;
        public HashMap<ItemStack, Integer> chenge;

        public Recipe(String NAME, ItemStack ICON, Integer TIME, Byte WATER,Byte FERTILIZER, ItemStack MATERIAL, ItemStack RESULT){
            name = NAME;
            icon = ICON;
            time = TIME;
            water = WATER;
            fertilizer = FERTILIZER;
            material = MATERIAL;
            result.put(RESULT, 1f);
            dochange = false;
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
}
