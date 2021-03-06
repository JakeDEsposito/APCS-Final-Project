package com.bluetitansoftware.apcsfp.engine.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.bluetitansoftware.apcsfp.engine.graphics.Shader;
import com.bluetitansoftware.apcsfp.engine.graphics.Texture;
import com.bluetitansoftware.apcsfp.engine.object.component.SpriteSheet;

public class AssetPool
{
	
	private static Map<String, Shader> shaders = new HashMap<>();
	private static Map<String, Texture> textures = new HashMap<>();
	private static Map<String, SpriteSheet> spriteSheets = new HashMap<>();
	
	public static Shader getShader(String resourceName)
	{
		File file = new File(resourceName);
		if(AssetPool.shaders.containsKey(file.getAbsolutePath()))
			return AssetPool.shaders.get(file.getAbsolutePath());
		else
		{
			Shader shader = new Shader(resourceName);
			shader.create();
			AssetPool.shaders.put(file.getAbsolutePath(), shader);
			return shader;
		}
	}
	
	public static Texture getTexture(String resourceName)
	{
		File file = new File(resourceName);
		if(AssetPool.textures.containsKey(file.getAbsolutePath()))
			return AssetPool.textures.get(file.getAbsolutePath());
		else
		{
			Texture texture = new Texture(resourceName);
			AssetPool.textures.put(resourceName, texture);
			return texture;
		}
	}
	
	public static void addSpriteSheet(String resourceName, SpriteSheet spriteSheet)
	{
		File file = new File(resourceName);
		if(!AssetPool.spriteSheets.containsKey(file.getAbsolutePath()))
			AssetPool.spriteSheets.put(file.getAbsolutePath(), spriteSheet);
	}
	
	public static SpriteSheet getSpriteSheet(String resourceName)
	{
		File file = new File(resourceName);
		if(!AssetPool.spriteSheets.containsKey(file.getAbsolutePath()))
			assert false : "Error: Tried to access spriteSheet '" + resourceName + "' and it has not been added to asset pool!";
		return AssetPool.spriteSheets.getOrDefault(file.getAbsolutePath(), null);
	}
	
}
