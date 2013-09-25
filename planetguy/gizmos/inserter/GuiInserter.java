package planetguy.gizmos.inserter;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import planetguy.gizmos.Gizmos;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



@SideOnly(Side.CLIENT)
public class GuiInserter extends GuiContainer {
	
	private IInventory home;
	private static final ResourceLocation guiLoc=new ResourceLocation(Gizmos.modName+":"+"textures/gui/container/spyLab.png");

	public GuiInserter (InventoryPlayer inventoryPlayer, IInventory inv) {
		//the container is instanciated and passed to the superclass for handling
		super(new ContainerInserter(inventoryPlayer, inv));
		home=inv;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		//draw text and stuff here
		//the parameters for drawString are: string, x, y, color
		fontRenderer.drawString("Inserter", 8, 6, 4210752);
		//draws "Inventory" or your regional equivalent
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		//System.out.println("Loading spy table GUI");
        this.mc.func_110434_K().func_110577_a(guiLoc);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

}