package com.bluetitansoftware.apcsfp.engine.object.component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import com.bluetitansoftware.apcsfp.engine.ai.Node;
import com.bluetitansoftware.apcsfp.engine.ai.Path;
import com.bluetitansoftware.apcsfp.engine.object.Map;

public class Bot extends Component
{
	
	private Player bot;
	private Map map;
	private Vector3f[][] mapColorArray;
	private List<Player> players;
	
	private int itemsToPickUp;
	
	private float viewDist = 500;
	private float shootTimer;
	private float shootFreq = 0.5f;
	
	private Boolean pathFound;
	private Vector2i pointOfInterest;
	private HashSet<Vector2i> pointsVisited;
	private List<Node> path;
	private int pathStep;
	private float stormTimer;
	
	private Vector2f lastPos;
	
	public Bot(Player bot, Map map, List<Player> players)
	{
		this.bot = bot;
		this.map = map;
		this.mapColorArray = map.mapColorArray();
		this.players = players;
		
		this.itemsToPickUp = (int)(Math.random() * 2 + 3);
		
		this.pathFound = null;
		this.pointsVisited = new HashSet<>();
		this.path = new ArrayList<>();
		this.pathStep = 0;
		
		this.lastPos = bot.go().t.p();
	}
	
	@Override
	public void update(float dt)
	{
		move(dt);
		
		shoot(dt);
		
		collision();
	}
	
	private void shoot(float dt)
	{
		shootTimer += dt;
		
		for(Player player : players)
		{
			Vector2f playerPos = player.go().t.p();
			Vector2f botPos = bot.go().t.p();
			if(playerPos.distance(botPos) < viewDist && player.go() != bot.go() && player.alive())
			{
				Vector2d dir = new Vector2d(playerPos.x() - bot.go().t.p().x(), playerPos.y() - bot.go().t.p().y());
				double angle = Math.toDegrees(Math.atan2(dir.y(), dir.x()));
				bot.go().t.r = (float)angle;
				
				if(shootTimer > shootFreq)
				{
					bot.shoot();
					shootTimer = 0;
					break;
				}
			}
		}
	}
	
	//TODO bots could be smarter and make moves based on there current state
	//if a bot is low on ammo, health, or shield, it can seek that thing out
	//also when its time to go to the middle, bots move in slowly instead of all at once
	private void move(float dt)
	{
		stormTimer += dt;
		
		Vector2f pos = bot.go().t.p;
		float x = (pos.x() / (map.map().t.s.x() / mapColorArray.length));
		float y = 47 - (pos.y() / (map.map().t.s.y() / mapColorArray[(int)x].length));
		
		Vector2f botPos = new Vector2f(x, y);
		Vector2i botPosi = new Vector2i((int)x, (int)y);
		
		//if(bot.go().getComponent(Player.class).go().getName().equals("playerMain"))System.out.println(pathFound);
		
		//FIXME finds first point but then doesn't find more after that
		//could be because it doesn't see the points below its current position
		if(pathFound == null)
		{
			Vector3f pointColor;
			
			if(pointsVisited.size() < itemsToPickUp) ///TODO items to pick up
				pointColor = new Vector3f(0, 0, 1);
			else if(stormTimer < 40)//60
				pointColor = new Vector3f(1, 0, 0);
			else
				pointColor = new Vector3f(0, 1, 0);
			
			for(int row = 0; row < mapColorArray.length; row++)
				for(int col = 0; col < mapColorArray[row].length; col++)
					if(mapColorArray[row][col].equals(pointColor))
					{
						Vector2i vec = new Vector2i(row, col);
						
						boolean visited = false;
						for(Vector2i point : pointsVisited)
							if(point.equals(vec))
								visited = true;
						
						if(!visited)
						{
							if(pointOfInterest == null)
								pointOfInterest = vec;
							
							if(botPosi.distance(vec) < botPosi.distance(pointOfInterest))
								pointOfInterest = vec;
						}
					}
			
			if(pointOfInterest == null)
				pointOfInterest = new Vector2i((int)(Math.random() * 4 + 22), (int)(Math.random() * 4 + 21));
			
			pathFound = false;
		}
		else if(!pathFound)
		{
			Path currentPath = new Path(botPosi, pointOfInterest, map);
						
			currentPath.evaluate();
			
			path = currentPath.path();
			
			
			//dev code -----
			
			//if(bot.go().getComponent(Player.class).go().getName().equals("playerMain"))
//				for(Node n : path)
//					map.mapSpriteArray[n.location().x()][n.location().y()].getComponent(SpriteRenderer.class).setColor(new Vector4f(0, 1, 0, 1));
			
			//--------------
			
			pathStep = 0;
			pathFound = true;
		}
		else if(pathFound)
		{
			int nextStep = pathStep + 1;
			
			if(nextStep < path.size())
			{
				Vector2f nextNode = new Vector2f((float)path.get(nextStep).location().x() + 0.5f, (float)path.get(nextStep).location().y() - 0.5f);
				float distToNextNode = botPos.distance(nextNode);
				
				Vector2f dir = new Vector2f();
				nextNode.sub(botPos, dir);
				double angle = Math.toDegrees(Math.atan2(-dir.y(), dir.x()));
				
				bot.go().t.p.x += dir.x() * (Player.MOVE_SPEED / distToNextNode) * dt;
				bot.go().t.p.y += -dir.y() * (Player.MOVE_SPEED / distToNextNode) * dt;
				
				if(angle == 0) bot.go().t.r = 0.01f;
				else bot.go().t.r = (float)angle;
				
				//TODO find good deviation
				if(distToNextNode <= 0.1f)// < 0.5f)
					pathStep++;
				
			}
			else
			{
				pointsVisited.add(pointOfInterest);
				pointOfInterest = null;
				pathFound = null;
			}
			
		}
	}
	
	private void collision()
	{
		Vector2f pos = bot.go().t.p;
		
		int x = (int)(pos.x() / (map.map().t.s.x() / mapColorArray.length));
		
		if(mapColorArray[x][47 - (int)(pos.y() / (map.map().t.s.y() / mapColorArray[x].length))].equals(new Vector3f()))
			bot.go().t.p = new Vector2f(lastPos);
		else
			lastPos.set(pos);
	}
	
}
