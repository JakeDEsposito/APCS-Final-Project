package com.bluetitansoftware.apcsfp.engine.object;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import com.bluetitansoftware.apcsfp.engine.math.Transform;
import com.bluetitansoftware.apcsfp.engine.object.component.Component;

public class GameObject extends Entity
{
	
	private List<Component> components;
	private String name;
	private int zIndex;
	
	public GameObject(String name, Transform transform, int zIndex)
	{
		super(transform);
		this.name = name;
		this.components = new ArrayList<>();
		this.zIndex = zIndex;
	}
	
	public GameObject(String name, Transform transform)
	{
		super(transform);
		this.name = name;
		this.components = new ArrayList<>();
		this.zIndex = 0;
	}
	
	public GameObject(String name, Vector2f position, float rotation, Vector2f scale)
	{
		super(position, rotation, scale);
		this.name = name;
		this.components = new ArrayList<>();
		this.zIndex = 0;
	}
	
	public GameObject(String name, Vector2f position, float rotation, float scale)
	{
		super(position, rotation, scale);
		this.name = name;
		this.components = new ArrayList<>();
		this.zIndex = 0;
	}
	
	public GameObject(String name, Vector2f position, float rotation)
	{
		super(position, rotation);
		this.name = name;
		this.components = new ArrayList<>();
		this.zIndex = 0;
	}
	
	public GameObject(String name)
	{
		super();
		this.name = name;
		this.components = new ArrayList<>();
		this.zIndex = 0;
	}
	
	public void start()
	{
		for(int i = 0; i < components.size(); i++)
			components.get(i).start();
	}
	
	public void update(float dt)
	{
		for(int i = 0; i < components.size(); i++)
			components.get(i).update(dt);
	}
	
	public void addComponent(Component c)
	{
		this.components.add(c);
		c.gameObject = this;
	}
	
	public <T extends Component> void removeComponent(Class<T> componentClass)
	{
		for(int i = 0; i < components.size(); i++)
		{
			Component c = components.get(i);
			if(componentClass.isAssignableFrom(c.getClass()))
			{
				components.remove(i);
				return;
			}
		}
	}
	
	public <T extends Component> T getComponent(Class<T> componentClass)
	{
		for(Component c : components)
		{
			if(componentClass.isAssignableFrom(c.getClass()))
			{
				try
				{
					return componentClass.cast(c);
				}
				catch(ClassCastException cce)
				{
					cce.printStackTrace();
					assert false : "Error: Casting component.";
				}
			}
				
		}
		return null;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int zIndex()
	{
		return zIndex;
	}
	
}
