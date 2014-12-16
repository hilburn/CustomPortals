package customportals.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class CustomPortalsUtils
{
    public static void removeItemsFromPlayer(EntityPlayer player, ItemStack stack)
    {
        IInventory inventory = player.inventory;
        int stackSize = stack.stackSize;
        for (int i=0; i<inventory.getSizeInventory(); i++)
        {
            ItemStack itemStack = inventory.getStackInSlot(i);
            if (itemStack!=null && itemStack.isItemEqual(stack))
            {
                stackSize-=inventory.decrStackSize(i,stackSize).stackSize;
                if (stackSize<=0) return;
            }
        }
    }

    public static boolean isCreative(EntityPlayer player)
    {
        return player.capabilities.isCreativeMode;
    }

    public static boolean playerHasItems(EntityPlayer entity, ItemStack stack)
    {
        IInventory playerInventory = entity.inventory;
        int numItems = 0;
        for (int i = 0; i<playerInventory.getSizeInventory();i++)
        {
            ItemStack itemStack = playerInventory.getStackInSlot(i);
            if (itemStack!=null && itemStack.isItemEqual(stack)) numItems+=itemStack.stackSize;
            if (numItems>=stack.stackSize) return true;
        }
        return false;
    }
}
