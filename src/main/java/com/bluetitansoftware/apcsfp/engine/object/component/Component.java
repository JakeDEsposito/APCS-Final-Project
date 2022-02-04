package com.bluetitansoftware.apcsfp.engine.object.component;

import com.bluetitansoftware.apcsfp.engine.object.GameObject;

public abstract class Component
{
	
	public GameObject gameObject = null;
	
	public void start()
	{
		
	}
	
	public abstract void update(float dt);
	
}
