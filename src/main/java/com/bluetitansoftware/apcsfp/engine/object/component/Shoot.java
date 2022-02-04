package com.bluetitansoftware.apcsfp.engine.object.component;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;
import com.bluetitansoftware.apcsfp.engine.object.Bullet;
import com.bluetitansoftware.apcsfp.engine.object.GameObject;
import com.bluetitansoftware.apcsfp.engine.scene.Scene;

public class Shoot extends Component
{
	
	private GameObject go;
	
	private GameObject mapObject;
	private Vector3f[][] map;
	
	public List<Bullet> bullets = new ArrayList<>();
	
	public Shoot(GameObject go, GameObject mapObject, Vector3f[][] map)
	{
		this.go = go;
		
		this.mapObject = mapObject;
		this.map = map;
	}
	
	private int MOVE_SPEED = 500;
	private List<Bullet> toBeRemoved;
	
	@Override
	public void update(float dt)
	{
		toBeRemoved = new ArrayList<>();
		
		for(int index = 0; index < bullets.size(); index++)
		{
			Bullet bulletObj = bullets.get(index);
			GameObject bullet = bulletObj.bullet;
			
			float x = (float)Math.sin(Math.toRadians(bullet.t.r())) * MOVE_SPEED * dt;
			float y = (float)Math.cos(Math.toRadians(bullet.t.r())) * MOVE_SPEED * dt;
			
			bullet.t.p().add(y, x);
			
			int xMap = (int)(bullet.t.p().x() / (mapObject.t.s.x() / 48));
			
			if(map[xMap][47 - (int)(bullet.t.p().y() / (mapObject.t.s.y() / map[xMap].length))].equals(new Vector3f()))
				toBeRemoved.add(bulletObj);
		}
	}
	
	public void shoot()
	{
		Bullet bullet = new Bullet(go);
		bullets.add(bullet);
	}
	
	private int bulletCount = 0;
	
	public void removeBullets(Scene scene)
	{
		if(bulletCount < bullets.size())
		{
			scene.addGameObjectToScene(bullets.get(bulletCount).bullet);
			bulletCount = bullets.size();
		}
		else
			bulletCount = bullets.size();
		
		for(int i = 0; i < bullets.size(); i++)
			for(Bullet remove : toBeRemoved)
				if(bullets.get(i) == remove)
					bullets.remove(i);
	}
	
	public void shot(List<Player> players, GameObject shooter, Scene scene)
	{
		for(Player player : players)
		{
			Vector2f pos = new Vector2f(player.go().t.p().x(), player.go().t.p().y());
			for(Bullet bullet : bullets)
				if(bullet.bullet.t.p().distance(pos) < Player.CIRCLE_HIT_BOX && player.go().getName() != shooter.getName() && !bullet.spent() && player.alive())
				{
					toBeRemoved.add(bullet);
					player.hit();
					bullet.spend();
				}
		}
		
		removeBullets(scene);
	}
	
	public GameObject go()
	{
		return go;
	}
	
}
