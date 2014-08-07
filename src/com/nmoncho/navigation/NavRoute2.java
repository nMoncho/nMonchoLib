package com.nmoncho.navigation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;
import com.nmoncho.GameObject;

public class NavRoute2 {

	private static final ShapeRenderer renderer = new ShapeRenderer();
	
	public Array<NavPoint2> navPoints;
	
	public NavRoute2(Array<NavPoint2> navPoints) {
		this.navPoints = navPoints;
	}
	
	public NavRoute2(NavPoint2... navPoints) {
		this.navPoints = new Array<NavPoint2>();
		for (NavPoint2 p : navPoints) {
			this.navPoints.add(p);
		}
	}
	
	/**
	 * Actualiza la ruta de navegacion
	 * @param	navigator entity que esta usando esta ruta de navegacion
	 * @return NavPoint2 a llegar, null ya llego al final
	 */
	public NavPoint2 updateNavigation(GameObject navigator) {
		if (navPoints.size > 0 && navPoints.get(0).reached(navigator)) {
			navPoints.removeIndex(0);//no se si anda como shift
		}
		if (navPoints.size > 0) {	
			return navPoints.get(0);
		} else {
			return null;
		}
	}
	
	public int length() {
		return navPoints.size;
	}
	
	public void draw(Camera camera) {
		Gdx.gl20.glLineWidth(3);
		renderer.setProjectionMatrix(camera.combined);
		renderer.begin(ShapeType.Line);
		NavPoint2 a, b;
		renderer.setColor(1, 0, 1, 1);
		for (int i = 0; i < navPoints.size-1; i++) {
			a = navPoints.get(i);
			b = navPoints.get(i+1);
			renderer.line(a.point.x, a.point.y, b.point.x, b.point.y);
		}
		renderer.end();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (NavPoint2 p : navPoints) {
			sb.append('(').append(p.point.x).append(", ")
			.append(p.point.y).append(") ->");
		}
		
		return sb.toString();
	}
}
