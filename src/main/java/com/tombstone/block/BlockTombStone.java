package com.tombstone.block;

import java.util.Random;

import com.tombstone.TombStone;
import com.tombstone.handler.ConfigurationHandler;
import com.tombstone.reference.Messages;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Team;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import com.tombstone.utility.LogHelper;

public class BlockTombStone extends BlockTS {
	IIcon[] icons;
	private int quantityDropped = 0;
	private boolean explosionRecover = false;
	private TileEntityTombStone tempEntity;

	public BlockTombStone() {
		super();
        setBlockName(LanguageRegistry.instance().getStringLocalization(Messages.BLOCK_TOMBSTONE));

//        setBlockName("Tombstone Block");
//		  setUnlocalizedName("Tombstone Block");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata)
	{
		//We use textures 0-5 for this block
		return icons[side];
	}



	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister iconRegister){
//	  public void registerIcons(net.minecraft.client.renderer.texture.IIconRegister iconRegister){
		icons = new IIcon[6];
		for(int i = 0; i < icons.length; i++){
			iconRegister.registerIcon("TombStone:block"+i); // assumed to be .png and in [src]/mods/[mod ID]/blocks/
		}
		
	}


	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int idk, float what, float these, float are) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (!ConfigurationHandler.allowGUI)     // if no GUI is allowed; exit out
            return false;

		if (tileEntity == null  || !(tileEntity instanceof TileEntityTombStone)) { // isSneaking check no longer valid (now part of default Minecraft behavior
			return false;
		}
		TileEntityTombStone tomb = (TileEntityTombStone)tileEntity;
		// security check
		switch (ConfigurationHandler.security){
		case 0:
			// no security, open to public looting
			player.openGui(TombStone.instance, 0, world, x, y, z);
			break;
		case 1:
			// team mode
//			if(player.getEntityName().equals(tomb.getOwner())){
            if(player.getCommandSenderName().equals(tomb.getOwner())){
                // this is their tomb
				player.openGui(TombStone.instance, 0, world, x, y, z);
			}
			Team looterTeam = player.getTeam(); // Team.func_96661_b() -> team name
			if(looterTeam  == null){
				// looter not on any team, can only loot their own tombs
				break;
			}
			String looterTeamName = looterTeam.getRegisteredName();
//			String looterTeamName = looterTeam.func_96661_b();
			String tombstoneTeamName = tomb.getTeamName();
            LogHelper.debug("[TombStone] onBlockActivated(...): " + looterTeam.getRegisteredName() + " =?= " + tombstoneTeamName);
			if(tombstoneTeamName.equals(TileEntityTombStone.nonteamName) || tombstoneTeamName.equals(looterTeamName)){
				// those on teams can loot the tombs of those who died while not on a team (teamless players at disadvantage)
				player.openGui(TombStone.instance, 0, world, x, y, z);
			}
			break;
		default:
//			if(player.getEntityName().equals(tomb.getOwner())){
            if(player.getCommandSenderName().equals(tomb.getOwner())){
				player.openGui(TombStone.instance, 0, world, x, y, z);
			}
			break;
		}
		
		
		return true;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
//    public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		dropItems(world, x, y, z);
		
		TileEntityTombStone tileEntity = (TileEntityTombStone) world.getTileEntity(x, y, z);
		if(tileEntity.isCrafted())
		{
			//TODO - Do something
			quantityDropped = 1;
		}
		world.setBlockMetadataWithNotify(x, y, z, 0, 0);
		super.breakBlock(world, x, y, z, par5, par6);
		
		for(int i=0; i<TombStone.instance.tombList.size(); i++)
		{
			TileEntityTombStone item = TombStone.instance.tombList.get(i);
			if(item.xCoord == x && item.yCoord == y && item.zCoord == z)
			{
				TombStone.instance.tombList.remove(i);
				break;
			}
		}
	}
	
	@Override
	public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6)
	{
		super.harvestBlock(par1World, par2EntityPlayer, par3, par4, par5, par6);

		//Reset the drop quantity		
		if(quantityDropped == 1)
			quantityDropped = 0;
	}

	@Override public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		super.onBlockPlacedBy(w, x, y, z, entity, itemStack);
		int rot = TileEntityTombStone.getRotationFromEntity(entity);
		w.setBlockMetadataWithNotify(x, y, z, rot, 1 | 2);
	}
	
	@Override
    public int quantityDropped(Random par1Random)
    {
        return quantityDropped;
    }
	
	private void dropItems(World world, int x, int y, int z){
		if(explosionRecover)
			return;
		
		Random rand = new Random();
		
		//Capture the TombStoneTileEntity based on the position of the block
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (!(tileEntity instanceof IInventory)) {
			return;
		}
		
		//And get the inventory space of the tile entity
		IInventory inventory = (IInventory) tileEntity;
		
		//Loop through all slots in the tombstone
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack item = inventory.getStackInSlot(i);
			
			//Only do something if the slot is valid
			if (item != null && item.stackSize > 0) {
				//Calculate a random velocity for the item
				float rx = rand.nextFloat() * 0.8F + 0.1F;
				float ry = rand.nextFloat() * 0.8F + 0.1F;
				float rz = rand.nextFloat() * 0.8F + 0.1F;
				
				//Create the item
				EntityItem entityItem = new EntityItem(world,
				                x + rx, y + ry, z + rz,
				                new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));
//                                new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));

				//Copy any NBT tags associated with the item
				if (item.hasTagCompound()) {
					// func_92014_d() appears to be getEntityItem()
					entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
				}
				
				//Assign the velocity
				float factor = 0.05F;
				entityItem.motionX = rand.nextGaussian() * factor;
				entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = rand.nextGaussian() * factor;
				
				//Spawn the item
				world.spawnEntityInWorld(entityItem);
				
				//Set the local slot to empty
				item.stackSize = 0;
			}
		}
	}

    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    public int getRenderType()
    {
        return -1;
    }
    
    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return false;
    }
    
    public boolean canDropFromExplosion(Explosion par1Explosion)
    {
		//DEBUG//
//        FMLLog.log(Level.WARNING, "[TombStone] TombStoneBlock.canDropFromExplosion(): " + par1Explosion.exploder.getEntityName());
        LogHelper.warn("[TombStone] TombStoneBlock.canDropFromExplosion(): " + par1Explosion.exploder.getCommandSenderName());

		explosionRecover = true;

        return true;
    }
    
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7)
    {
    	super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, par7);
    	
    	//If we're trying to recover from an explosion, backup the tile entity
    	if(explosionRecover){
//            tempEntity = (TombStoneTileEntity) par1World.getBlockTileEntity(par2, par3, par4);
    		tempEntity = (TileEntityTombStone) par1World.getTileEntity(par2, par3, par4);
    	}
    }
    @Override
    public void onBlockDestroyedByExplosion(World par1World, int par2, int par3, int par4, Explosion expl)
    {
		//DEBUG//
//		FMLLog.log(Level.WARNING, "[TombStone] TombStoneBlock.onBlockDestroyedByExplosion(): " + par2 + "," + par3 + "," + par4);
        LogHelper.warn("[TombStone] TombStoneBlock.onBlockDestroyedByExplosion(): " + par2 + "," + par3 + "," + par4);
   	
		//We get here because the tombstone is placed between doExplosionA() and doExplosionB() because that's when the player dies
		//This creates a problem because doExplosionA() is the one that checks the explosion resistance for blocks ...
		//Ergo the location where the tombstone is about to exist has already been marked for demolition
		
		//If destroyed by explosion place it right back
//		  par1World.setBlock(par2, par3, par4, TombStone.instance.tombStoneBlockId, tempEntity.blockMetadata, 1 | 2);
//        TombStoneTileEntity blockTileEntity = (TombStoneTileEntity) par1World.getBlockTileEntity(par2, par3, par4);
        par1World.setBlock(par2, par3, par4, TombStone.instance.blockTombStone, tempEntity.blockMetadata, 1 | 2);
		TileEntityTombStone blockTileEntity = (TileEntityTombStone) par1World.getTileEntity(par2, par3, par4);
		blockTileEntity.setOwner(tempEntity.getOwner());
		blockTileEntity.setDeathText(tempEntity.getDeathText());
		blockTileEntity.setIsCrafted(tempEntity.isCrafted());
		blockTileEntity.setTeam(tempEntity.getTeamName());
		for(int i=0; i<tempEntity.getSizeInventory(); i++)
		{
			ItemStack playerItem = tempEntity.getStackInSlot(i);
			blockTileEntity.setInventorySlotContents(i, playerItem);
		}
		// drop Tombstone down to ground
		

		tempEntity = null;
		explosionRecover = false;
    }
}
