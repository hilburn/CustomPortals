package customportals.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import customportals.config.Settings;
import customportals.tileentities.TileEntityPortal;
import customportals.utils.CustomPortalsUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Random;

public class NetherPortalBlock extends BlockPortal implements ITileEntityProvider
{
    public NetherPortalBlock()
    {
        this.isBlockContainer = true;
        this.setHardness(-1.0F);
        setStepSound(soundTypeGlass);
        setLightLevel(0.75F);
        setBlockName("portal");
        setBlockTextureName("portal");
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        int l = world.getBlockMetadata(x, y, z) % 3;

        if (l == 0)
        {
            if (world.getBlock(x - 1, y, z) != this && world.getBlock(x + 1, y, z) != this)
            {
                l = 2;
            }
            else
            {
                l = 1;
            }

            if (world instanceof World && !((World)world).isRemote)
            {
                ((World)world).setBlockMetadataWithNotify(x, y, z, l, 2);
            }
        }

        float f = 0.125F;
        float f1 = 0.125F;

        if (l == 1)
        {
            f = 0.5F;
        }

        if (l == 2)
        {
            f1 = 0.5F;
        }

        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, 1.0F, 0.5F + f1);
    }


    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        int i1 = 0;

        if (world.getBlock(x, y, z) == this)
        {
            i1 = world.getBlockMetadata(x, y, z % 3);

            if (i1 == 0)
            {
                return false;
            }

            if (i1 == 2 && side != 5 && side != 4)
            {
                return false;
            }

            if (i1 == 1 && side != 3 && side != 2)
            {
                return false;
            }
        }

        boolean flag = world.getBlock(x - 1, y, z) == this && world.getBlock(x - 2, y, z) != this;
        boolean flag1 = world.getBlock(x + 1, y, z) == this && world.getBlock(x + 2, y, z) != this;
        boolean flag2 = world.getBlock(x, y, z - 1) == this && world.getBlock(x, y, z - 2) != this;
        boolean flag3 = world.getBlock(x, y, z + 1) == this && world.getBlock(x, y, z + 2) != this;
        boolean flag4 = flag || flag1 || i1 == 1;
        boolean flag5 = flag2 || flag3 || i1 == 2;
        return flag4 && side == 4 ? true : (flag4 && side == 5 ? true : (flag5 && side == 2 ? true : flag5 && side == 3));
    }


    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        if (!checkForKey(world, entity)) return;
        TileEntity te = world.getTileEntity(x,y,z);
        if (te instanceof TileEntityPortal)
        {
            if (!((TileEntityPortal)te).allowTeleport(entity)) return;
        }
        else if (te != null) return;
        super.onEntityCollidedWithBlock(world,x,y,z,entity);
    }

    private boolean checkForKey(World world, Entity entity)
    {
        if (Settings.portalKeyNether==null || (!Settings.consumeKeyBothWays && (world.provider.dimensionId == -1))) return true;
        if (entity instanceof EntityPlayer)
        {
            if (CustomPortalsUtils.isCreative((EntityPlayer) entity)) return true;
            if (CustomPortalsUtils.playerHasItems((EntityPlayer) entity, Settings.portalKeyNether)) return true;
            return false;
        }
        return true;
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
        if (Settings.disableNetherFire) return false;
        GeneratePortal portal = new GeneratePortal(world, x, y, z);
        if (!portal.checkForValidPortal(false)) return false;
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
        if (block == Blocks.portal || Settings.portalBlockNether.isBlockMatch(block))
        {
            GeneratePortal portal = new GeneratePortal(world, x, y, z, world.getBlockMetadata(x, y, z));
            if (!portal.checkForValidPortal(true))
            {
                world.setBlockToAir(x, y, z);
                world.removeTileEntity(x, y, z);
            }
        }
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
        private boolean upOnly;
        private EntityPlayer player;
        private ArrayList<ChunkCoordinates[]> portalBlocks = new ArrayList<ChunkCoordinates[]>();

        private GeneratePortal(World world, int x, int y, int z, boolean upOnly)
        {
            this.world = world;
            this.x=x;
            this.y=y;
            this.z=z;
            this.upOnly = upOnly;
        }

        public GeneratePortal(World world, int x, int y, int z)
        {
            this(world,x,y,z,true);
        }

        public GeneratePortal(World world, int x, int y, int z, int metadata)
        {
            this(world,x,y,z,true);
            this.metadata = metadata;
        }

        public GeneratePortal(World world, int x, int y, int z, EntityPlayer player)
        {
            this(world,x,y,z,false);
            this.player = player;
        }

        public boolean checkForValidPortal(boolean justPortal)
        {
            if (world.provider.dimensionId == 1) return false;
            while (!isFrameBlock(x,y,z))
            {
                y--;
                if (y<1) return false;
            }
            if ((this.metadata % 3) != 0) return checkForValidPortal(metadata, justPortal);
            for (int i = 1; i<3; i++)
            {
                if (checkForValidPortal(i, justPortal))
                {
                    if (metadata%3==0) metadata = i + (player!=null?3:0);
                    return true;
                }
            }
            return false;
        }

        public boolean checkForValidPortal(int checkVal, boolean justPortal)
        {
            ForgeDirection right = getRight(checkVal);
            int blocksRight = 1;
            while (isFrameBlock(x+right.offsetX*blocksRight,y,z+right.offsetZ*blocksRight)) blocksRight++;
            int blocksLeft = 1;
            while (isFrameBlock(x-right.offsetX*blocksLeft,y,z-right.offsetZ*blocksLeft)) blocksLeft++;
            if (blocksRight+blocksLeft-1>=Settings.minNetherPortalWidth && blocksRight+blocksLeft-1<=Settings.maxNetherPortalWidth)
            {
                if (checkUp(x + right.offsetX * blocksRight, y, z + right.offsetZ * blocksRight, x - right.offsetX * blocksLeft, y, z - right.offsetZ * blocksLeft,justPortal)) return true;
                if (!upOnly) if (checkDown(x + right.offsetX * blocksRight, y, z + right.offsetZ * blocksRight, x - right.offsetX * blocksLeft, y, z - right.offsetZ * blocksLeft, justPortal)) return true;
            }
            if (!upOnly) if (checkSides(justPortal)) return true;
            return false;
        }

        public boolean checkSides(boolean justPortal)
        {
            for (int i = 2; i<6;i++)
            {
                ForgeDirection dir = ForgeDirection.getOrientation(i);
                GeneratePortal sidePortal = new GeneratePortal(world,x+dir.offsetX,y,z+dir.offsetZ,(i/2 % 2)+1);
                if (sidePortal.checkForValidPortal(justPortal))
                {
                    this.portalBlocks = sidePortal.portalBlocks;
                    this.metadata = sidePortal.metadata+3;
                    return true;
                }
            }
            return false;
        }

        private boolean checkUp(int x, int y, int z, int x2, int y2, int z2, boolean justPortal)
        {
            portalBlocks = new ArrayList<ChunkCoordinates[]>();
            while (checkLayer(Math.min(x,x2),y+1,Math.min(z,z2),Math.max(x,x2),y+1,Math.max(z,z2), justPortal)) {y++; y2++;}
            if (checkFrameLayer(Math.min(x,x2),y+1,Math.min(z,z2),Math.max(x,x2),y+1,Math.max(z,z2))) return true;
            return false;
        }

        private boolean checkDown(int x, int y, int z, int x2, int y2, int z2, boolean justPortal)
        {
            portalBlocks = new ArrayList<ChunkCoordinates[]>();
            while (checkLayer(Math.min(x,x2),y-1,Math.min(z,z2),Math.max(x, x2),y-1,Math.max(z, z2), justPortal)) {y--; y2--;}
            if (checkFrameLayer(Math.min(x,x2),y-1,Math.min(z,z2),Math.max(x,x2),y-1,Math.max(z,z2))) return true;
            return false;
        }

        private boolean checkFrameLayer(int x1, int y1, int z1, int x2, int y2, int z2)
        {
            if (portalBlocks.size()<Settings.minNetherPortalHeight || portalBlocks.size()>Settings.maxNetherPortalHeight) return false;
            for (ChunkCoordinates coordinates:portalBlocks.get(0))
            {
                if (!isFrameBlock(coordinates.posX,y1,coordinates.posZ)) return false;
            }
            return true;
        }

        private boolean checkLayer(int x1, int y1, int z1, int x2, int y2, int z2, boolean justPortal)
        {
            while (x1<x2 && !isFrameBlock(x1,y1,z1)) x1++;
            while (x2>x1 && !isFrameBlock(x2,y2,z2)) x2--;
            while (z1<z2 && !isFrameBlock(x1,y1,z1)) z1++;
            while (z2>z1 && !isFrameBlock(x2,y2,z2)) z2--;
            ChunkCoordinates[] layer = new ChunkCoordinates[Math.max(Math.max(x2-x1,z2-z1)-1,0)];
            if (layer.length==0) return false;
            int index = 0;
            for (int x = x1; x <= x2; x++)
            {
                for (int z = z1; z <= z2; z++)
                {
                    if ((x1!=x2 && (x==x1 || x==x2)) || (z1!=z2 && (z==z1 || z==z2))) continue;
                    if (!isValidInnerBlock(x, y1, z, justPortal)) return false;
                    layer[index++] = new ChunkCoordinates(x,y1,z);
                }
            }
            portalBlocks.add(layer);
            if (layer.length!=portalBlocks.get(0).length) return false;
            return true;
        }

        private boolean isValidInnerBlock(int x, int y, int z, boolean justPortal)
        {
            Block block = world.getBlock(x,y,z);
            return !justPortal && (block == Blocks.air || block == Blocks.fire) || block == Blocks.portal;
        }

        private boolean isFrameBlock(int x, int y, int z)
        {
            return Settings.portalBlockNether.isMatch(world,x,y,z);
        }

        public boolean setPortal()
        {
            for (ChunkCoordinates[] portalBlockLayer : portalBlocks)
            {
                for (ChunkCoordinates portalBlock:portalBlockLayer)
                {
                    world.setBlock(portalBlock.posX, portalBlock.posY, portalBlock.posZ, Blocks.portal, metadata, 3);
                    if (metadata > 2)
                    {
                        TileEntity te = world.getTileEntity(portalBlock.posX, portalBlock.posY, portalBlock.posZ);
                        if (te instanceof TileEntityPortal)
                        {
                            ((TileEntityPortal) te).setOwner(player.getPersistentID());
                        }
                    }
                }
            }
            return true;
        }

        private ForgeDirection getRight(int metadata)
        {
            switch (metadata % 3)
            {
                case 1:
                    return ForgeDirection.EAST;
                case 2:
                    return ForgeDirection.NORTH;
            }
            return null;
        }
    }
}
