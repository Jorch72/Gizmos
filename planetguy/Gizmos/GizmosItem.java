package planetguy.Gizmos;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

public class GizmosItem extends Item {

	public GizmosItem(int par1) {
		super(par1);
		// TODO Auto-generated constructor stub
	}
	
	public void registerTexture(IconRegister ir){
		System.out.println("Generic item textures loading");
		iconIndex=ir.registerIcon(ConfigHolder.modName+":"+this.getUnlocalizedName());
	}

}
