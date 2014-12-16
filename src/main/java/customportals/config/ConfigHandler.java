package customportals.config;

import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import customportals.CustomPortals;
import customportals.reference.Names;
import customportals.reference.Reference;
import customportals.utils.TranslationHelper;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler
{
    public static Configuration config;

    public ConfigHandler(File configFile)
    {
        if (config == null)
        {
            config = new Configuration(configFile);
            loadConfig();
        }
    }

    @SubscribeEvent
    public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.modID.equalsIgnoreCase(Reference.ID))
        {
            loadConfig();
        }
    }

    private static void loadConfig()
    {
        Settings.consumeKeyItem = getBoolean("consumeKeyItems",Configuration.CATEGORY_GENERAL,true);
        Settings.consumeKeyBothWays = getBoolean("consumeKeyBothWays",Configuration.CATEGORY_GENERAL,true);

        Settings.disableNetherFire = getBoolean("disableFireLighting", Settings.NETHER,true);
        Settings.disableNetherPortalPigmen = getBoolean("disableNetherPigmen", Settings.NETHER,true);
        Settings.consumePortalItem = getBoolean("consumePortalItem", Settings.NETHER,true);
        Settings.netherPortalBlock = getString("netherPortalBlock", Settings.NETHER, "minecraft:obsidian");
        Settings.netherPortalItem = getString("netherPortalItem", Settings.NETHER, "");
        Settings.netherPortalKey = getString("netherPortalKey", Settings.NETHER, "");
        Settings.lockNetherPortalToCreator = getBoolean("lockNether", Settings.NETHER, false);
        Settings.minNetherPortalWidth = getInt("minNetherWidth", Settings.NETHER_DIMENSIONS, 2, 1, 23);
        Settings.maxNetherPortalWidth = getInt("maxNetherWidth", Settings.NETHER_DIMENSIONS, 23, 1, 23);
        Settings.minNetherPortalHeight = getInt("minNetherHeight", Settings.NETHER_DIMENSIONS, 3, 2, 23);
        Settings.maxNetherPortalHeight = getInt("maxNetherHeight", Settings.NETHER_DIMENSIONS,23,2,23);

        if (CustomPortals.isTFLoaded)
        {
            Settings.twilightForestPodzol = getBoolean("twilightForestPodzol",Settings.TWILIGHT,false);
            Settings.twilightForestPortalItem = getString("twilightPortalItem", Settings.TWILIGHT, "");
            Settings.twilightForestPortalKey = getString("twilightPortalKey", Settings.TWILIGHT, "");
            Settings.lockTwilightForestPortalToCreator = getBoolean("lockTwilight", Settings.TWILIGHT, false);
        }

        if (config.hasChanged())
        {
            config.save();
        }
        Settings.reload();
    }

    private static boolean getBoolean(String name, String category, boolean val)
    {
        return config.getBoolean(TranslationHelper.localConfig(name), category, val, TranslationHelper.localConfig(name+Names.configSuffix));
    }

    private static String getString(String name, String category, String val)
    {
        return config.getString(TranslationHelper.localConfig(name), category, val, TranslationHelper.localConfig(name+Names.configSuffix));
    }

    private static int getInt(String name, String category, int val, int min, int max)
    {
        return config.getInt(TranslationHelper.localConfig(name), category, val,min,max, TranslationHelper.localConfig(name+Names.configSuffix));
    }

    public static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        list.addAll(new ConfigElement(config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements());
        list.addAll(new ConfigElement(config.getCategory(Settings.NETHER)).getChildElements());
        list.addAll(new ConfigElement(config.getCategory(Settings.NETHER_DIMENSIONS)).getChildElements());
        if (CustomPortals.isTFLoaded) list.addAll(new ConfigElement(config.getCategory(Settings.TWILIGHT)).getChildElements());
        return list;
    }
}
