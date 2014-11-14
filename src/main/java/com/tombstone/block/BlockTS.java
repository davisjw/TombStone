package com.tombstone.block;

import com.tombstone.handler.ConfigurationHandler;
import com.tombstone.reference.Textures;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTS extends BlockContainer {

    public BlockTS(Material mat){
        super(mat);
        setHardness(2.0F);                                      // Block hardness level
        setResistance(2000.0F);                                 // Set well above an normal material so that it's immune to explosions
        setStepSound(Block.soundTypeStone);                     // Sound type for movement sound
        this.setCreativeTab(CreativeTabs.tabDecorations);       // Which creative tab to list under
        setHarvestLevel(ConfigurationHandler.mineLevel, 0);     // What level tool can harvest this block
    }

    public BlockTS()
    {
        this(Material.rock);
    }

    @Override
    public String getUnlocalizedName()
    {
//        return String.format("tile.%s%s", Textures.RESOURCE_PREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
        return String.format("tile.%s%s", Textures.RESOURCE_PREFIX, super.getUnlocalizedName());
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityTombStone();
    }

    @Override
    public TileEntity createNewTileEntity(World world, int data) {
        return new TileEntityTombStone();
    }

}
