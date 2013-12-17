package planetguy.portalmod;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@MCVersion(value = "1.6.4")
public class NoCrosslinkCoremod implements IFMLLoadingPlugin {

	public static boolean runtimeDeobfEnabled = true;
	
	@Override
	public String[] getLibraryRequestClass() {
		return null;
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{"planetguy.portalmod.NoCrosslinkASMTransformer"};
	}

	@Override
	public String getModContainerClass() {
		return "planetguy.portalmod.NoCrosslinkModContainer";
	}

	@Override
	public String getSetupClass() {
		return null;
	}

    @Override
    public void injectData(Map<String, Object> data)
    {
            runtimeDeobfEnabled = (Boolean)data.get("runtimeDeobfuscationEnabled");
    }

}
