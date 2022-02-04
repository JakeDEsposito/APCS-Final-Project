package com.bluetitansoftware.apcsfp.engine.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.joml.Vector2i;
import com.bluetitansoftware.apcsfp.engine.object.Map;

public class Path
{
	
	private List<Node> open, path;
	HashSet<Node> closed;
	
	private Node start, finish;
	
	private Map map;
		
	public Path(Vector2i start, Vector2i finish, Map map)
	{
		this.open = new ArrayList<>();
		this.closed = new HashSet<>();
		this.path = new ArrayList<>();
		
		this.start = map.mapNodeArray()[start.x()][start.y()];
		this.finish = map.mapNodeArray()[finish.x()][finish.y()];
		
		this.open.add(map.mapNodeArray()[start.x()][start.y()]);
		
		this.map = map;
	}
	
	public void evaluate()
	{	
		while(open.size() > 0)
		{
			Node current = open.get(0);
			
			for(int i = 0; i < open.size(); i++)
				if(open.get(i).fCost() < current.fCost() || open.get(i).fCost() == current.fCost() && open.get(i).hCost() < current.hCost())
					current = open.get(i);
			
			open.remove(current);
			closed.add(current);
						
			if(current == finish)
			{
				path = retracePath(start, finish);
			}
			
			for(Node neighbor : map.getNeighbours(current))
			{
				if(!neighbor.traversable() || closed.contains(neighbor))
					continue;
				
				int newMoveCostToNeighbor = current.gCost() + distance(current, neighbor);
				if(newMoveCostToNeighbor < neighbor.gCost() || !open.contains(neighbor))
				{
					neighbor.gCost = newMoveCostToNeighbor;
					neighbor.hCost = distance(neighbor, finish);
					neighbor.parent = current;
					
					if(!open.contains(neighbor))
						open.add(neighbor);
				}
			}		
		}
	}
	
	@Override
	public String toString()
	{
		String s = "";
		for(Node n : path)
			s += n + " -> ";
		return s.substring(0, s.length() - 4);
	}

	private int distance(Node a, Node b)
	{
		int distX = (int)Math.abs(a.location().x() - b.location().x());
		int distY = (int)Math.abs(a.location().y() - b.location().y());
		
		if(distX > distY)
			return 14 * distY + 10 * (distX - distY);
		return 14 * distX + 10 * (distY - distX);
	}
	
	private List<Node> retracePath(Node startNode, Node endNode)
	{
		List<Node> path = new ArrayList<Node>();
		Node current = endNode;
		
		while(current != startNode)
		{
			path.add(current);
			current = current.parent();
		}
		Collections.reverse(path);
		
		return path;
	}

	public List<Node> path()
	{
		return path;
	}
	
}
