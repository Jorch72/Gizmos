package planetguy.gizmos.tool;

import java.util.Random;

import planetguy.gizmos.Gizmos;
import planetguy.gizmos.GizmosItem;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

public class ItemBlockTicker extends GizmosItem{

	private final int TICKS_PER_MINUTE=20*60;
	private Random r=new Random();
	
	public ItemBlockTicker(int par1) {
		super(par1);
        this.maxStackSize = 1;
        this.setMaxDamage(64);
        this.setCreativeTab(CreativeTabs.tabTools);
        //MinecraftForgeClient.preloadTexture("/mods/Gizmos/textures/items/dislocator.png");
	}
	/*
	public String getTextureFile(){
		return "/planetguy/Gizmos/tex.png";
	}
	*/
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{
		try{
			Block b=Block.blocksList[par3World.getBlockId(par4, par5, par6)];

			TileEntity t=par3World.getBlockTileEntity(par4, par5, par6);
			for(int i=0; i<TICKS_PER_MINUTE; i++){
				t.updateEntity();
			}
			par1ItemStack.damageItem(1, par2EntityPlayer);

			for(int i=0; i<TICKS_PER_MINUTE; i++){
				b.updateTick(par3World, par4, par5, par6, r);
			}

		}catch(NullPointerException ignore){
		
		}
		return false;
		
	}
	
	public void registerIcons(IconRegister ir){
		itemIcon=ir.registerIcon(Gizmos.modName+":"+"dislocator");
	}
	
}