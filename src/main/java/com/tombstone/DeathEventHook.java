package com.tombstone;

import java.util.ArrayList;
import java.util.Calendar;

import com.tombstone.block.TileEntityTombStone;
import com.tombstone.handler.ConfigurationHandler;
import com.tombstone.utility.LogHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;

public class DeathEventHook {
	
	public DeathEventHook()
	{
		
	}
	
	@SubscribeEvent
	public void onEntityDeath(PlayerDropsEvent event)
	{
		EntityPlayer deadPlayer = event.entityPlayer;
		DamageSource attackSource = event.source;
		ArrayList<EntityItem> drops = event.drops;
		World world = deadPlayer.worldObj;

		//DEBUG//

        if (ConfigurationHandler.allowEmpty || (drops.size() > 0)) {
            //LogHelper.debug("[TombStone] onEntityDeath(): " + attackSource.func_151519_b(deadPlayer).getFormattedText());
            //Calculate the spot to put the tombstone
            int tombX = (int) Math.floor(deadPlayer.posX);
            int tombY = (int) Math.floor(deadPlayer.posY);
            int tombZ = (int) Math.floor(deadPlayer.posZ);
            if (tombY < 0) {
                // You fell into the void! Sucks to be you.
                return;
            } else if (tombY > 255) {
                tombY = 255;
            }
            int rotation = TileEntityTombStone.getRotationFromEntity(deadPlayer);
            // get team info for security
            Team t = deadPlayer.getTeam();
            String teamName;
            if (t != null) {
                //			teamName = t.func_96661_b();
                teamName = t.getRegisteredName();
            } else {
                teamName = TileEntityTombStone.nonteamName;
            }

            // move down to surface if in air
            if (world.isAirBlock(tombX, tombY, tombZ)) {
                while (tombY > 0 && world.isAirBlock(tombX, tombY - 1, tombZ)) {
                    tombY--;
                }
            }
            // move up to surface if buried
            while (tombY < 255 && world.isAirBlock(tombX, tombY, tombZ) == false) {
                tombY++;
            }

            String dateOfDeath = ConfigurationHandler.dateFormat
                    .replace("m", (world.getCurrentDate().get(Calendar.MONTH) + 1) + "")
                    .replace("d", world.getCurrentDate().get(Calendar.DAY_OF_MONTH) + "")
                    .replace("y", world.getCurrentDate().get(Calendar.YEAR) + "")
                    .replace("H", world.getCurrentDate().get(Calendar.HOUR) + "")
                    .replace("M", world.getCurrentDate().get(Calendar.MINUTE) + "")
                    .replace("S", world.getCurrentDate().get(Calendar.SECOND) + "");
            //		String deathMessage = attackSource.getDeathMessage(deadPlayer) + " here\n Died " + dateOfDeath;
            String deathMessage = "R.I.P.\n" + attackSource.func_151519_b(deadPlayer).getUnformattedTextForChat() + " here on\n" + dateOfDeath;


            //Place the tombstone
            //        world.setBlock(tombX, tombY, tombZ, TombStone.instance.tombStoneBlockId, rotation, 1 | 2);
            world.setBlock(tombX, tombY, tombZ, TombStone.instance.blockTombStone, rotation, 1 | 2);
            TileEntityTombStone blockTileEntity = (TileEntityTombStone) world.getTileEntity(tombX, tombY, tombZ);

            //Move all items from the list to the tombstone inventory
            for (int i = 0; i < drops.size(); i++) {
                ItemStack playerItem = drops.get(i).getEntityItem();
                blockTileEntity.setInventorySlotContents(i, playerItem);
            }
            //Set the other meta-data for the tile entity
            //        blockTileEntity.setOwner(deadPlayer.getEntityName());
            blockTileEntity.setOwner(deadPlayer.getCommandSenderName());
            blockTileEntity.setTeam(teamName);
            blockTileEntity.setDeathText(deathMessage);
            blockTileEntity.setIsCrafted(false);
            //	blockTileEntity.setRotation(rotation); // rotation handled by metadata (just like a sign)
        }
        else {
            LogHelper.debug("[TombStone] onEntityDeath(): Tombstone not created due to config");
        }
		event.setCanceled(true);
	}
}
