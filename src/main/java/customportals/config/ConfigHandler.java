package customportals.config;

import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.Loader;
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

        Settings.disableNetherPortal = getBoolean("disableNether", Settings.NETHER,true);
        Settings.disableNetherPortalPigmen = getBoolean("disableNetherPigmen", Settings.NETHER,true);
        Settings.netherPortalBlock = getString("netherPortalBlock", Settings.NETHER, "minecraft:obsidian");
        Settings.netherPortalItem = getString("netherPortalItem", Settings.NETHER, "");
        Settings.netherPortalKey = getString("netherPortalKey", Settings.NETHER,"");
        Settings.lockNetherPortalToCreator = getBoolean("lockNether", Settings.NETHER,false);

        if (CustomPortals.isTFLoaded)
        {
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

    public static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        list.addAll(new ConfigElement(config.getCategory(Settings.NETHER)).getChildElements());
        if (CustomPortals.isTFLoaded) list.addAll(new ConfigElement(config.getCategory(Settings.TWILIGHT)).getChildElements());
        return list;
    }
}
