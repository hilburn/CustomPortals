package customportals.utils;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.regex.Pattern;

public class ProcessString
{
    private static final Pattern stackSize = Pattern.compile("^\\d+x");
    private String input;

    public ProcessString(String input)
    {
        this.input = input;
    }

    public ItemStack getItemStack()
    {
        if (input == null || input.length() == 0) return null;
        int size = stripStackSize();
        int damage = stripMetadata();
        Item item = getItem();
        if (item!=null) return new ItemStack(item,size,Math.max(damage,0));
        return getStack(size);
    }

    public PortalBlock getPortalBlock()
    {
        if (input == null || input.length() == 0) return null;
        int damage = stripMetadata();
        Block block = getBlock();
        return new PortalBlock(block,Math.min(damage,15));
    }

    private Block getBlock()
    {
        if (!input.contains(":")) return null;
        String[] split = input.split(":");
        return GameRegistry.findBlock(split[0], split[1]);
    }

    private Item getItem()
    {
        if (!input.contains(":")) return null;
        String[] split = input.split(":");
        return GameRegistry.findItem(split[0],split[1]);
    }

    private ItemStack getStack(int size)
    {
        if (!input.contains(":")) return null;
        String[] split = input.split(":");
        return GameRegistry.findItemStack(split[0], split[1], size);
    }

    private int stripMetadata()
    {
        if (!input.contains("@")) return 0;
        String[] split = input.split("@");
        input = split[0];
        if (split[1].equals("*")) return -1;
        try
        {
            return Integer.valueOf(split[1]);
        }catch(Exception e){
            return 0;
        }
    }

    private int stripStackSize()
    {
        if (!stackSize.matcher(input).find()) return 1;
        String[] split = input.split("x");
        input = input.substring(input.indexOf("x") + 1);
        try
        {
            return Math.max(Integer.valueOf(split[0]),1);
        }catch(Exception e){
            return 1;
        }
    }
}
