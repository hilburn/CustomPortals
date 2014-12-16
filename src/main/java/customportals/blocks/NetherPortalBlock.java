package customportals.blocks;

import customportals.config.Settings;
import customportals.registry.BlockRegistry;
import customportals.tileentities.TileEntityPortal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Random;

public class NetherPortalBlock extends BlockPortal implements ITileEntityProvider
{
    public NetherPortalBlock()
    {
        this.isBlockContainer = true;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        TileEntity te = world.getTileEntity(x,y,z);
        if (te instanceof TileEntityPortal)
        {
            if (!((TileEntityPortal)te).allowTeleport(entity)) return;
        }
        else if (te != null) return;
        super.onEntityCollidedWithBlock(world,x,y,z,entity);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        if (Settings.disableNetherPortalPigmen) return;
        super.updateTick(world,x,y,z,rand);
    }

    @Override
    public boolean func_150000_e(World world, int x, int y, int z)
    {
        if (Settings.disableNetherPortal) return false;
        GeneratePortal portal = new GeneratePortal(world, x, y-1, z);
        if (!portal.checkForValidPortal()) return false;
        return portal.setPortal();
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        if (!Settings.lockNetherPortalToCreator || metadata < 3) return null;
        return new TileEntityPortal();
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        GeneratePortal portal = new GeneratePortal(world,x,y,z,world.getBlockMetadata(x,y,z));
        if (!portal.checkForValidPortal())
            portal.clearPortal();
    }

    @Override
    public boolean hasTileEntity(int metadata)
    {
        return metadata>2;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
    {
        super.breakBlock(world, x, y, z, block, metadata);
        world.removeTileEntity(x, y, z);
    }

    @Override
    public boolean onBlockEventReceived(World world, int x, int y, int z, int number, int argument)
    {
        super.onBlockEventReceived(world, x, y, z, number, argument);
        TileEntity tileentity = world.getTileEntity(x, y, z);
        return tileentity != null ? tileentity.receiveClientEvent(number, argument) : false;
    }

    public static class GeneratePortal
    {
        private World world;
        private int x;
        private int y;
        private int z;
        private int metadata;
        private EntityPlayer player;
        private ArrayList<ChunkCoordinates> portalBlocks = new ArrayList<ChunkCoordinates>();

        public GeneratePortal(World world, int x, int y, int z)
        {
            this.world = world;
            this.x=x;
            this.y=y;
            this.z=z;
        }

        public GeneratePortal(World world, int x, int y, int z, int metadata)
        {
            this(world,x,y,z);
            this.metadata = metadata;
        }

        public GeneratePortal(World world, int x, int y, int z, EntityPlayer player)
        {
            this(world,x,y,z);
            this.player = player;
        }

        public boolean checkForValidPortal()
        {
            if ((this.metadata & 3) != 0) return checkForValidPortal(metadata);
            for (int i = 1; i<3; i++)
            {
                if (checkForValidPortal(i))
                {
                    metadata = i + (player!=null?3:0);
                    return true;
                }
            }
            return false;
        }

        public boolean checkForValidPortal(int checkVal)
        {
            //TODO:Actually scan for portal
            return false;
        }


        public boolean setPortal()
        {
            for (ChunkCoordinates portalBlock : portalBlocks)
            {
                world.setBlock(portalBlock.posX,portalBlock.posY,portalBlock.posZ, BlockRegistry.customPortalBlock,metadata,3);
                if (metadata>2)
                {
                    TileEntity te = world.getTileEntity(portalBlock.posX, portalBlock.posY, portalBlock.posZ);
                    if (te instanceof TileEntityPortal)
                    {
                        ((TileEntityPortal)te).setOwner(player.getPersistentID());
                    }
                }
            }
            return true;
        }

        public void clearPortal()
        {
            for (ChunkCoordinates portalBlock : portalBlocks)
            {
                world.setBlockToAir(portalBlock.posX, portalBlock.posY, portalBlock.posZ);
                world.removeTileEntity(portalBlock.posX, portalBlock.posY, portalBlock.posZ);
            }
        }

        private ForgeDirection getRight(int metadata)
        {
            switch (metadata&3)
            {
                case 1:
                    return ForgeDirection.EAST;
                case 2:
                    return ForgeDirection.NORTH;
            }
            return null;
        }

        private ForgeDirection getLeft(int metadata)
        {
            switch (metadata&3)
            {
                case 1:
                    return ForgeDirection.WEST;
                case 2:
                    return ForgeDirection.SOUTH;
            }
            return null;
        }
    }
}
