package me.planetguy.gizmos;

import me.planetguy.gizmos.content.flashlight.ItemFlashlightBase;
import net.minecraftforge.common.config.Configuration;

public class Properties {
	
	/*
	 * Compile-time properties...
	 */
	public static final String modID= "planetguy_Gizmos";
	
	public static final String version="3.3";
	
	/*
	 * Class-name properties, for annotations that require strings.
	 */
	
	public static final String clientProxy="me.planetguy.gizmos.ClientProxy";

	public static final String commonProxy="me.planetguy.gizmos.CommonProxy";
	
	/*
	 * Run-time properties
	 */
	
	public static Configuration configFile;

	public static boolean enableMinecartTweaks=true;
	
	public static boolean enableSpecialPortals=true;
	
	public static int maxPortalSize=100;

	public static int timeBombFuse=400;

	public static int telekinesisRange=10;

	public static String[] whitelist;

	public static boolean noStackingHiddenItems;

	public static int flashlightRechargePerGlowstone=25;

	public static int rfPerFlashlightRecharge=1000;
	
	public static void update() {
		enableMinecartTweaks=configFile.getBoolean("enablePullingOnFurnaceCart", Configuration.CATEGORY_GENERAL, enableMinecartTweaks, "Should sneaking while right-clicking a minecart start it moving towards you?");
		enableSpecialPortals=configFile.getBoolean("enableSpecialPortals", Configuration.CATEGORY_GENERAL, enableSpecialPortals, "Should free-form portals be allowed? (Note: Slightly buggy)");
		maxPortalSize=configFile.getInt("maxPortalSize", Configuration.CATEGORY_GENERAL, maxPortalSize, 0, Integer.MAX_VALUE, "Maximum size of freeform portals");
		flashlightRechargePerGlowstone=configFile.getInt("flashlightRechargePerGlowstone", Configuration.CATEGORY_GENERAL, flashlightRechargePerGlowstone, 0, ItemFlashlightBase.maxDamage, "How much use should glowstone repair on a flashlight?");
		if(configFile.hasChanged()){
			configFile.save();
		}
	}

}
