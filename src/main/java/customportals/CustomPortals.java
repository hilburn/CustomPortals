package customportals;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import customportals.config.ConfigHandler;
import customportals.reference.Metadata;
import customportals.reference.Reference;

@Mod(modid = Reference.ID, name = Reference.NAME, version = Reference.VERSION_FULL, guiFactory = Reference.ID_LOWERCASE + ".gui.ModGuiFactory")
public class CustomPortals
{
	public static boolean isTFLoaded = false; //Loader.isModLoaded("TwilightForest");

	@Instance(Reference.ID)
	public static CustomPortals instance = new CustomPortals();

	@Mod.Metadata(Reference.ID)
	public static ModMetadata metadata;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
//		BlockHandler.replaceBlock(Blocks.portal, BlockNullPortal.class, ItemBlock.class);
		metadata = Metadata.init(metadata);
		FMLCommonHandler.instance().bus().register(new ConfigHandler(event.getSuggestedConfigurationFile()));
	}

}
