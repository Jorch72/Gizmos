package planetguy.gizmos.tool;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import planetguy.simpleLoader.SLLoad;

@SLLoad(name="redstoneWandBlock",primacy=13)
public class BlockRedstoneWand extends BlockRedstoneOre{

	@SLLoad
	public BlockRedstoneWand(int par1) {
		super(true);//I think...
	}
	
	public boolean isOpaqueCube(){
		return false;
	}
	
	public int getRenderType(){
		return Blocks.grass.getRenderType();
	}
	
	public void onBlockAdded(World w, int x, int y, int z){
		w.scheduleBlockUpdate(x, y, z, this, 20);
	}

	//On tick, vanish
	public void updateTick(World w, int x, int y, int z, Random par5Random){
		w.setBlockToAir(x, y, z);
	}
	
	//Always power
    public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5){
        return 15;
    }
    
    //Yes indeed, power other blocks
    public boolean canProvidePower(){
    	return true;
    }
    
    //Vanish if r-clicked
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer player, int idk, float what, float these, float are)  {
		w.setBlockToAir(x,y,z);
		return true;
	}
	
	public void registerIcons(IIconRegister ir){
		this.blockIcon=Blocks.wool.getIcon(0, 14); //Snarf the icon from red wool.
	}

}
