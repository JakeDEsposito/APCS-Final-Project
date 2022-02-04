package com.bluetitansoftware.apcsfp.engine.object;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import com.bluetitansoftware.apcsfp.engine.math.Transform;

public abstract class Entity
{
	
	public Transform t;
	
	private Matrix4f model;
	
	public Entity(Transform t)
	{
		this.t = t;
	}
	
	public Entity(Vector2f position, float rotation, Vector2f scale)
	{
		this.t = new Transform(position, rotation, scale);
	}
	
	public Entity(Vector2f position, float rotation, float scale)
	{
		this.t = new Transform(position, rotation, scale);
	}
	
	public Entity(Vector2f position, float rotation)
	{
		this.t = new Transform(position, rotation);
	}
	
	public Entity(Vector2f position)
	{
		this.t = new Transform(position);
	}
	
	public Entity()
	{
		this.t = new Transform();
	}
	
	public void update()
	{
		
	}
	
	public Vector2f getPosition()
	{
		return t.p();
	}
	
	public float getRotation()
	{
		return t.r();
	}
	
	public Vector2f getScale()
	{
		return t.s();
	}
	
	public Matrix4f getModel()
	{
		return model;
	}
	
}
