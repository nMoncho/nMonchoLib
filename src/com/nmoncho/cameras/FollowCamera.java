package com.nmoncho.cameras;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.nmoncho.GameObject;
/**
 * Follow Camera behavior, it follow a GameObject with the camera (ie the player).
 * The following behavior can be bounded to a rectangle.
 * @author nMoncho
 *
 */
public class FollowCamera {

	private GameObject gameObj;
	private Camera camera;
	private float lerpFactor = 5;
	
	private boolean boundCamera;
	private Rectangle boundRectangle;
	
	private Vector3 objTmp = new Vector3();
	
	public FollowCamera(GameObject obj, Camera camera) {
		this.gameObj = obj;
		this.camera = camera;
		this.camera.position.set(gameObj.getPosition(), 0);
		this.boundCamera = false;
	}
	
	public FollowCamera(GameObject obj, Camera camera, Rectangle boundTo) {
		this.gameObj = obj;
		this.camera = camera;
		this.camera.position.set(gameObj.getPosition(), 0);
		this.boundRectangle = boundTo;
		this.boundCamera = true;
	} 
	
	public void update(float delta) {
		followCamera(delta);
		if (boundCamera) {
			boundCamera();
		}
		
		camera.update();
	}
	
	private void boundCamera() {
		if (camera.position.x - camera.viewportWidth / 2 <= boundRectangle.x) { // bound left
			camera.position.x = (int) camera.viewportWidth / 2;
		} else if (camera.position.x + camera.viewportWidth/ 2 >= 320) { // bound right
			camera.position.x = boundRectangle.width - (int) camera.viewportWidth / 2;
		}
		
		if (camera.position.y - camera.viewportHeight / 2 <= boundRectangle.y) { // bound bottom
			camera.position.y = (int) camera.viewportHeight / 2;
		} else if (camera.position.y + camera.viewportHeight / 2 >= 320) { // bound up
			camera.position.y = boundRectangle.height - (int) camera.viewportHeight / 2;
		}
	}
	
	private void followCamera(float delta) {
		objTmp.set(gameObj.x, gameObj.y, 0);
		camera.position.lerp(objTmp, delta * lerpFactor);
		camera.position.x = (int) camera.position.x;
		camera.position.y = (int) camera.position.y;
		camera.position.z = (int) camera.position.z;
	}
}
