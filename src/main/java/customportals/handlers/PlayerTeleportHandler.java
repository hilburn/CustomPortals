package customportals.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import customportals.config.Settings;
import customportals.utils.CustomPortalsUtils;

public class PlayerTeleportHandler
{
    @SubscribeEvent
    public void teleportEvent(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        if (Settings.consumeKeyItem && !CustomPortalsUtils.isCreative(event.player))
        {
            if (event.fromDim == 0 && event.toDim == -1)
                CustomPortalsUtils.removeItemsFromPlayer(event.player, Settings.portalKeyNether);
            if (Settings.consumeKeyBothWays)
            {
                if (event.fromDim == -1 && event.toDim == 0)
                    CustomPortalsUtils.removeItemsFromPlayer(event.player, Settings.portalKeyNether);
            }
        }
    }
}
