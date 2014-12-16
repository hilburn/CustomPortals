package customportals.tileentities;

import net.minecraft.entity.Entity;
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
        return entity.getPersistentID().equals(owner);
    }

}
