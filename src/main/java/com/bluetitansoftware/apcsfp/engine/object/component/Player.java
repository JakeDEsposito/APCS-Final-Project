package com.bluetitansoftware.apcsfp.engine.object.component;

import org.joml.Vector4f;
import com.bluetitansoftware.apcsfp.engine.object.GameObject;

public class Player extends Component
{
	
	public static int MOVE_SPEED = 200;
	public static int CIRCLE_HIT_BOX = 50;
	
	private GameObject go;
	
	private byte health, shield, ammo;
	
	private boolean dead;
	
	public Player(GameObject go)
	{
		this.go = go;
		
		this.health = 10;
		this.shield = 0;
		this.ammo = 40;
		
		this.dead = false;
	}

	@Override
	public void update(float dt)
	{		
		float healthColor = (health / 10f) * 0.8f + 0.2f;
		go.getComponent(SpriteRenderer.class).setColor(new Vector4f(healthColor, healthColor, healthColor, 1));
				
		if(health <= 0 && !dead)
		{
			dead = true;
			System.out.println(go.getName() + " DEAD");
		}
		
		if(dead)
		{
			go.getComponent(SpriteRenderer.class).setColor(new Vector4f(0.2f, 0.2f, 0.2f, 0.8f));
			
			go.removeComponent(Controls.class);
			go.removeComponent(Bot.class);
		}
	}
	
	public void hit()
	{
		if(shield > 0)
			shield--;
		else
			health--;
	}
	
	public void addHealth()
	{
		health += 5;
		if(health > 10)
			health = 10;
	}
	
	public void addShield()
	{
		shield += 2;
		if(shield > 10)
			shield = 10;
	}
	
	public void addAmmo()
	{
		ammo += 10;
		if(ammo > 100)
			ammo = 100;
	}
	
	public boolean healthFull()
	{
		return health >= 10;
	}
	
	public boolean shieldFull()
	{
		return shield >= 10;
	}
	
	public boolean ammoFull()
	{
		return ammo >= 100;
	}
	
	public void shoot()
	{
		if(ammo > 0 && !dead)
		{
			go.getComponent(Shoot.class).shoot();
			ammo--;
		}
	}
	
	public boolean alive()
	{
		return !dead;
	}
	
	public GameObject go()
	{
		return go;
	}
	
}
