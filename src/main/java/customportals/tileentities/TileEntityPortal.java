package customportals.tileentities;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;

import java.util.UUID;

public class TileEntityPortal extends TileEntity
{
    private UUID owner;

    public void setOwner(UUID owner)
    {
        this.owner = owner;
    }

    public boolean allowTeleport(Entity entity)
    {
        if (entity instanceof EntityPlayer)
        {
            if (((EntityPlayer)entity).capabilities.isCreativeMode) return true;
        }
        return entity.getPersistentID().equals(owner);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        tagCompound.setString("UUID",owner.toString());
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        owner = UUID.fromString(tagCompound.getString("UUID"));
    }
}
