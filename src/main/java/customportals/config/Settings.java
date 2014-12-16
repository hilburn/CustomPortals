package customportals.config;

import customportals.utils.CustomPortalsUtils;
import customportals.utils.PortalBlock;
import net.minecraft.item.ItemStack;

public final class Settings
{
    public static final String NETHER = "nether";
    public static final String TWILIGHT = "twilight_forest";

    public static boolean disableNetherFire;
    public static boolean disableNetherPortalPigmen;
    public static boolean consumePortalItem;
    public static String netherPortalBlock;
    public static String netherPortalItem;
    public static String netherPortalKey;
    public static int maxNetherPortalWidth;
    public static int minNetherPortalWidth;
    public static int maxNetherPortalHeight;
    public static int minNetherPortalHeight;
    public static boolean lockNetherPortalToCreator;

    public static String twilightForestPortalItem;
    public static String twilightForestPortalKey;
    public static boolean lockTwilightForestPortalToCreator;

    public static PortalBlock portalBlockNether;
    public static ItemStack portalItemNether;
    public static ItemStack portalKeyNether;

    public static void reload()
    {
        portalBlockNether = CustomPortalsUtils.getPortalBlock(netherPortalBlock);
        portalItemNether = CustomPortalsUtils.getItemStack(netherPortalItem);
        portalKeyNether = CustomPortalsUtils.getItemStack(netherPortalKey);
        if (portalBlockNether==null) disableNetherFire = true;
        int min = Math.min(minNetherPortalWidth,maxNetherPortalWidth);
        int max = Math.max(minNetherPortalWidth,maxNetherPortalWidth);
        minNetherPortalWidth = min;
        maxNetherPortalWidth = max;
        min = Math.min(minNetherPortalHeight,maxNetherPortalHeight);
        max = Math.max(minNetherPortalHeight,maxNetherPortalHeight);
        minNetherPortalHeight = min;
        maxNetherPortalHeight = max;

    }
}
