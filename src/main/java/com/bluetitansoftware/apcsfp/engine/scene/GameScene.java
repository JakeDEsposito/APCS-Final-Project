package com.bluetitansoftware.apcsfp.engine.scene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import com.bluetitansoftware.apcsfp.engine.graphics.Texture;
import com.bluetitansoftware.apcsfp.engine.io.Window;
import com.bluetitansoftware.apcsfp.engine.object.Camera;
import com.bluetitansoftware.apcsfp.engine.object.GameObject;
import com.bluetitansoftware.apcsfp.engine.object.Map;
import com.bluetitansoftware.apcsfp.engine.object.component.Collision;
import com.bluetitansoftware.apcsfp.engine.object.component.Controls;
import com.bluetitansoftware.apcsfp.engine.object.component.Player;
import com.bluetitansoftware.apcsfp.engine.object.component.Shoot;
import com.bluetitansoftware.apcsfp.engine.object.component.SpriteRenderer;
import com.bluetitansoftware.apcsfp.engine.object.component.SpriteSheet;
import com.bluetitansoftware.apcsfp.engine.util.AssetPool;

public class GameScene extends Scene
{
	
	public GameObject playerMain;
	private List<Player> players;
	private Map map;
	
	private SpriteSheet playerSprites;
	
	private List<Shoot> shooters;
	
	private SpriteSheet itemSprites;
	private SpriteSheet gameEnd;
	
	private GameObject gameEndObject;
	private boolean gameOver = false;
	private float gameOverTimer = 2;
	
	@Override
	public void init()
	{
		
		loadResources();
		
		this.camera = new Camera(new Vector2f());
		
		playerSprites = AssetPool.getSpriteSheet("resources/textures/playerSpriteSheet.png");
		itemSprites = AssetPool.getSpriteSheet("resources/textures/gameMapItemSpriteSheet.png");
		gameEnd = AssetPool.getSpriteSheet("resources/textures/gameSceneEnd.png");
		
		this.players = new ArrayList<>();
		shooters = new ArrayList<>();
		
		try
		{
			this.map = new Map(new Texture("resources/textures/gameMap.png"), itemSprites, playerSprites);
			map.generateMap(this);
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		map.generateItems(this);
		map.generateSpawns();
		
		this.playerMain = new GameObject("playerMain", new Vector2f(), 0, 100);
		playerMain.addComponent(new SpriteRenderer(playerSprites.getSprite(0)));
		playerMain.addComponent(new Controls(playerMain, camera));
		playerMain.addComponent(new Player(playerMain));
		
		Shoot shoot = new Shoot(playerMain, map.map(), map.mapColorArray());
		playerMain.addComponent(shoot);
		playerMain.addComponent(new Collision(camera, playerMain, map.map(), map.mapColorArray()));
		
		this.addGameObjectToScene(playerMain);
		players.add(playerMain.getComponent(Player.class));
		shooters.add(shoot);
		
		map.spawnPlayers(players, shooters, playerSprites, this);
		
		this.gameEndObject = new GameObject("gameOverObject");
		this.gameEndObject.addComponent(new SpriteRenderer(gameEnd.getSprite(0)));
		this.addGameObjectToScene(gameEndObject);
	}
	
	private void loadResources()
	{
		AssetPool.getShader("resources/shaders/default.glsl");
		AssetPool.addSpriteSheet("resources/textures/playerSpriteSheet.png", new SpriteSheet(AssetPool.getTexture("resources/textures/playerSpriteSheet.png"), 8, 8, 8, 1));
		AssetPool.addSpriteSheet("resources/textures/gameMapItemSpriteSheet.png", new SpriteSheet(AssetPool.getTexture("resources/textures/gameMapItemSpriteSheet.png"), 8, 8, 4, 1));
		AssetPool.addSpriteSheet("resources/textures/gameSceneEnd.png", new SpriteSheet(AssetPool.getTexture("resources/textures/gameSceneEnd.png"), 45, 16, 2, 2));
	}
	
	@Override
	public void update(float dt)
	{
		
		for(GameObject go : this.gameObjects)
			go.update(dt);
		
		for(Shoot shoot : shooters)
		{
			shoot.removeBullets(this);
			shoot.shot(players, shoot.go(), this);
		}
				
		map.update(dt, players);
		
		gameOver(dt);
		
		this.renderer.render();
		
	}
	
	private void gameOver(float dt)
	{
		int playersAlive = 0;
		for(Player player : players)
			if(player.alive())
				playersAlive++;
		
		if(!gameOver)
		{
			if(!playerMain.getComponent(Player.class).alive())
			{
				gameOver = true;
				gameEndObject.getComponent(SpriteRenderer.class).setSprite(gameEnd.getSprite(1));
				
				Vector2f pos = new Vector2f();
				playerMain.getPosition().sub(new Vector2f(100, 50), pos);
				gameEndObject.t.p = new Vector2f(pos);
				
				gameEndObject.t.s = new Vector2f(200, 100);
				playerMain.removeComponent(Controls.class);
			}
			else if(playersAlive <= 1)
			{
				gameOver = true;
				gameEndObject.getComponent(SpriteRenderer.class).setSprite(gameEnd.getSprite(0));
				
				Vector2f pos = new Vector2f();
				playerMain.getPosition().sub(new Vector2f(100, 50), pos);
				gameEndObject.t.p = new Vector2f(pos);
				
				gameEndObject.t.s = new Vector2f(200, 100);
				playerMain.removeComponent(Controls.class);
			}
		}
		
		if(gameOver)
			gameOverTimer -= dt;
		
		if(gameOverTimer <= 0)
			Window.changeScene(0);
	}
	
	public List<Player> players()
	{
		return players;
	}
	
}
