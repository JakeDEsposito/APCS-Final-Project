package com.bluetitansoftware.apcsfp.engine.object.component;

import org.joml.Vector2f;
import org.joml.Vector4f;

import com.bluetitansoftware.apcsfp.engine.graphics.Texture;
import com.bluetitansoftware.apcsfp.engine.math.Transform;

public class SpriteRenderer extends Component
{
	
	private Vector4f color;
	private Sprite sprite;
	
	private Transform lastTransform;
	private boolean isDirty = false;
	
	public SpriteRenderer(Vector4f color)
	{
		this.color = color;
		this.sprite = new Sprite(null);
		this.isDirty = true;
	}
	
	public SpriteRenderer(Sprite sprite)
	{
		this.color = new Vector4f(1, 1, 1, 1);
		this.sprite = sprite;
		this.isDirty = true;
	}
	
	public SpriteRenderer()
	{
		this.color = new Vector4f();
	}
	
	@Override
	public void start()
	{
		this.lastTransform = gameObject.t.copy();
	}
	
	@Override
	public void update(float dt)
	{
		if(!this.lastTransform.equals(this.gameObject.t))
		{
			this.gameObject.t.copy(this.lastTransform);
			isDirty = true;
		}
	}
	
	public void setSprite(Sprite sprite)
	{
		this.sprite = sprite;
		this.isDirty = true;
	}
	
	public void setColor(Vector4f color)
	{
		if(!this.color.equals(color))
			this.color.set(color);
			this.isDirty = true;
	}

	public Vector4f getColor()
	{
		return color;
	}
	
	public Texture getTexture()
	{
		return sprite.getTexture();
	}
	
	public Vector2f[] getTexCoords()
	{
		return sprite.getTexCoords();
	}
	
	public boolean isDirty()
	{
		return isDirty;
	}
	
	public void setClean()
	{
		this.isDirty = false;
	}
	
}
