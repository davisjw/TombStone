package com.tombstone.handler;

import com.tombstone.gui.TombStoneGUI;
import com.tombstone.block.ContainerTombStone;
import com.tombstone.block.TileEntityTombStone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class TombStoneGUIHandler implements IGuiHandler {
	//returns an instance of the Container you made earlier
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
//		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        TileEntity tileEntity = world.getTileEntity(x, y, z);
		if(tileEntity instanceof TileEntityTombStone){
		        return new ContainerTombStone(player.inventory, (TileEntityTombStone) tileEntity);
		}
		return null;
	}
	
	//returns an instance of the Gui you made earlier
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
//        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if(tileEntity instanceof TileEntityTombStone){
			return new TombStoneGUI(player.inventory, (TileEntityTombStone) tileEntity);
		}
		return null;
	}
}