package me.planetguy.gizmos.tool;

import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import me.planetguy.core.sl.SLLoad;
import me.planetguy.gizmos.Gizmos;
import me.planetguy.gizmos.GizmosItem;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.MinecraftForgeClient;

@SLLoad(name="temporalDislocator")
public class ItemBlockTicker extends GizmosItem{

	private final int TICKS_PER_MINUTE=20*60;
	private Random r=new Random();
	
	@SLLoad
	public ItemBlockTicker() {
        this.maxStackSize = 1;
        this.setMaxDamage(64);
    	ItemStack stackClock=new ItemStack(Items.clock);
    	ItemStack iron = new ItemStack(Items.iron_ingot);
    	ItemStack stackTicker=new ItemStack(this,1,0);
    	GameRegistry.addRecipe(stackTicker, new Object[] {"ccc", "cic", "ccc",
    	Character.valueOf('c'),stackClock,
    	Character.valueOf('i'),iron});
    	LanguageRegistry.addName(this, "§5Temporal Dislocator");	
	}

	//Ticks tile entity as many times as it would normally tick in a whole minute.
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World w, int par4, int par5, int par6, int par7, float par8, float par9, float par10){
		try{
			TileEntity t=w.getTileEntity(par4, par5, par6);
			if(t!=null){ //if it has a tile entity tick it
				for(int i=0; i<TICKS_PER_MINUTE; i++){
					t.updateEntity();
				}
			}else{//otherwise tick the block
				Block b=w.getBlock(par4, par5, par6);
				for(int i=0; i<TICKS_PER_MINUTE; i++){
					b.updateTick(w, par4, par5, par6, r);
				}
			}
			par1ItemStack.damageItem(1, par2EntityPlayer);

		}catch(NullPointerException ignore){
		
		}
		return false;
		
	}
	
	@Override
	public void registerIcons(IIconRegister ir){
		itemIcon=ir.registerIcon(Gizmos.modName+":"+"dislocator");
	}
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List tooltipLines, boolean advancedTooltipsActive){
        if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
        	tooltipLines.add("Hold <shift> for more");
        	return;
        }
		tooltipLines.add("Dislocates a block's temporal position");
		tooltipLines.add("from the future to the present.");
	}
	
}
