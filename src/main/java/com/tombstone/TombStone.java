/**
 * 2013.06.17 - Update for Minecraft 1.5.2 and MinecraftFortge 7.8.1.737 by 
 * Cyanobacterium (Minecraft user Synechocystis)
 *
 * 2014.10.01 - Update for Minecraft 1.7.10 and MinecraftForge 1207 by MadMaxJD
 */
package com.tombstone;

import java.util.ArrayList;
import java.util.List;

import com.tombstone.block.BlockTombStone;
import com.tombstone.block.TileEntityTombStone;
import com.tombstone.handler.ConfigurationHandler;
import com.tombstone.handler.TombStoneGUIHandler;
import com.tombstone.proxy.ClientProxy;
import com.tombstone.proxy.IProxy;
import com.tombstone.reference.Reference;
import com.tombstone.utility.ChatHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import com.tombstone.utility.LogHelper;
import com.tombstone.reference.Messages;

@Mod(modid=Reference.MOD_ID, name=Reference.MOD_NAME, certificateFingerprint = Reference.FINGERPRINT, version=Reference.VERSION, guiFactory = Reference.GUI_FACTORY_CLASS)
public class TombStone {
//	public static int tombStoneBlockId;
	public static BlockTombStone blockTombStone;
	
	//Keeps track of the existing tombs
	public static List<TileEntityTombStone> tombList = new ArrayList<TileEntityTombStone>();
	
	// The instance of your mod that Forge uses.
	@Instance(Reference.MOD_ID)
	public static TombStone instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide=Reference.CLIENT_PROXY_CLASS, serverSide=Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    @Mod.EventHandler
    public void invalidFingerPrint(FMLFingerprintViolationEvent event)
    {
        // Report (log) to the user that the version of TombStone they are
        // using has been changed/tampered with
        if (Reference.FINGERPRINT.equals("@FINGERPRINT@")) {
            LogHelper.info(Messages.NO_FINGERPRINT_MESSAGE);
        }
        else
        {
            LogHelper.warn(Messages.INVALID_FINGERPRINT_MESSAGE);
        }
    }

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
        FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
		blockTombStone = new BlockTombStone();
	}
	
	@Mod.EventHandler
	public void load(FMLInitializationEvent event) {
		proxy.registerRenderers();
		
		if(event.getSide().isClient())
			ClientProxy.setCustomRenderers();
		
		//Register the tombstone block
		GameRegistry.registerBlock(blockTombStone, "TombStoneBlock");
//        LanguageRegistry.getStringLocalization(String "tombstone.block.tombstoneblockname");
//		  LanguageRegistry.addName(blockTombStone, "TombStone Block"); // removed in 1.8

		//Register the death hook
		MinecraftForge.EVENT_BUS.register(new DeathEventHook());

		//Register the tombstone tile entity
		GameRegistry.registerTileEntity(TileEntityTombStone.class, "TombStoneTileEntity");

		//Register the tombstone gui
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new TombStoneGUIHandler());
//		NetworkRegistry.instance().registerGuiHandler(this, new TombStoneGUIHandler());
		    
		//Item stack (ID, Count, Meta)
        ItemStack stoneStack = new ItemStack(Blocks.stone);
		ItemStack signStack = new ItemStack(Items.sign);

		//3x3 shaped crafting
		if(ConfigurationHandler.canCraft){
			GameRegistry.addRecipe(new ItemStack(blockTombStone), " x ", "xyx", "xxx",
		    'x', stoneStack, 'y', signStack);
		}    
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// Stub Method
	}
	
	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new ChatHandler());
	}
}