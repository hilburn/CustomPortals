package customportals.utils;

import customportals.reference.Names;
import net.minecraft.util.StatCollector;

public class TranslationHelper
{
    public static String localConfig(String key)
    {
        return StatCollector.translateToLocal(Names.configPrefix + key);
    }

    public static boolean canTranslate(String key)
    {
        return StatCollector.canTranslate(key);
    }
}
