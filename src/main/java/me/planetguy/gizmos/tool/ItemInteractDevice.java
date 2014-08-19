package me.planetguy.gizmos.tool;

import me.planetguy.gizmos.Gizmos;
import me.planetguy.gizmos.GizmosItem;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;



public abstract class ItemInteractDevice extends GizmosItem{
	
    public ItemInteractDevice() {
		super();
        this.maxStackSize = 1;
        this.setMaxDamage(64);
        this.setCreativeTab(Gizmos.tabGizmos);
	}
    
    public abstract boolean doEffect(int posX, int posY,int posZ, World theWorld, ItemStack me, EntityPlayer thePlayer);
    
    public abstract boolean canDoEffect(int posX, int posY, int posZ, World theWorld, ItemStack me, EntityPlayer thePlayer);

	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if (par7 == 0)
        {
            --par5;
        }

        if (par7 == 1)
        {
            ++par5;
        }

        if (par7 == 2)
        {
            --par6;
        }

        if (par7 == 3)
        {
            ++par6;
        }

        if (par7 == 4)
        {
            --par4;
        }

        if (par7 == 5)
        {
            ++par4;
        }

        if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack))
        {
            return false;
        }
        else
        {
        	if(canDoEffect(par4, par5, par6, par3World, par1ItemStack, par2EntityPlayer)){
        		return doEffect(par4, par5, par6, par3World, par1ItemStack, par2EntityPlayer);
        	}
        }
		return false;
    }
	
	
}


