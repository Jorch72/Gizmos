package planetguy.Gizmos;
 import java.util.logging.Level;

import planetguy.Gizmos.gravitybomb.BlockGraviBomb;
import planetguy.Gizmos.gravitybomb.EntityGravityBomb;
import planetguy.Gizmos.gravitybomb.EntityTunnelBomb;
import planetguy.Gizmos.gravitybomb.ItemGraviBombs;
import planetguy.Gizmos.spy.BlockSpyLab;
import planetguy.Gizmos.spy.EventWatcherBombUse;
import planetguy.Gizmos.spy.GuiHandler;
import planetguy.Gizmos.spy.ItemLens;
import planetguy.Gizmos.tool.BlockSuperFire;
import planetguy.Gizmos.tool.ItemBlockTicker;
import planetguy.Gizmos.tool.ItemDeforester;
import planetguy.Gizmos.tool.ItemMinersLighter;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
 import cpw.mods.fml.common.registry.LanguageRegistry;
 import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
 import net.minecraft.entity.Entity;
 import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.SidedProxy;
 

@Mod(modid="planetguy_Gizmos", name="Gizmos", version="1.0")
@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class ContentLoader{
	
	//@SidedProxy(clientSide="planetguy.Gizmos.ClientProxy", serverSide="planetguy.Gizmos.CommonProxy")
	//private CommonProxy proxy;
		   
	public static Block graviBomb;
	public static Block spyDesk;
	public static Entity graviBombPrimed;
	public static EntityTunnelBomb tunnelBombPrimed;
	public static Block doomFire;
	public static Item deforestator;
	public static Item mlighter;
	public static Item dislocator;
	public static Item spyLens;
	public static Enchantment bomb;
	public static boolean allowGravityBombs, allowFire, allowDislocator, allowBombItems;
	

		
	@Instance("planetguy_Gizmos")
	public static ContentLoader cl;
	
	@PreInit
	public static void loadConfig(FMLPreInitializationEvent event) throws Exception{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		try{
			allowGravityBombs=config.get("general", "Explosives allowed", true).getBoolean(true);
			allowFire=config.get("general", "Extra fire allowed", true).getBoolean(true);
			allowBombItems=config.get("general", "Spy bombs allowed",true).getBoolean(true);
			allowDislocator=config.get("general", "Temporal Dislocator allowed", true).getBoolean(true);
			ConfigHolder.explosivesID = config.getBlock("Explosives ID", 3981).getInt();
			ConfigHolder.doomFireID = config.getBlock("Superfire ID", 3982).getInt();
			ConfigHolder.spyLabID = config.getBlock("Spy lab ID", 3983).getInt();
			ConfigHolder.netherLighterID = config.getItem("Deforestator ID", 8100).getInt();
			ConfigHolder.minerLighterID = config.getItem("Mineral igniter ID", 8101).getInt();
			ConfigHolder.WandID = config.getItem("Temporal Dislocator ID", 8102).getInt();
			ConfigHolder.lensID = config.getItem("Spy lens ID", 8102).getInt();
			ConfigHolder.serverSafeMode = config.get("general", "Safe server mode",false).getBoolean(false);
			ConfigHolder.nerfHiding = config.get("general", "Limit stack size to hide",false).getBoolean(false);
			//ConfigHolder.modName=config.get("general", "Mod zip file name", "Gizmos_v0.4").getString();
		}catch (Exception e){
			FMLLog.log(Level.SEVERE,e,"BAD GIZMOS CONFIG IS BAD! Try deleting it.");
			throw e;
		}
		config.save();
	}

	@Init
	public final void load(FMLInitializationEvent ignored){
		//proxy.registerRenderers();
		
		
		//Get our Vanilla itemstacks ready for crafting
		ItemStack tnt = new ItemStack(Block.tnt);
		ItemStack powder = new ItemStack(Item.blazePowder);
		ItemStack iron = new ItemStack(Item.ingotIron);
		ItemStack itemStackPick = new ItemStack(Item.pickaxeSteel);
		ItemStack itemStackFlintAndSteel= new ItemStack(Item.flintAndSteel);
		ItemStack redstone = new ItemStack(Item.redstone);
		ItemStack stackClock=new ItemStack(Item.pocketSundial);
		ItemStack sapling=new ItemStack(Block.sapling);
		ItemStack gravel=new ItemStack(Block.gravel);
		ItemStack glass=new ItemStack(Block.glass);
		ItemStack wood=new ItemStack(Block.planks);
		ItemStack blockIron=new ItemStack(Block.blockSteel);
		ItemStack crafter=new ItemStack(Block.workbench);
		ItemStack chest=new ItemStack(Block.chest);

		//Fire module
		if(allowFire&&!(ConfigHolder.serverSafeMode)){
			deforestator = new ItemDeforester(ConfigHolder.netherLighterID).setUnlocalizedName("netherLighter");
			mlighter = new ItemMinersLighter(ConfigHolder.minerLighterID).setUnlocalizedName("minersLighter");
			doomFire = new BlockSuperFire(ConfigHolder.doomFireID, 31).setUnlocalizedName("doomFire").setHardness(0.0F).setLightValue(1.0F);
			GameRegistry.registerBlock(doomFire);
			ItemStack itemStackNetherLighter = new ItemStack(deforestator,1,0);
			GameRegistry.addRecipe(itemStackNetherLighter, new Object[]{ "brb", "rfr", "brb",
					'b',powder,
					'r',sapling,
					'f',itemStackFlintAndSteel});
			ItemStack itemStackMinerLighter = new ItemStack(mlighter,1,0);
			GameRegistry.addRecipe(itemStackNetherLighter, new Object[]{ "brb", "rfr", "brb",
					'b',powder,
					'r',gravel,
					'f',itemStackFlintAndSteel});
			LanguageRegistry.addName(deforestator, "Deforestator");
			LanguageRegistry.addName(mlighter, "Miner's lighter");
		}
		
		//Temporal dislocator module
		if(allowDislocator){
			dislocator = new ItemBlockTicker(ConfigHolder.WandID).setUnlocalizedName("dislocator");
			ItemStack stackTicker=new ItemStack(dislocator,1,0); 
			GameRegistry.addRecipe(stackTicker, new Object[] {"ccc", "cic", "ccc",
					Character.valueOf('c'),stackClock,
					Character.valueOf('i'),iron});
			LanguageRegistry.addName(dislocator, "§5Temporal Dislocator");
		}
		
		//Explosives module
		if(allowGravityBombs&&!(ConfigHolder.serverSafeMode)){
			graviBomb = new BlockGraviBomb( ConfigHolder.explosivesID).setUnlocalizedName("graviBomb").setHardness(0.0F).setResistance(0.0F);
			Item.itemsList[ ConfigHolder.explosivesID] = new ItemGraviBombs( ConfigHolder.explosivesID-256).setItemName("graviBomb");
			graviBombPrimed = new EntityGravityBomb(null);
			tunnelBombPrimed=new EntityTunnelBomb(null);
			//EntityRegistry.registerModEntity(EntityTunnelBeam.class, "Tunnel Beam", 199, this, 80, 3, true);
			EntityRegistry.registerModEntity(EntityGravityBomb.class, "GBomb", 201, this, 80, 3, true);
			EntityRegistry.registerModEntity(EntityTunnelBomb.class, "TBomb", 202, this, 80, 3, true);
			ItemStack itemStackGB = new ItemStack(graviBomb, 3, 0);
			ItemStack itemStackExcaBomb = new ItemStack(graviBomb, 1, 1);
			GameRegistry.addRecipe(itemStackGB, new Object[] { "xxx", "iyi", " i ", 
					Character.valueOf('x'),tnt, 
					Character.valueOf('y'), powder,
					Character.valueOf('i'), iron });
			GameRegistry.addRecipe(itemStackExcaBomb, new Object[] { " b ", "ibi", "pbp", 
					Character.valueOf('b'), itemStackGB, 
					Character.valueOf('i'), iron, 
					Character.valueOf('p'), itemStackPick });
			LanguageRegistry.instance().addStringLocalization("tile.graviBomb.gravityBomb.name", "Gravity Bomb");
			LanguageRegistry.instance().addStringLocalization("tile.graviBomb.tunnelBomb.name", "Excavator Bomb");	
		}
		
		//Spy module
		if(allowBombItems){
			//this.bomb=new EnchantmentBomb(136);
			spyDesk=new BlockSpyLab(ConfigHolder.spyLabID,6).setUnlocalizedName("spyLab");
			GameRegistry.registerBlock(spyDesk, ItemBlock.class, "spyLab");
			spyLens=new ItemLens(ConfigHolder.lensID).setCreativeTab(CreativeTabs.tabMaterials);
			MinecraftForge.EVENT_BUS.register(new EventWatcherBombUse());
	        NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
			ItemStack lens=new ItemStack(spyLens);
			ItemStack itemSpyDesk=new ItemStack(spyDesk);
			GameRegistry.addRecipe(lens, new Object[] { " i ", "igi", " i ", 
					Character.valueOf('g'), glass,
					Character.valueOf('i'), iron });
	        LanguageRegistry.instance().addName(spyDesk, "Spy lab");
	        LanguageRegistry.instance().addName(spyLens, "Spy lens");
	        GameRegistry.addRecipe(itemSpyDesk, "LWC", "III","B B",
	        		Character.valueOf('L'),lens,
	        		Character.valueOf('W'),crafter,
	        		Character.valueOf('C'),chest,
	        		Character.valueOf('I'),blockIron,
	        		Character.valueOf('B'),wood);
		}

	    //EntityRegistry.registerModEntity(EntityGravityBomb.class, "Lit Gravity Bomb", 222, planetguy.EvilToys.ContentLoader, 0, 0, false);

   }
 }
