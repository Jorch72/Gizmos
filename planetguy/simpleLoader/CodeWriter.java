package planetguy.simpleLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import planetguy.gizmos.Gizmos;

import cpw.mods.fml.common.Mod;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;

/**
 * Generates reflection-free loader source code for SimpleLoader mods.
 * @author planetguy
 *
 */

public class CodeWriter {
	
	final Object modContainer;
	final PrintStream output;
	final String modName;
	List<Class> classes=new ArrayList<Class>();
		
	public CodeWriter(Object o) throws IOException{
		String filename=SimpleLoader.mcdir+"/../repo/Gizmos/planetguy/gizmos/SLGeneratedLoader.java";
		File out=new File(filename);
		output=new PrintStream(out);
		modContainer=o;
		Annotation a=modContainer.getClass().getAnnotation(Mod.class);
		modName=((Mod)a).name();
	}
	
	public void setClasses(Class[] classes){
		for(Class c:classes){
			this.classes.add(c);
		}
	}
	
	public void writeLoaderClass(List<String> classnames) throws IllegalArgumentException, IllegalAccessException{
		System.out.println("Writing loader...");
		System.out.println("[CW] classes:"+classes);
		output.println("package planetguy.gizmos;");
		output.println();
		output.println("import net.minecraftforge.common.Configuration;");
		output.println();
		output.println("import cpw.mods.fml.common.registry.GameRegistry;");
		output.println("import cpw.mods.fml.common.registry.EntityRegistry;");
		output.println();
		output.println("import java.util.Arrays;\nimport java.util.HashMap;\nimport java.util.List;\nimport java.util.ArrayList;");
		output.println("/*This class has been automatically generated by SimpleLoader.");
		output.println("Note that it may have errors that require manual correction.");
		output.println("To help with this, known errors are flagged with \"!!!\".");
		output.println("Also note that the code generated may be difficult to read.");
		output.println("Sorry about that, but that's how the cookie crumbles.*/");
		output.println();
		output.println("public class SLGeneratedLoader{");
		output.println();
		//HashMap to store IDs from loading from config to object construction
		output.println("private static HashMap<String, Integer> idMap=new HashMap<String,Integer>();");
		output.println("private static List<String> moduleList=new ArrayList(Arrays.asList(new String[]{");
		output.println("\""+classnames.get(0)+"\"");
		for(int i=1; i<classnames.size(); i++){//treat 0 as a special case, it needs no comma before
			output.println(",\""+classnames.get(i)+"\"");
		}
		output.println("}));");
		output.println();
		output.println("public static void setupConfigs(Configuration config){");

		//Copied from SimpleLoader. Gets list of modules the user wants to allow.
		for(int i=0; i<classnames.size(); i++){//treat 0 as a special case, it needs no comma before
			output.println("if(!config.get(\"[SL] Item-restrict\",\"allow '"+classnames.get(i)+"'\",true).getBoolean(true))");
			output.println("moduleList.remove(\""+classnames.get(i)+"\");");
		}
		
		//For each class, look for @SLProp fields and get from config.
		for(Class c:classes){
			for(Field f:c.getDeclaredFields()){
				if(f.isAnnotationPresent(SLProp.class)){
					SLProp prop=f.getAnnotation(SLProp.class);
					String category=prop.category();
					String key=prop.name();
					Object defaultVal=f.get(null);
					
					String qName=c.getName()+"."+f.getName();
					
					output.print(qName+"=config.get(\""+prop.category()+"\",\""+prop.name()+"\","+qName+")");
					
					//choose the matching type to get from the config property to put in the field
					if(f.getGenericType().equals(Integer.TYPE)){
						output.print(".getInt("+qName);
					}else if(f.getGenericType().equals(Double.TYPE)){
						output.print(".getDouble("+qName);
					}else if(f.getGenericType().toString()=="class java.lang.String"){
						output.print(".getString("+qName);
					}else if(f.getGenericType().toString()=="class [Ljava.lang.String;"){
						output.print(".getStringList(");
					}else if(f.getGenericType().toString()=="class [I"){
						output.print(".getIntList(");
					}else if(f.getGenericType().toString()=="class [D"){
						output.print(".getDoubleList(");
					}else if(f.getGenericType()==Boolean.TYPE){
						output.print(".getBoolean("+qName); 
					}else{//If unsupported type, leave something noticeable.
						output.print("/*!!!*/.getIntList(");
					}
					output.println(");");
				}
			}
		}
		
		output.println();
		
		int nextBlockID=3980;
		int nextItemID=8100;
		int nextEntityID=201;
		//Add IDs to config
		for(Class c:classes){
			SLLoad sll=((SLLoad)c.getAnnotation(SLLoad.class));
			if(Block.class.isAssignableFrom(c)){
				//Get block ID and put it in idMap
				output.println("idMap.put(\""+sll.name()+".blockID\",config.getBlock(\""+sll.name()+"\","+nextBlockID+").getInt("+nextBlockID+"));");
				++nextBlockID;
			}else if(Item.class.isAssignableFrom(c)){
				output.println("idMap.put(\""+sll.name()+".itemID\",config.getItem(\""+sll.name()+"\","+nextItemID+").getInt("+nextItemID+"));");
				++nextItemID;
			}else if(Entity.class.isAssignableFrom(c)){
				output.println("idMap.put(\""+sll.name()+".entityID\",config.get(\"Entities\",\""+sll.name()+"\","+nextEntityID+").getInt("+nextEntityID+"));");
				++nextEntityID;
			}
			
		}
		output.println("}");
		output.println();
		output.println("public static void loadThings(){");
		for(Class c:classes){
			SLLoad sll=((SLLoad)c.getAnnotation(SLLoad.class));
			output.print("if(moduleList.contains(\""+sll.name()+"\")"); //check if this module is allowed in the config
			for(String dependency:sll.dependencies()){
				output.print("&&moduleList.contains(\""+dependency+"\")"); //check if dependencies are allowed in config
			}
			output.println("){");
			
			String containerField=modContainer.getClass().getName()+"."+sll.name();
			if(Block.class.isAssignableFrom(c)){
				output.println(containerField+"=new "+c.getName()+"(idMap.get(\""+sll.name()+".blockID\"));");
				output.println("GameRegistry.registerBlock("+containerField+","+sll.itemClass()+".class,\""+modName+"."+sll.name()+"\");");
			}
			if(Item.class.isAssignableFrom(c)){
				output.println(containerField+"=new "+c.getName()+"(idMap.get(\""+sll.name()+".itemID\"));");
				output.println("GameRegistry.registerItem("+containerField+",\""+modName+"."+sll.name()+"\");");
			}
			if(Entity.class.isAssignableFrom(c)){
				output.println("EntityRegistry.registerModEntity("+c.getName()+".class,\""+sll.name()+"\",idMap.get(\""+sll.name()+".entityID\"),Gizmos.instance,80,3,true);");
			}
			if(CustomModuleLoader.class.isAssignableFrom(c)){
				output.println(containerField+"=new "+c.getName()+"();");
				output.println(containerField+".load();");
			}
			output.println("}");
		}
		
		output.println("}");
		
		output.println("}");
		
		output.close();
		
	}
}
