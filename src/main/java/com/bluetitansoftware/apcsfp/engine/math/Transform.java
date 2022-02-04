package com.bluetitansoftware.apcsfp.engine.math;

import org.joml.Vector2f;

public class Transform
{
	/**
	 * The position of the transform.
	 */
	public Vector2f p;
	
	/**
	 * The rotation of the transform.
	 */
	public float r;
	
	/**
	 * The scale of the transform.
	 */
	public Vector2f s;
	
	public Transform()
	{
		init(new Vector2f(), 0, new Vector2f(1));
	}
	
	public Transform(Vector2f p)
	{
		init(p, 0, new Vector2f(1));	
	}
	
	public Transform(Vector2f p, float r)
	{
		init(p, r, new Vector2f(1));
	}
	
	public Transform(Vector2f p, float r, float s)
	{
		init(p, r, new Vector2f(s));
	}
	
	public Transform(Vector2f p, float r, Vector2f s)
	{
		init(p, r, s);
	}
	
	private void init(Vector2f p, float r, Vector2f s)
	{
		this.p = p;
		this.r = r;
		this.s = s;
	}
	
	/**
	 * @return A copy of "this" transform.
	 */
	public Transform copy()
	{
		return new Transform(new Vector2f(this.p()), this.r(), new Vector2f(this.s()));
	}
	
	/**
	 * Takes in Transform "to" and sets it to "this" transform.
	 * @param to.
	 */
	public void copy(Transform to)
	{
		to.p.set(this.p());
		to.r = this.r();
		to.s.set(this.s());
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == null)
			return false;
		if(!(o instanceof Transform))
			return false;
		
		Transform t = (Transform)o;
		return t.p().equals(this.p()) && t.r() == this.r() && t.s().equals(this.s());
	}
	
	/**
	 * @return The position of the transform.
	 */
	public Vector2f p()
	{
		return p;
	}
	
	/**
	 * @return The rotation of the transform.
	 */
	public float r()
	{
		return r;
	}
	
	/**
	 * @return The scale of the transform.
	 */
	public Vector2f s()
	{
		return s;
	}
	
}
