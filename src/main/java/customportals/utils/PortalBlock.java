package customportals.utils;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class PortalBlock
{
    private Block block;
    private int meta;

    public PortalBlock(Block block, int meta)
    {
        this.block = block;
        this.meta = meta;
    }

    public boolean isMatch(World world, int x, int y, int z)
    {
        return world.getBlock(x,y,z) == block && (meta == -1 || world.getBlockMetadata(x,y,z) == meta);
    }
}
