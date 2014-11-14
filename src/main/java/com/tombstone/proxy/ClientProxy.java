package com.tombstone.proxy;

import com.tombstone.TombStone;
import com.tombstone.block.ItemRendererTombStone;
import com.tombstone.block.RendererTombStone;
import com.tombstone.block.TileEntityTombStone;
import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {
        
        @Override
        public void registerRenderers() {
               // MinecraftForgeClient.preloadTexture(ITEMS_PNG); // old way
               // MinecraftForgeClient.preloadTexture(BLOCK_PNG);
        }
        
        public static void setCustomRenderers()
        {
        	ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTombStone.class, new RendererTombStone());
        	// register item rendering
        	MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TombStone.blockTombStone), new ItemRendererTombStone());
        }
}
