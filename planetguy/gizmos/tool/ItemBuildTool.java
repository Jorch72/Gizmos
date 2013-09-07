package planetguy.gizmos.tool;

import cpw.mods.fml.common.registry.LanguageRegistry;
import planetguy.gizmos.Gizmos;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemBuildTool extends ItemPickaxe{

	private final int myID;

	public ItemBuildTool(int par1) {
		super(par1, EnumToolMaterial.EMERALD);
		myID=par1;
		LanguageRegistry.instance().addStringLocalization("item.buildTool.name", "Build tool");
	}

	public void registerIcons(IconRegister ir){
		itemIcon = ir.registerIcon(Gizmos.modName+":buildTool");
	}
	
	@Override
	public String getUnlocalizedName(){
		return "buildTool";
	}

	public boolean onItemUse(ItemStack me, EntityPlayer player, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10){
		try{
			NBTTagCompound tag=me.getTagCompound();
			ItemStack a=ItemStack.loadItemStackFromNBT(tag);
			me.setTagCompound(null);

			a.tryPlaceItemIntoWorld(player, par3World, par4, par5, par6, par7, par8, par9, par10);

			if(a.stackSize==0||(a.getItemDamage()==a.getMaxDamage()&&a.getItem().getItemStackLimit()==1)){
				ItemStack[] stacks=player.inventory.mainInventory;
				boolean hasFoundReplace=false;
				for(int i=0; i<stacks.length; i++){
					if((a.itemID==stacks[i].itemID&&a.getItemDamage()==stacks[i].getItemDamage())){
						a=stacks[i];
						stacks[i]=null;
						hasFoundReplace=true;
						break;
					}
				}
				if(!hasFoundReplace)a=null;
			}
			
			NBTTagCompound oldTag=new NBTTagCompound("tag");
			a.writeToNBT(oldTag);
			me.setTagCompound(oldTag);
			
		}catch(Exception e){
			//e.printStackTrace(); //Causes alarm if there is no item inside
		}
		return true;
	}
	/**
	 * 
	 * @param inside The stack to hide
	 * @param outside the stack to hide it in
	 */
	private void writeObjectToNbt(ItemStack inside, ItemStack outside){
		try{
			NBTTagCompound futureTag=new NBTTagCompound("Planetguy-spy");
			if(inside!=null){
				inside.writeToNBT(futureTag);
				NBTTagCompound oldTag=outside.getTagCompound();
				NBTTagCompound combinedTag=(NBTTagCompound) NBTTagCompound.newTag((byte) 10,"");
				combinedTag.setCompoundTag("spydata", futureTag);
				outside.setTagCompound(futureTag);
			}else{
				outside.setTagCompound(null);
			}
		}catch(NullPointerException npe){

		}
	}

}
