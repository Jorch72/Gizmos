package planetguy.Gizmos.spy;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;

public class ItemSpyDisguise extends ItemArmor{

	public ItemSpyDisguise(int par1, int par3, int par4) {
		super(par1, null, par3, par4);
		// TODO Auto-generated constructor stub
	}
	
	public void registerTexture(IconRegister ir){
		System.out.println("Spy disguise textures loading");
		iconIndex=ir.registerIcon("Gizmos"+":"+"spyLens");
	}

}
