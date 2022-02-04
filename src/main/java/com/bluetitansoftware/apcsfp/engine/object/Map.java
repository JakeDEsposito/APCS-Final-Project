package com.bluetitansoftware.apcsfp.engine.object;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.bluetitansoftware.apcsfp.engine.ai.Node;
import com.bluetitansoftware.apcsfp.engine.graphics.Texture;
import com.bluetitansoftware.apcsfp.engine.object.component.Bot;
import com.bluetitansoftware.apcsfp.engine.object.component.Player;
import com.bluetitansoftware.apcsfp.engine.object.component.Shoot;
import com.bluetitansoftware.apcsfp.engine.object.component.SpriteRenderer;
import com.bluetitansoftware.apcsfp.engine.object.component.SpriteSheet;
import com.bluetitansoftware.apcsfp.engine.scene.Scene;

public class Map
{
	
	private GameObject mapObject;
	private float scale;
	private Texture mapTexture;
	private Vector3f[][] mapColorArray;
	private Node[][] mapNodeArray;
	private GameObject[][] mapSpriteArray;
	private SpriteSheet itemSprites;
	private GameObject[] mapItemArray;
	private Vector2f[] mapSpawnArray;
	
	private Vector2f stormCenter = new Vector2f(1, 1);
	private float stormRadius = 100;
	private float stormTick;
	
	public Map(Texture mapTexture, SpriteSheet itemSprites, SpriteSheet playerSprites) throws IOException
	{
		this.mapObject = new GameObject("map", new Vector2f(), 0, 7000);
		this.scale = mapObject.t.s().x() / mapTexture.getWidth();
		this.mapTexture = mapTexture;
		
		this.mapColorArray = new Vector3f[mapTexture.getWidth()][mapTexture.getHeight()];
		this.mapNodeArray = new Node[mapTexture.getWidth()][mapTexture.getHeight()];
		File file = new File(mapTexture.getFilePath());
		BufferedImage image = ImageIO.read(file);
		for(int x = 0; x < mapTexture.getWidth(); x++)
			for(int y = 0; y < mapTexture.getHeight(); y++)
			{
				int clr = image.getRGB(x, y);
				int r = (clr & 0x00ff0000) >> 16;
				int g = (clr & 0x0000ff00) >> 8;
				int b = clr & 0x000000ff;
				
				this.mapColorArray[x][y] = new Vector3f(r / 255, g / 255, b / 255);
				this.mapNodeArray[x][y] = new Node(new Vector2i(x, y), !mapColorArray[x][y].equals(0, 0, 0));
			}
		
		this.itemSprites = itemSprites;
	}
	
	public void generateMap(Scene scene) throws IOException
	{
		this.mapColorArray = new Vector3f[mapTexture.getWidth()][mapTexture.getHeight()];
		this.mapSpriteArray = new GameObject[mapTexture.getWidth()][mapTexture.getHeight()];
		File file = new File(mapTexture.getFilePath());
		BufferedImage image = ImageIO.read(file);
		for(int x = 0; x < mapTexture.getWidth(); x++)
			for(int y = 0; y < mapTexture.getHeight(); y++)
			{
				int clr = image.getRGB(x, y);
				int r = (clr & 0x00ff0000) >> 16;
				int g = (clr & 0x0000ff00) >> 8;
				int b = clr & 0x000000ff;
				
				Vector3f color = new Vector3f(r / 255, g / 255, b / 255);
				
				this.mapColorArray[x][y] = color;
				
				GameObject go = new GameObject("sprite:" + x + ":" + y, new Vector2f(x * scale, (mapTexture.getHeight() - 1 - y) * scale), 0, scale);
				go.addComponent(new SpriteRenderer(new Vector4f(color, 1)));
				scene.addGameObjectToScene(go);
				
				this.mapSpriteArray[x][y] = go;
			}
		
		this.stormCenter = new Vector2f((mapTexture.getWidth() / 2) * (mapObject.t.s().x() / mapTexture.getWidth()), (mapTexture.getHeight() / 2) * (mapObject.t.s().y() / mapTexture.getHeight()));
		this.stormRadius = mapObject.t.s().x() + 500;
	}
	
	public void generateItems(Scene scene)
	{
		Vector3f blue = new Vector3f(0, 0, 1);
		List<Vector2f> positions = new ArrayList<>();
		
		for(int row = 0; row < mapColorArray.length; row++)
			for(int col = 0; col < mapColorArray[row].length; col++)
				if(mapColorArray[row][col].equals(blue))
					positions.add(new Vector2f(row, col));
		
		this.mapItemArray = new GameObject[positions.size()];
		
		for(int index = 0; index < mapItemArray.length; index++)
		{
			int spriteNumber = (int)(Math.random() * 3);
			
			if(spriteNumber == 3)
				spriteNumber = 2;
			
			mapItemArray[index] = new GameObject("Item" + spriteNumber, new Vector2f(positions.get(index).x() * (mapObject.t.s().x() / mapTexture.getWidth()) + (mapObject.t.s().x() / mapTexture.getWidth()) / 2, (47 - positions.get(index).y()) * (mapObject.t.s().y() / mapTexture.getHeight()) + (mapObject.t.s().y() / mapTexture.getHeight()) / 2), 0.01f, 50);
			mapItemArray[index].addComponent(new SpriteRenderer(itemSprites.getSprite(spriteNumber)));
			scene.addGameObjectToScene(mapItemArray[index]);
		}
	}
	
	public void generateSpawns()
	{
		Vector3f red = new Vector3f(1, 0, 0);
		List<Vector2f> positions = new ArrayList<>();
		
		for(int row = 0; row < mapColorArray.length; row++)
			for(int col = 0; col < mapColorArray[row].length; col++)
				if(mapColorArray[row][col].equals(red))
					positions.add(new Vector2f(row, col));
		
		this.mapSpawnArray = new Vector2f[positions.size()];
		
		for(int i = 0; i < mapSpawnArray.length; i++)
			mapSpawnArray[i] = new Vector2f(positions.get(i).x() * (mapObject.t.s().x() / mapTexture.getWidth()) + (mapObject.t.s().x() / mapTexture.getWidth()) / 2, (47 - positions.get(i).y()) * (mapObject.t.s().y() / mapTexture.getHeight()) + (mapObject.t.s().y() / mapTexture.getHeight()) / 2);
	}
	
	public void spawnPlayers(List<Player> players, List<Shoot> shooters, SpriteSheet playerSprites, Scene scene)
	{
		boolean[] mapSpawnTakenArray = new boolean[mapSpawnArray.length];
		Arrays.fill(mapSpawnTakenArray, false);
		
		int index = (int)(Math.random() * mapSpawnArray.length);
		players.get(0).go().t.p = mapSpawnArray[index];
		mapSpawnTakenArray[index] = true;
		
		for(int i = 0; i < mapSpawnTakenArray.length; i++)
			if(mapSpawnTakenArray[i] == false)
			{
				GameObject bot = new GameObject("player" + i, mapSpawnArray[i], 0.001f, 100);
				Shoot botShoot = new Shoot(bot, map(), mapColorArray());
				
				bot.addComponent(new SpriteRenderer(playerSprites.getSprite(i)));
				bot.addComponent(new Player(bot));
				bot.addComponent(botShoot);
				bot.addComponent(new Bot(bot.getComponent(Player.class), this, players));
				scene.addGameObjectToScene(bot);
				players.add(bot.getComponent(Player.class));
				shooters.add(botShoot);
			}
	}
	
	public List<Node> getNeighbours(Node node)
	{
		List<Node> neighbours = new ArrayList<Node>();

		for (int x = -1; x <= 1; x++)
			for (int y = -1; y <= 1; y++)
			{
				if (x == 0 && y == 0 || x == -1 && y == -1 || x == 1 && y == -1 || x == -1 && y == 1 || x == 1 && y == 1)
					continue;

				int checkX = node.location().x() + x;
				int checkY = node.location().y() + y;
				
				if (checkX >= 0 && checkX < mapNodeArray.length && checkY >= 0 && checkY < mapNodeArray[checkX].length) {
					neighbours.add(mapNodeArray[checkX][checkY]);
				}
			}

		return neighbours;
	}
	
	public void update(float dt, List<Player> players)
	{
		boolean stormExecute = false;
		
		stormRadius -= 50 * dt;
		stormTick += dt;
		
		if(stormTick > 1.5)
		{
			stormTick = 0;
			stormExecute = true;
		}
		
		for(Player player : players)
		{
			for(int i = 0; i < mapItemArray.length; i++)
			{
				GameObject item = mapItemArray[i];
				if(player.gameObject.t.p().distance(item.t.p()) < Player.CIRCLE_HIT_BOX)
				{
					int itemType = Integer.parseInt(item.getName().substring(4));
					boolean used = false;
					
					if(itemType == 0 && !player.healthFull())
					{
						player.addHealth();
						used = true;
					}
					else if(itemType == 1 && !player.ammoFull())
					{
						player.addAmmo();
						used = true;
					}
					else if(itemType == 2 & !player.shieldFull())
					{
						player.addShield();
						used = true;
					}
					
					if(used)
					{
						item.t.s = new Vector2f();
						item.t.p = new Vector2f();
					}
				}
			}
			
			if(player.gameObject.t.p().distance(stormCenter) > stormRadius && stormExecute)
				player.hit();
		}
		
		for(GameObject[] tileArray : mapSpriteArray)
			for(GameObject tile : tileArray)
			{
				if(tile.t.p().distance(stormCenter) > stormRadius && !tile.getComponent(SpriteRenderer.class).getColor().equals(new Vector4f(0, 0, 0, 1)) && stormExecute)
					tile.getComponent(SpriteRenderer.class).setColor(new Vector4f(0.5f));
			}
	}
	
	public GameObject map()
	{
		return mapObject;
	}
	
	public Vector3f[][] mapColorArray()
	{
		return mapColorArray;
	}
	
	public Node[][] mapNodeArray()
	{
		return mapNodeArray;
	}
	
	public GameObject[] mapItemArray()
	{
		return mapItemArray;
	}
	
}
