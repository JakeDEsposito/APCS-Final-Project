package com.bluetitansoftware.apcsfp.engine.graphics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.bluetitansoftware.apcsfp.engine.object.GameObject;
import com.bluetitansoftware.apcsfp.engine.object.component.SpriteRenderer;

public class Renderer
{
	
	private final int MAX_BATCH_SIZE = 1000;
	private List<RenderBatch> batches;
	
	public Renderer()
	{
		this.batches = new ArrayList<>();
	}
	
	public void add(GameObject go)
	{
		SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
		if(spr != null)
			add(spr);
	}
	
	private void add(SpriteRenderer sprite)
	{
		boolean added = false;
		for(RenderBatch batch : batches)
			if(batch.hasRoom())
			{
				Texture tex = sprite.getTexture();
				if(tex == null || (batch.hasTexture(tex) || batch.hasTextureRoom()))
				{
					batch.addSprite(sprite);
					added = true;
					break;
				}
			}
		
		if(!added)
		{
			RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.zIndex());
			newBatch.start();
			batches.add(newBatch);
			newBatch.addSprite(sprite);
			Collections.sort(batches);
		}
	}
	
	public void render()
	{
		for(RenderBatch batch : batches)
			batch.render();
	}
	
}
