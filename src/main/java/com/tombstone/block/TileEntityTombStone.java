package com.tombstone.block;

import com.tombstone.TombStone;
import com.tombstone.utility.LogHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.Constants;

public class TileEntityTombStone extends TileEntity implements IInventory {

	private ItemStack[] inv;
	public static final String nonteamName = "";
	//NBT tag(s)
	private String owner = ".";
	private String ownerTeam = nonteamName;
	private String deathText = "R.I.P.";
	private boolean isCrafted = true;
	private boolean isAddedToTombList = false;
	
	public TileEntityTombStone(){
		//A tombstone holds as much as a double-wide chest (54+)
//        inv = new ItemStack[54];
		inv = new ItemStack[108];
		
	}
	
	public TileEntityTombStone(String newOwner, String newDeathText, boolean newIsCrafted)
	{
		//A tombstone holds as much as a double-wide chest (54+)
//        inv = new ItemStack[54];
		inv = new ItemStack[108];
		
		this.owner = newOwner;
		this.deathText = newDeathText;
		this.isCrafted = newIsCrafted;
	}
	
	public void finalize() throws Throwable
	{
		super.finalize();
		
		TombStone.instance.tombList.remove(this);
	}
	
	///////////////////////////////////////
	
	public boolean isCrafted()
	{
		return this.isCrafted;
	}
	
	public void setIsCrafted(boolean newIsCrafted)
	{
		this.isCrafted = newIsCrafted;
	}	
	
	public String getOwner()
	{
		return this.owner;
	}
	
	public String getTeamName(){
		return ownerTeam;
	}
	
	public void setOwner(String newOwner)
	{
		this.owner = newOwner;
	}
	
	public void setTeam(String newTeamName)
	{
		ownerTeam = newTeamName;
	}
	
	public String getDeathText()
	{
		return this.deathText;
	}
	
	public void setDeathText(String newDeathText)
	{
		this.deathText = newDeathText;
	}
	
	
	public static int getRotationFromEntity(Entity p){
		return MathHelper.floor_double((double)((p.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 0x0F;
	}

	public void setRotation(int rot){
		this.blockMetadata = rot;
	}
	///////////////////////////////////////

    @Override
    public boolean hasCustomInventoryName() { return true; }

	@Override
	public int getSizeInventory() {
		return inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inv[slot];
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inv[slot] = stack;
		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}               
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			if (stack.stackSize <= amt) {
				setInventorySlotContents(slot, null);
			} else {
				stack = stack.splitStack(amt);
				if (stack.stackSize == 0) {
				setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this &&
			player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}
	
	@Override
	public void openInventory() {}
	
	@Override
	public void closeInventory() {}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		
		owner = tagCompound.getString("owner");
		deathText = tagCompound.getString("deathText");
		isCrafted = tagCompound.getBoolean("isCrafted");
		if(tagCompound.hasKey("team")) ownerTeam = tagCompound.getString("team");
		
        LogHelper.debug("Loaded Tombstone: owner="+owner+", deathText="+deathText+", isCrafted="+isCrafted);

//        NBTTagList tagList = tagCompound.getTagList("Inventory");
        NBTTagList tagList = tagCompound.getTagList("Inventory", Constants.NBT.TAG_LIST);
		for (int i = 0; i < tagList.tagCount(); i++) {
//			NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
            NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < inv.length) {
				inv[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		tagCompound.setString("owner", owner);
		tagCompound.setString("team", ownerTeam);
		tagCompound.setString("deathText", deathText);
		tagCompound.setBoolean("isCrafted", isCrafted);
		                
		//FMLLog.log(Level.WARNING, "Saved Tombstone: owner=%s, deathText=%s, isCrafted=%b", owner, deathText, isCrafted);
        LogHelper.debug("Saved Tombstone: owner="+owner+", deathText="+deathText+", isCrafted="+isCrafted);

		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < inv.length; i++) {
			ItemStack stack = inv[i];
			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
			stack.writeToNBT(tag);
			itemList.appendTag(tag);
			}
		}
		tagCompound.setTag("Inventory", itemList);
	}
	
	@Override
	public String getInventoryName() {
		return "tco.tombstonetileentity";
	}
	
	@Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        readFromNBT(pkt.func_148857_g());
//		readFromNBT(pkt.data);

		boolean foundDuplicate = false;
		for(int i=0; i<TombStone.instance.tombList.size(); i++)
		{
			TileEntityTombStone item = TombStone.instance.tombList.get(i);
			if(item.xCoord == this.xCoord && item.yCoord == this.yCoord && item.zCoord == this.zCoord)
			{
				foundDuplicate = true;
			}
		}
		
		if(!isAddedToTombList && !foundDuplicate)
			TombStone.instance.tombList.add(this);
    }
	
	@Override
	public Packet getDescriptionPacket()
    {
        NBTTagCompound nbtData = new NBTTagCompound();
        this.writeToNBT(nbtData);
        
		boolean foundDuplicate = false;
		for(int i=0; i<TombStone.instance.tombList.size(); i++)
		{
			TileEntityTombStone item = TombStone.instance.tombList.get(i);
			if(item.xCoord == this.xCoord && item.yCoord == this.yCoord && item.zCoord == this.zCoord)
			{
				foundDuplicate = true;
			}
		}
		
		if(!isAddedToTombList && !foundDuplicate)
			TombStone.instance.tombList.add(this);
        
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbtData);
    }
/*
	@Override
	public boolean isInvNameLocalized() {
		return true; // return false to look-up localized name from language pack
	}
	// 145818_k I thought replaced isInvNameLocalized
    @Override
    public boolean func_145818_k_() {
        return true;
    }
*/

	/**
     * DOC: Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false; // don't allow items to go in (only out)
		//return true; // no special restrictions
	}
	
}