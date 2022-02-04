package com.bluetitansoftware.apcsfp.engine.object.component;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import com.bluetitansoftware.apcsfp.engine.io.Input;
import com.bluetitansoftware.apcsfp.engine.io.Window;
import com.bluetitansoftware.apcsfp.engine.object.Camera;
import com.bluetitansoftware.apcsfp.engine.object.GameObject;

public class Controls extends Component
{
	
	private Camera camera;
	
	public Controls(GameObject go, Camera camera)
	{
		this.gameObject = go;
		this.camera = camera;
	}
	
	@Override
	public void update(float dt)
	{
		if(Input.isKeyDown(GLFW.GLFW_KEY_W)) gameObject.t.p.y += Player.MOVE_SPEED * dt;
		if(Input.isKeyDown(GLFW.GLFW_KEY_S)) gameObject.t.p.y -= Player.MOVE_SPEED * dt;
		if(Input.isKeyDown(GLFW.GLFW_KEY_A)) gameObject.t.p.x -= Player.MOVE_SPEED * dt;
		if(Input.isKeyDown(GLFW.GLFW_KEY_D)) gameObject.t.p.x += Player.MOVE_SPEED * dt;
		
		Vector2d dir = new Vector2d(Input.getMouseOrthoX() - (camera.position.x() + Window.width() / 2) - gameObject.t.s().x(), (Window.height() - Input.getMouseOrthoY()) - (camera.position.y() + (Window.height() / 2)));
		double angle = Math.toDegrees(Math.atan2(-dir.y(), dir.x()));
		gameObject.t.r = (int)angle == 0 ? 0.001f : (short)angle;
		
		camera.position = new Vector2f(gameObject.t.p().x() - (float)(Window.width() / 2) - gameObject.t.s().x(), gameObject.t.p().y() - (float)(Window.height() / 2));
				
		if(Input.isButtonPress(GLFW.GLFW_MOUSE_BUTTON_LEFT))
			gameObject.getComponent(Player.class).shoot();
		}
	
}
