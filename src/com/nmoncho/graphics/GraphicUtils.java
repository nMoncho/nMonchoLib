package com.nmoncho.graphics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class GraphicUtils {

	public static Rectangle calculateViewport(Camera cam, Rectangle viewport) {
		Vector3 vect = new Vector3(0, 0, 0);
		cam.unproject(vect);
		Rectangle rect = viewport != null ? viewport : new Rectangle();
		rect.set(vect.x, vect.y - cam.viewportHeight
				, cam.viewportWidth, cam.viewportHeight);
		//System.out.println("view: "+rect.x+", "+rect.y);
		return rect;
	}
}
