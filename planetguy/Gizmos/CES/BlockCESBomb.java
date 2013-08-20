package planetguy.Gizmos.CES;

import java.util.Random;

import planetguy.Gizmos.CES.powerups.Powerup;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCESBomb extends BlockContainer{

	public static BlockCESBomb instance;
	
	public BlockCESBomb(int blockID) {
		super(blockID, Material.tnt);
		LanguageRegistry.addName(this, "CES base [TEST]");
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityCESBomb();
	}
	
	public void explode(TileEntityCESBomb teBomb){
		World w=teBomb.worldObj;
		int x=teBomb.xCoord;
		int y=teBomb.yCoord;
		int z=teBomb.zCoord;
		w.spawnEntityInWorld(new EntityCESBombPrimed(w, teBomb, x, y, z));
		w.setBlockTileEntity(x, y, z, null);
		w.setBlock(x, y, z, 0);
	}

	//Pass event to all powerups
	@Override
    public void breakBlock(World w, int x, int y, int z, int idk, int idk2){
    	TileEntityCESBomb teBomb=((TileEntityCESBomb) w.getBlockTileEntity(x,y,z));
    	if(teBomb==null){
    		System.out.println("TE is null when bomb destroyed!");
    		super.breakBlock(w, x, y, z, idk, idk2);
    		return;
    	}
    	for(Powerup p: teBomb.getInstalledPowerups()){
    		if(p!=null){
    			p.onBlockDestroyed(w,x,y,z,teBomb);
    		}
    	}
    	super.breakBlock(w, x, y, z, idk, idk2);
    }
	
	@Override
    public void onNeighborBlockChange(World w, int x, int y, int z, int neighbor){
		TileEntityCESBomb bomb=((TileEntityCESBomb)(w.getBlockTileEntity(x, y, z)));
		for(Powerup p:bomb.getInstalledPowerups()){
			p.onNeighborBlockChange(w,  x,  y,  z, neighbor, bomb);
		}
		w.scheduleBlockUpdate(x, y, z, this.blockID, 2);
	}
	
    public void updateTick(World w, int x, int y, int z, Random rand) {
		TileEntityCESBomb bomb=((TileEntityCESBomb)(w.getBlockTileEntity(x, y, z)));
		for(Powerup p:bomb.getInstalledPowerups()){
			p.onBlockUpdate(w,  x,  y,  z, rand, bomb);
		}
    }

	@Override
	public void onBlockAdded(World w, int x, int y, int z){
		w.scheduleBlockUpdate(x, y, z, this.blockID, 2);
	}
    
	@Override
    public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9){
		ItemStack stk=player.getHeldItem();
		TileEntityCESBomb bomb=((TileEntityCESBomb) w.getBlockTileEntity(x, y, z));
		if(stk==null||stk.itemID!=345){
			onBlockActivatedPassEvent(bomb,side,stk);
			return true;
		}else{
			for(Powerup p : bomb.getInstalledPowerups()){
				System.out.println(p.getName());
			}
		}
		bomb.tryAddPowerup(Powerup.powerups[1]);
		bomb.tryAddPowerup(Powerup.powerups[2]);
		return true;
	}
	
	private void onBlockActivatedPassEvent(TileEntityCESBomb bomb, int side,ItemStack stk){
		for(Powerup p : bomb.getInstalledPowerups()){
			if(p!=null)p.onRightClick(bomb, side, stk);
		}
	}
}
