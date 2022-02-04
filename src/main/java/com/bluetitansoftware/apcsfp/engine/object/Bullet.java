package com.bluetitansoftware.apcsfp.engine.object;

import org.joml.Vector2f;

import com.bluetitansoftware.apcsfp.engine.graphics.Texture;
import com.bluetitansoftware.apcsfp.engine.object.component.Sprite;
import com.bluetitansoftware.apcsfp.engine.object.component.SpriteRenderer;

public class Bullet extends Entity
{
	
	public GameObject bullet;
	private boolean spent;
	
	private Texture bulletTexture = new Texture("resources/textures/bullet.png");
	
	public Bullet(GameObject shooter)
	{
		this.bullet = new GameObject("bullet", new Vector2f(shooter.t.p()), shooter.t.r(), 10);
		this.bullet.addComponent(new SpriteRenderer(new Sprite(bulletTexture)));
		
		this.spent = false;
	}
	
	public boolean spent()
	{
		return spent;
	}
	
	public void spend()
	{
		spent = true;
	}

}
