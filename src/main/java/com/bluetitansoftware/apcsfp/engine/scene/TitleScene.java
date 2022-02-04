package com.bluetitansoftware.apcsfp.engine.scene;

import org.joml.Vector2f;

import com.bluetitansoftware.apcsfp.engine.object.Camera;
import com.bluetitansoftware.apcsfp.engine.object.GameObject;
import com.bluetitansoftware.apcsfp.engine.object.component.Button;
import com.bluetitansoftware.apcsfp.engine.object.component.SpriteRenderer;
import com.bluetitansoftware.apcsfp.engine.object.component.SpriteSheet;
import com.bluetitansoftware.apcsfp.engine.util.AssetPool;

public class TitleScene extends Scene
{
	
	private GameObject objPlay;
	private SpriteSheet sprites;
	
	@Override
	public void init()
	{
		loadResources();
		
		this.camera = new Camera();
		
		sprites = AssetPool.getSpriteSheet("resources/textures/TitleSpriteSheet.jpg");
		
		objPlay = new GameObject("objPlay", new Vector2f(100, 100), 0, new Vector2f(200, 100));
		objPlay.addComponent(new SpriteRenderer(sprites.getSprite(0)));
		objPlay.addComponent(new Button(objPlay, 1));
		this.addGameObjectToScene(objPlay);
	}
	
	private void loadResources()
	{
		AssetPool.getShader("resources/shaders/default.glsl");
		AssetPool.addSpriteSheet("resources/textures/TitleSpriteSheet.jpg", new SpriteSheet(AssetPool.getTexture("resources/textures/TitleSpriteSheet.jpg"), 45, 16, 2, 1));
	}
	
	@Override
	public void update(float dt)
	{
		
		for(GameObject go : this.gameObjects)
			go.update(dt);
		
		this.renderer.render();
	}

}
