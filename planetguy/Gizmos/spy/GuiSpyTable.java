package planetguy.Gizmos.spy;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiSpyTable extends GuiContainer {
	
	private IInventory home;
	

	public GuiSpyTable (InventoryPlayer inventoryPlayer, IInventory inv) {
		//the container is instanciated and passed to the superclass for handling
		super(new ContainerSpyLab(inventoryPlayer, inv));
		home=inv;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		//draw text and stuff here
		//the parameters for drawString are: string, x, y, color
		fontRenderer.drawString("Spy Lab", 8, 6, 4210752);
		//draws "Inventory" or your regional equivalent
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		//draw your Gui here, only thing you need to change is the path
		//int texture = mc.renderEngine.getTexture();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		System.out.println("Loading spy table GUI");
		this.mc.renderEngine.bindTexture("/planetguy/Gizmos/spy/spyLab.png");// /planetguy/Gizmos/spy/spyLab.png
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

}