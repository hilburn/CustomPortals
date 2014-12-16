package customportals.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import customportals.blocks.NetherPortalBlock;
import customportals.config.Settings;
import customportals.utils.CustomPortalsUtils;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class PortalActivateHandler
{
    @SubscribeEvent
    public void onHitBlock(PlayerInteractEvent event)
    {
        if (event.world.isRemote) return;
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && Settings.portalItemNether!=null && Settings.portalBlockNether!=null &&
                Settings.portalBlockNether.isMatch(event.world, event.x, event.y, event.z) && Settings.portalItemNether.isItemEqual(event.entityPlayer.getCurrentEquippedItem())
                && (CustomPortalsUtils.playerHasItems(event.entityPlayer, Settings.portalItemNether) || CustomPortalsUtils.isCreative(event.entityPlayer)))
        {
            NetherPortalBlock.GeneratePortal portal = new NetherPortalBlock.GeneratePortal(event.world, event.x, event.y, event.z, event.entityPlayer);
            if (portal.checkForValidPortal(false))
            {
                portal.setPortal();
                if (Settings.consumePortalItem && !CustomPortalsUtils.isCreative(event.entityPlayer)) CustomPortalsUtils.removeItemsFromPlayer(event.entityPlayer, Settings.portalItemNether);
            }
        }
    }
}
