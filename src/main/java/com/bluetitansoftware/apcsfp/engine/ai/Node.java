package com.bluetitansoftware.apcsfp.engine.ai;

import org.joml.Vector2i;

public class Node
{
	
	public int gCost, hCost;
		
	private Vector2i location;
	private boolean traversable;
	
	public Node parent;

	public Node(Vector2i location, boolean traversable)
	{	
		this.location = location;
		this.traversable = traversable;
	}
	
	public Node(Vector2i start, Vector2i finish, Vector2i location, boolean traversable)
	{		
		this.location = location;
		this.traversable = traversable;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		if(!(obj instanceof Node))
			return false;
		
		Node node = (Node)obj;
		return node.gCost() == this.gCost() && node.hCost() == this.hCost() && node.fCost() == this.fCost() && node.location().equals(this.location());
	}
	
	

	@Override
	public String toString()
	{
		return location.x() + " " + location.y();
	}

	public int gCost()
	{
		return gCost;
	}
	
	public int hCost()
	{
		return hCost;
	}
	
	public int fCost()
	{
		return gCost + hCost;
	}
	
	public Node parent()
	{
		return parent;
	}
	
	public Vector2i location()
	{
		return location;
	}
	
	public boolean traversable()
	{
		return traversable;
	}
	
}
