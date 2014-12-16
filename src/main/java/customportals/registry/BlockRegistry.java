package customportals.registry;

import cpw.mods.fml.common.registry.GameRegistry;
import customportals.blocks.NetherPortalBlock;
import customportals.reference.Reference;
import customportals.tileentities.TileEntityPortal;
import customportals.utils.BlockHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;

public class BlockRegistry
{
    public static Block customPortalBlock;

    public static void replacePortalBlock()
    {
        BlockHandler.replaceBlock(Blocks.portal, NetherPortalBlock.class, ItemBlock.class);
        GameRegistry.registerTileEntity(TileEntityPortal.class, Reference.ID+".TileEntityPortal");
    }
}
