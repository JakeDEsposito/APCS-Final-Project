package com.bluetitansoftware.apcsfp.engine.scene;

import com.bluetitansoftware.apcsfp.engine.object.Camera;
import com.bluetitansoftware.apcsfp.engine.object.GameObject;
import com.bluetitansoftware.apcsfp.engine.util.AssetPool;

public class TestScene extends Scene
{
	
	public TestScene()
	{
		
	}
	
	@Override
	public void init()
	{
		loadResources();
		
		this.camera = new Camera();
	}
	
	private void loadResources()
	{
		AssetPool.getShader("resources/shaders/default.glsl");
	}
	
	@Override
	public void update(float dt)
	{
		for(GameObject go : this.gameObjects)
			go.update(dt);
		
		this.renderer.render();
	}
	
}
