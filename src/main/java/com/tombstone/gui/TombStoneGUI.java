package com.tombstone.gui;

import com.tombstone.block.ContainerTombStone;
import com.tombstone.block.TileEntityTombStone;
import com.tombstone.reference.Textures;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class TombStoneGUI extends GuiContainer {
	
	
	public TombStoneGUI (InventoryPlayer inventoryPlayer,
		TileEntityTombStone tileEntity) {
		//the container is instantiated and passed to the superclass for handling
		super(new ContainerTombStone(inventoryPlayer, tileEntity));
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		//draw text and stuff here
		//the parameters for drawString are: string, x, y, color
//		fontRenderer.drawString("Tombstone", 8, 6, 4210752);
        fontRendererObj.drawString("Tombstone", 8, 6, 4210752);
		//draws "Inventory" or your regional equivalent
//		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 39, 4210752);
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 39, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		//draw your Gui here, only thing you need to change is the path
		//int texture = mc.renderEngine.getTexture("/gui/container.png");// int version is private now
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        this.mc.renderEngine.bindTexture(TombStone.tombstoneGUI);
		this.mc.renderEngine.bindTexture(Textures.Misc.TOMBSTONE_UI);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, 6 * 18 + 17);
        this.drawTexturedModalRect(x, y + 6 * 18 + 17, 0, 126, this.xSize, 96);
	}
	
}
