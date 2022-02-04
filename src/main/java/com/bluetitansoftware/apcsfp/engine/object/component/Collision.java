package com.bluetitansoftware.apcsfp.engine.object.component;

import org.joml.Vector2f;
import org.joml.Vector3f;
import com.bluetitansoftware.apcsfp.engine.io.Window;
import com.bluetitansoftware.apcsfp.engine.object.Camera;
import com.bluetitansoftware.apcsfp.engine.object.GameObject;

public class Collision extends Component
{
	
	private Camera camera;	
	private Vector2f lastPos;
	private GameObject go;
	
	private GameObject mapObject;
	private Vector3f[][] map;
	
	public Collision(Camera camera, GameObject go, GameObject mapObject, Vector3f[][] map)
	{
		this.camera = camera;
		this.go = go;
		this.lastPos = camera.position;
		
		this.mapObject = mapObject;
		this.map = map;
	}
	
	@Override
	public void update(float dt)
	{
		int x = (int)((camera.position.x() + go.t.s().x() + Window.width() / 2) / (mapObject.t.s.x() / map.length));
		
		if(map[x][47 - (int)((camera.position.y() + Window.height() / 2) / (mapObject.t.s.y() / map[x].length))].equals(new Vector3f()))
			go.t.p = new Vector2f(lastPos);
		else
			lastPos.set(go.t.p());
	}
	
}
