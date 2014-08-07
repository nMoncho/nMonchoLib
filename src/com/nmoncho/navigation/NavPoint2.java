package com.nmoncho.navigation;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.nmoncho.GameObject;

public class NavPoint2 {

	public static final float MAX_DISTANCE = 10000000;
	public static final float e = 1f;
	
	public Vector2 point;
	
	public NavPoint2(float x, float y) {
		this.point = new Vector2(x, y);
	}
	
	public NavPoint2(Vector2 point) {
		this(point.x, point.y);
	}
	
	public boolean reached(GameObject other) {
		boolean alcanzo = Math.abs(point.x - other.x) < e 
				&& Math.abs(point.y - other.y) < e;
		return alcanzo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((point == null) ? 0 : point.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NavPoint2 other = (NavPoint2) obj;
		if (point == null) {
			if (other.point != null)
				return false;
		} else if (!point.equals(other.point))
			return false;
		return true;
	}

	public boolean epsilonEquals(NavPoint2 other, float epsilon) {
		return point.epsilonEquals(other.point, epsilon);
	}
	
	public float distance(GameObject other){
		return distance(other, new Vector2(0, 0));	
	}
	
	public float distance(GameObject other, Vector2 relativeOrigin) {
		Vector2 comp = new Vector2(other.x, other.y).add(relativeOrigin);
		return point.dst(comp);
	}
	
	public static NavPoint2 minDistance(GameObject other, Array<NavPoint2> points) {
		return minDistance(other, new Vector2(0, 0), points);
	}
	
	public static NavPoint2 minDistance(GameObject other, Vector2 relativeOrigin, Array<NavPoint2> points) {
		float distance;
		float minDistance = MAX_DISTANCE;
		NavPoint2 ptoMin = null;
		
		for (NavPoint2 pto : points) {
			distance = pto.distance(other, relativeOrigin);
			if (distance < minDistance) {
				minDistance = distance;
				ptoMin = pto;
			}
		}
		
		return ptoMin;
	}
	
	@Override
	public String toString() {
		return "NavPoint [point=" + point + "]";
	}
}
