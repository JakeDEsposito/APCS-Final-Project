package com.bluetitansoftware.apcsfp.engine.object;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera
{
	
	private Matrix4f projection, view;
	public Vector2f position;
	
	private Matrix4f inverseProjection, inverseView;
	
	public Camera(Vector2f position)
	{
		this.position = position;
		this.projection = new Matrix4f();
		this.view = new Matrix4f();
		
		this.inverseProjection = new Matrix4f();
		this.inverseView = new Matrix4f();
		
		adjustProjection();
	}
	
	public Camera()
	{
		this.projection = new Matrix4f();
		this.view = new Matrix4f();
		this.position = new Vector2f();
		
		this.inverseProjection = new Matrix4f();
		this.inverseView = new Matrix4f();
		
		adjustProjection();
	}
	
	public void adjustProjection()
	{
		projection.identity();
		projection.ortho(0.0f, 32.0f * 40.0f, 0.0f, 32.0f * 21.0f, 0.0f, 100.0f);
		
		projection.invert(inverseProjection);
	}
	
	public Matrix4f getView()
	{
		Vector3f cameraFront = new Vector3f(0, 0, -1);
		Vector3f cameraUp = new Vector3f(0, 1, 0);
		this.view.identity();
		view.lookAt(new Vector3f(position.x, position.y, 20), cameraFront.add(position.x, position.y, 0), cameraUp);
		
		this.view.invert(inverseView);
		
		return this.view;
	}
	
	public Matrix4f getProjection()
	{
		return this.projection;
	}

	public Matrix4f getInverseProjection()
	{
		return this.inverseProjection;
	}

	public Matrix4f getInverseView()
	{
		return this.inverseView;
	}
	
}
