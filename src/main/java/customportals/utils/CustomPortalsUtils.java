package customportals.utils;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CustomPortalsUtils
{

    public static ItemStack getStack(String string)
    {
        int damage = stripMetadata(string);
        Item item = getItem(string);
        if (item==null) return getItemStack(string);
        return new ItemStack(item,1, Math.max(damage,0));
    }

    public static PortalBlock getPortalBlock(String string)
    {
        int damage = stripMetadata(string);
        Block block = getBlock(string);
        if (block==null) return null;
        return new PortalBlock(block,Math.min(damage,15));
    }

    public static Block getBlock(String string)
    {
        if (!string.contains(":")) return null;
        String[] split = string.split(":");
        return GameRegistry.findBlock(split[0],split[1]);
    }

    public static Item getItem(String string)
    {
        if (!string.contains(":")) return null;
        String[] split = string.split(":");
        return GameRegistry.findItem(split[0],split[1]);
    }

    public static ItemStack getItemStack(String string)
    {
        if (!string.contains(":")) return null;
        String[] split = string.split(":");
        return GameRegistry.findItemStack(split[0], split[1], 1);
    }

    public static int stripMetadata(String string)
    {
        if (!string.contains("@")) return 0;
        String[] split = string.split("@");
        string = split[0];
        if (split[1].equals("*")) return -1;
        try
        {
            return Integer.valueOf(split[1]);
        }catch(Exception e){
            return 0;
        }
    }
}
