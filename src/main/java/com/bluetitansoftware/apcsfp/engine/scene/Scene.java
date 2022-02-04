package com.bluetitansoftware.apcsfp.engine.scene;

import java.util.ArrayList;
import java.util.List;

import com.bluetitansoftware.apcsfp.engine.graphics.Renderer;
import com.bluetitansoftware.apcsfp.engine.object.Camera;
import com.bluetitansoftware.apcsfp.engine.object.GameObject;

public abstract class Scene
{
	
	protected Renderer renderer = new Renderer();
	protected Camera camera;
	protected List<GameObject> gameObjects = new ArrayList<>();
	private boolean isRunning = false;
	
	public Scene()
	{
		
	}
	
	public void init()
	{
		
	}
	
	public void start()
	{
		for(GameObject go : gameObjects)
		{
			go.start();
			this.renderer.add(go);
		}
		isRunning = true;
	}
	
	public void addGameObjectToScene(GameObject go)
	{
		if(!isRunning)
			gameObjects.add(go);
		else
		{
			gameObjects.add(go);
			go.start();
			this.renderer.add(go);
		}
	}
	
	public abstract void update(float dt);
	
	public void close()
	{
		
	}
	
	public Camera camera()
	{
		return camera;
	}
	
}
