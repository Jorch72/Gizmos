package planetguy.Gizmos;
 import java.util.logging.Level;

import planetguy.Gizmos.gravitybomb.BlockGraviBomb;
import planetguy.Gizmos.gravitybomb.EntityGravityBomb;
import planetguy.Gizmos.gravitybomb.EntityTunnelBomb;
import planetguy.Gizmos.gravitybomb.ItemGraviBombs;
import planetguy.Gizmos.invUtils.BlockInvenswapperBase;
import planetguy.Gizmos.invUtils.BlockInvenswapperTop;
import planetguy.Gizmos.invUtils.TileEntityInvenswapper;
import planetguy.Gizmos.mobcollider.BlockAccelerator;
import planetguy.Gizmos.mobcollider.BlockLauncher;
import planetguy.Gizmos.mobcollider.ColliderRecipe;
import planetguy.Gizmos.spy.BlockInserter;
import planetguy.Gizmos.spy.EventWatcherSpyItemUse;
import planetguy.Gizmos.spy.ItemLens;
import planetguy.Gizmos.timebomb.BlockTimeBomb;
import planetguy.Gizmos.timebomb.ItemBombDefuser;
import planetguy.Gizmos.timebomb.ItemTimeBomb;
import planetguy.Gizmos.tool.BlockForestFire;
import planetguy.Gizmos.tool.BlockSuperFire;
import planetguy.Gizmos.tool.ItemBuildTool;
import planetguy.Gizmos.tool.ItemBlockTicker;
import planetguy.Gizmos.tool.ItemDeforester;
import planetguy.Gizmos.tool.ItemMinersLighter;
import planetguy.Gizmos.unused.BlockColliderCore;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
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
import net.minecraft.entity.passive.EntityCow;
 import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.SidedProxy;
 

@Mod(modid="planetguy_Gizmos", name="Gizmos", version="0.6")
@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class ContentLoader{
	
	//@SidedProxy(clientSide="planetguy.Gizmos.ClientProxy", serverSide="planetguy.Gizmos.CommonProxy")
	//public static CommonProxy proxy;
		   
	public static Block graviBomb;
	public static Entity graviBombPrimed;
	public static EntityTunnelBomb tunnelBombPrimed;
	
	public static Item dislocator;

	public static Block geoFire,forestFire;
	public static Item deforestator;
	public static Item mlighter;
	
	public static Block spyDesk;
	public static Item spyLens;
	
	public static ItemStack IStimeBomb;

	public static Block invenswapperBase,invenswapperTop;
	
	public static Block particleAccelerator;
	public static Block colliderCore;
	public static Block launcher;
	
	public static Block timeBomb;
	public static Item defuser;
	public static Item buildTool;
	
	public static boolean allowGravityBombs, allowFire, allowDislocator,allowInvenswappers,
		allowBombItems, allowAccelerator,allowTimeBomb,allowForkBomb,allowBuildTool;
	

		
	@Instance("planetguy_Gizmos")
	public static ContentLoader instance;
	
	@PreInit
	public static void loadConfig(FMLPreInitializationEvent event) throws Exception{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		try{

			ConfigHolder.gravityExplosivesID = config.getBlock("Explosives ID", 3981).getInt();
			ConfigHolder.geoFireID = config.getBlock("Superfire ID", 3982).getInt();
			ConfigHolder.spyLabID = config.getBlock("Spy lab ID", 3983).getInt();
			ConfigHolder.accelID = config.getBlock("Accelerator ID", 3984).getInt();
			ConfigHolder.forestFireID = config.getBlock("Forest fire ID", 3985).getInt();
			//ConfigHolder.colliderID = config.getBlock("Collider ID", 3985).getInt(); Probably will never be implemented
			ConfigHolder.launcherID = config.getBlock("Launcher ID", 3986).getInt();
			ConfigHolder.timeExplosivesID = config.getBlock("Time bomb ID", 3987).getInt();
			ConfigHolder.invenswapperTopID = config.getBlock("Invenswapper ID", 3988).getInt();
			ConfigHolder.invenswapperBottomID = config.getBlock("Invenswapper base ID", 3989).getInt();
			
			
			ConfigHolder.netherLighterID = config.getItem("Deforestator ID", 8100).getInt();
			ConfigHolder.minerLighterID = config.getItem("Mineral igniter ID", 8101).getInt();
			ConfigHolder.WandID = config.getItem("Temporal Dislocator ID", 8102).getInt();
			ConfigHolder.lensID = config.getItem("Spy lens ID", 8103).getInt();
			ConfigHolder.defuserID=config.getItem("Defuser ID", 8104).getInt();
			ConfigHolder.buildToolID=config.getItem("Build tool ID", 8105).getInt();
			
			allowGravityBombs=config.get("Nerfs and bans", "Allow gravity explosives", true).getBoolean(true);
			allowFire=config.get("Nerfs and bans", "Allow extra fire", true).getBoolean(true);
			allowBombItems=config.get("Nerfs and bans", "Allow spy desk module",true).getBoolean(true);
			allowDislocator=config.get("Nerfs and bans", "Temporal Dislocator allowed", true).getBoolean(true);
			allowAccelerator=config.get("Nerfs and bans", "Allow accelerator block", true).getBoolean(true);
			allowTimeBomb=config.get("Nerfs and bans", "Allow time bomb and fork bomb", true).getBoolean(true);
			allowInvenswappers=config.get("Nerfs and bans", "Allow invenswappers", true).getBoolean(true);


			ConfigHolder.allowFB=config.get("Nerfs and bans", "Allow fork bombs to fork", true).getBoolean(true);
			ConfigHolder.accelRate = (float) config.get("Nerfs and bans", "Accelerator rate", 1.16158634964).getDouble(1.16158634964);
			ConfigHolder.serverSafeMode = config.get("Nerfs and bans", "Safe server mode",false).getBoolean(false);
			ConfigHolder.nerfHiding = config.get("Nerfs and bans", "Limit stack size to hide",false).getBoolean(false);
			ConfigHolder.launcherPower=config.get("Nerfs and bans", "Mob launcher power", 10D).getDouble(10D);
			int[] dangerous={46,ConfigHolder.gravityExplosivesID,ConfigHolder.timeExplosivesID};
			ConfigHolder.defuseableIDs=config.get("Nerfs and bans", "IDs of defuseable", dangerous).getIntList();
			ConfigHolder.timeExplosivesFuse=config.get("Nerfs and bans", "Time bomb fuse, seconds", 60).getInt(60);


			//ConfigHolder.modName=config.get("Nerfs and bans", "Mod zip file name", "Gizmos_v0.4").getString();
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
		ItemStack itemStackPick = new ItemStack(Item.pickaxeIron);
		ItemStack itemStackFlintAndSteel= new ItemStack(Item.flintAndSteel);
		ItemStack redstone = new ItemStack(Item.redstone);
		ItemStack stackClock=new ItemStack(Item.pocketSundial);
		ItemStack sapling=new ItemStack(Block.sapling);
		ItemStack gravel=new ItemStack(Block.gravel);
		ItemStack glass=new ItemStack(Block.glass);
		ItemStack wood=new ItemStack(Block.planks);
		ItemStack blockIron=new ItemStack(Block.blocksList[42]);
		ItemStack crafter=new ItemStack(Block.workbench);
		ItemStack chest=new ItemStack(Block.chest);
		ItemStack endStone=new ItemStack(Block.whiteStone);
		ItemStack shears=new ItemStack(Item.shears);
		ItemStack stick=new ItemStack(Item.stick);

        NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
		
		//First comes common crafting...
		if(allowBombItems||allowTimeBomb){
			spyLens=new ItemLens(ConfigHolder.lensID).setCreativeTab(CreativeTabs.tabMaterials);
			ItemStack lens=new ItemStack(spyLens);
	        LanguageRegistry.instance().addName(spyLens, "Spy lens");
			GameRegistry.addRecipe(lens, new Object[] { " i ", "igi", " i ", 
					Character.valueOf('g'), glass,
					Character.valueOf('i'), iron });
			
			//Might need lens for bomb items...
			if(allowBombItems){
				//this.bomb=new EnchantmentBomb(136);
				spyDesk=new BlockInserter(ConfigHolder.spyLabID,6).setUnlocalizedName("spyLab");
				buildTool=new ItemBuildTool(ConfigHolder.buildToolID).setUnlocalizedName("buildTool").setCreativeTab(CreativeTabs.tabTools);
				GameRegistry.registerBlock(spyDesk, ItemBlock.class, "spyLab");
				
		        LanguageRegistry.instance().addName(spyDesk, "Inserter");
		        LanguageRegistry.instance().addName(buildTool, "Builder's Tool");
				
				MinecraftForge.EVENT_BUS.register(new EventWatcherSpyItemUse());

				ItemStack itemSpyDesk=new ItemStack(spyDesk);
		        GameRegistry.addRecipe(itemSpyDesk, new Object[] {"LWC", "III","B B",
		        		Character.valueOf('L'),lens,
		        		Character.valueOf('W'),crafter,
		        		Character.valueOf('C'),chest,
		        		Character.valueOf('I'),blockIron,
		        		Character.valueOf('B'),wood});
		        
		        ItemStack itStkBuildTool=new ItemStack(buildTool);
		        ItemStack iSDPcx=new ItemStack(Item.pickaxeDiamond);
		        ItemStack iSPist=new ItemStack(Block.pistonBase);
		        
		        //IT LIVES!! No more duping, either!
		        GameRegistry.addRecipe(itStkBuildTool, new Object[]{"  c"," p ","d  ", 
		        		Character.valueOf('c'),chest,
		        		Character.valueOf('p'),iSPist,
		        		Character.valueOf('d'),iSDPcx});
		        
		        
			}
			
			//Or time bombs (for making the defuser)
			if(allowTimeBomb){
				timeBomb=new BlockTimeBomb(ConfigHolder.timeExplosivesID);
				GameRegistry.registerBlock(timeBomb,ItemTimeBomb.class,"timeBombs");
				Item.itemsList[ ConfigHolder.timeExplosivesID] = new ItemTimeBomb( ConfigHolder.timeExplosivesID-256).setItemName("timeBombs");
				defuser=new ItemBombDefuser(ConfigHolder.defuserID).setMaxDamage(10).setCreativeTab(CreativeTabs.tabTools).setUnlocalizedName("defuser");
				
				LanguageRegistry.addName(defuser, "Bomb defuser");
				final String[] oreNames = {"Time bomb", "Fork bomb", "?", "?", "?", "?", "?", "?", "?", "?", "?", "?", "?", "?", "Fork bomb", "Time bomb"};

				for (int re = 0; re < 16; re++){
					ItemStack oreStack = new ItemStack(timeBomb, 1, re);
					LanguageRegistry.addName(oreStack, oreNames[re]);
				}	
				//A fat lot of good THAT did... Still need a way to tell time bombs from fork bombs.
				
				//LET'S CRAFT!!!
				ItemStack itemStackTB=new ItemStack(timeBomb,1,0); 
				ItemStack itemStackFB=new ItemStack(timeBomb,1,1);
				ItemStack ISDefuser=new ItemStack(defuser);
				
				GameRegistry.addRecipe(ISDefuser, new Object[]{
						" sl",
						" ks",
						"k  ",
						Character.valueOf('s'),shears,
						Character.valueOf('k'),stick,
						Character.valueOf('l'),lens});
				
				GameRegistry.addShapelessRecipe(itemStackTB, Block.tnt, Item.pocketSundial);
				
				GameRegistry.addRecipe(itemStackFB, new Object[]{
						"EEE","ETE","EEE", 
						Character.valueOf('T'),itemStackTB,
						Character.valueOf('E'),endStone});
				

			}
			
		}
		
		if(allowInvenswappers){
			invenswapperTop=new BlockInvenswapperTop(ConfigHolder.invenswapperTopID);
			invenswapperBase=new BlockInvenswapperBase(ConfigHolder.invenswapperBottomID).setCreativeTab(CreativeTabs.tabDecorations);
			GameRegistry.registerTileEntity(TileEntityInvenswapper.class, "Gizmos.invenswapper");
			GameRegistry.registerBlock(invenswapperTop, ItemBlock.class,"invenswapperTop");
			GameRegistry.registerBlock(invenswapperBase, ItemBlockWithMetadata.class,"invenswapperBase");
			LanguageRegistry.addName(invenswapperBase, "Invenswapper base");


		}
		
		//Fire module
		if(allowFire&&!(ConfigHolder.serverSafeMode)){
			deforestator = new ItemDeforester(ConfigHolder.netherLighterID).setUnlocalizedName("netherLighter");
			mlighter = new ItemMinersLighter(ConfigHolder.minerLighterID).setUnlocalizedName("minersLighter");
			geoFire = new BlockSuperFire(ConfigHolder.geoFireID, 31).setUnlocalizedName("doomFire").setHardness(0.0F).setLightValue(1.0F);
			forestFire = new BlockForestFire(ConfigHolder.forestFireID, 31).setUnlocalizedName("woodFire").setHardness(0.0F).setLightValue(1.0F);

			GameRegistry.registerBlock(geoFire, ItemBlock.class, "doomFire");
			
			ItemStack itemStackNetherLighter = new ItemStack(deforestator,1,0);
			GameRegistry.addRecipe(itemStackNetherLighter, new Object[]{ "brb", "rfr", "brb",
					'b',powder,
					'r',sapling,
					'f',itemStackFlintAndSteel});
			ItemStack itemStackMinerLighter = new ItemStack(mlighter,1,0);
			GameRegistry.addRecipe(itemStackMinerLighter, new Object[]{ "brb", "rfr", "brb",
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
			graviBomb = new BlockGraviBomb( ConfigHolder.gravityExplosivesID).setUnlocalizedName("graviBomb").setHardness(0.0F).setResistance(0.0F);
			Item.itemsList[ ConfigHolder.gravityExplosivesID] = new ItemGraviBombs( ConfigHolder.gravityExplosivesID-256).setItemName("graviBomb");
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
			final String[] oreNames = {"Gravity bomb", "Tunnel bomb", "?", "?", "?", "?", "?", "?", "?", "?", "?", "?", "?", "?", "Tunnel Bomb", "Gravity Bomb"};

			for (int re = 0; re < 16; re++){
				ItemStack oreStack = new ItemStack(graviBomb, 1, re);
				LanguageRegistry.addName(oreStack, oreNames[oreStack.getItemDamage()]);
			}
		}
		
		//Spy module

		
		if(allowAccelerator){
			
			particleAccelerator=new BlockAccelerator(ConfigHolder.accelID).setUnlocalizedName("accelerator").setCreativeTab(CreativeTabs.tabRedstone);
			launcher=new BlockLauncher(ConfigHolder.launcherID).setUnlocalizedName("entityLauncher");
			GameRegistry.registerBlock(launcher, ItemBlock.class, "launcher");

			
			//BlockColliderCore core=new BlockColliderCore(ConfigHolder.colliderID);
			//colliderCore=(Block) core.setUnlocalizedName("colliderCore");
			
			GameRegistry.registerBlock(particleAccelerator, ItemBlock.class, "accelerator");
			//GameRegistry.registerBlock(colliderCore, ItemBlock.class, "colliderCore");
			
			ItemStack[] stacks={new ItemStack(334,64,0)};
			//ColliderRecipe cowCowHighSpeed=new ColliderRecipe(stacks, 1.0D, EntityCow.class, EntityCow.class);
			//core.addColliderRecipe(cowCowHighSpeed);

			LanguageRegistry.instance().addName(launcher, "Launcher");
			LanguageRegistry.instance().addName(particleAccelerator, "Accelerator");
			//LanguageRegistry.instance().addName(colliderCore, "Collider core");
		}

		

	    //EntityRegistry.registerModEntity(EntityGravityBomb.class, "Lit Gravity Bomb", 222, planetguy.EvilToys.ContentLoader, 0, 0, false);
   }
	
	@PostInit
	public final void loadAfterEverything(FMLPostInitializationEvent foo){
		//System.out.println("PostInit called");
		//SpyReflector.doStuff();
	}
 }
 
