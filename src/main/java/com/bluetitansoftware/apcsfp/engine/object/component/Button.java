package com.bluetitansoftware.apcsfp.engine.object.component;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import com.bluetitansoftware.apcsfp.engine.io.Input;
import com.bluetitansoftware.apcsfp.engine.io.Window;
import com.bluetitansoftware.apcsfp.engine.object.GameObject;

public class Button extends Component
{
	
	private Vector2f topRight, bottomLeft;
	
	private int action;
	
	public Button(GameObject go, int action)
	{
		this.topRight = new Vector2f(go.t.p().x() + go.t.s().x(), go.t.p().y() + go.t.s().y());
		this.bottomLeft = new Vector2f(go.t.p());
		this.action = action;
	}
	
	@Override
	public void update(float dt)
	{
		if(Input.getMouseOrthoX() >= bottomLeft.x() && Input.getMouseOrthoY() >= bottomLeft.y() && Input.getMouseOrthoX() <= topRight.x() && Input.getMouseOrthoY() <= topRight.y())
			if(Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT))
				actions();
	}
	
	public void actions()
	{
		switch(action)
		{
		case 0:
			Window.changeScene(0);
			break;
		case 1:
			Window.changeScene(1);
			break;
		}
	}
	
}
