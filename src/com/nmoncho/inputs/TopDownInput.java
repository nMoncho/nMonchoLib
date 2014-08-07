package com.nmoncho.inputs;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.nmoncho.GameObject;
import com.nmoncho.graphics.GraphicUtils;
import com.nmoncho.navigation.ICollisionDectector;

/**
 * Clase que sirve como {@link InputProcessor} para juegos top-down.
 * Esta asociada a un {@link GameObject} que es al que va a aplicar la entrada.
 * 
 * @author nMoncho
 *
 */
public class TopDownInput implements InputProcessor {

	private static final float E = 0.1f;
	
	private boolean upPressed, downPressed, leftPressed, rightPressed;
	private float maxSpeed;
	
	private Vector2 speed = new Vector2(), accel = new Vector2();
	
	private Vector3 tmp = new Vector3();
	
	/** GameObject al que se aplica la entrada*/
	private GameObject gameObj;
	/** Detector de Colisiones, en caso de ser null lo ignora*/
	public ICollisionDectector collDectector;
	
	public Camera cam;
	
	/**
	 * Construye un TopDownInput.
	 * @param obj GameObj al que se aplica la entrada
	 * @param maxSpeed maxima velocidad a que puede alcanzar (px/seg)
	 */
	public TopDownInput(GameObject obj, float maxSpeed) {
		this.gameObj =  obj;
		this.maxSpeed = maxSpeed;
	}
	
	public Vector2 processInput(float delta) {
		float temp = 0;
		float x = 0, y = 0;
		speed.set(x, y);
		if (upPressed || downPressed) {
			y = (upPressed ? 1 : -1) * maxSpeed;
			accel.y += (upPressed ? 1 : -1) * maxSpeed * delta;
			accel.y = Math.min(accel.y, maxSpeed);
		} else if (Math.abs(accel.y) > E) {
			temp = accel.y;
			accel.y += (accel.y > 0 ? -1 : 1) * maxSpeed * delta;
			accel.y = Math.signum(accel.y) != Math.signum(temp) ? 0 : accel.y;
		} else {
			accel.y = 0;
		}
		
		if (leftPressed || rightPressed) {
			x = (rightPressed ? 1 : -1) * maxSpeed;
			accel.x += (rightPressed ? 1 : -1) * maxSpeed * delta;
			accel.x = Math.min(accel.x, maxSpeed);
		} else if (Math.abs(accel.x) > E) {
			temp = accel.x;
			accel.x += (accel.x > 0 ? -1 : 1) * maxSpeed * delta;
			accel.x = Math.signum(accel.x) != Math.signum(temp) ? 0 : accel.x;
		} else {
			accel.x = 0;
		}
		speed.set(accel.x, accel.y);
		
		gameObj.x += speed.x * delta;
		gameObj.y += speed.y * delta;
		gameObj.updateHitbox();
		if (collDectector != null 
				&& collDectector.collides(gameObj)) {
			gameObj.x -= speed.x * delta;
			gameObj.y -= speed.y * delta;
			accel.set(0, 0);
		}
		
		return speed.cpy();
	}
	
	@Override
	public boolean keyDown(int keyCode) {
		switch (keyCode) {
			case Keys.UP:
			case Keys.W:
				upPressed = true;
				break;
			case Keys.DOWN:
			case Keys.S:
				downPressed = true;
				break;
			case Keys.LEFT:
			case Keys.A:
				leftPressed = true;
				break;
			case Keys.RIGHT:
			case Keys.D:
				rightPressed = true;
				break;
			
		}
		return true;
	}

	@Override
	public boolean keyUp(int keyCode) {
		switch (keyCode) {
			case Keys.UP:
			case Keys.W:
				upPressed = false;
				break;
			case Keys.DOWN:
			case Keys.S:
				downPressed = false;
				break;
			case Keys.LEFT:
			case Keys.A:
				leftPressed = false;
				break;
			case Keys.RIGHT:
			case Keys.D:
				rightPressed = false;
				break;
			
		}
		return true;
	}
	
	@Override
	public boolean keyTyped(char arg0) {
		return false;
	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		return false;
	}
	
	private void processTouchInput(int screenX, int screenY) {
		tmp.set(screenX, screenY, 0);
		cam.unproject(tmp);
		Rectangle viewport = GraphicUtils.calculateViewport(cam, null);
		// Would be better if used Center (less calcs)
		final float slopeA = (gameObj.y - viewport.y) / (gameObj.x - viewport.x),
				slopeB = ((gameObj.y + gameObj.height) 
						- (viewport.y + viewport.height)) / (gameObj.x - viewport.x);
		final float coordAY = slopeA * (tmp.x - viewport.x) + viewport.y
				, coordBY = slopeB * (tmp.x - viewport.x) + (viewport.y + viewport.height);
		
		if (tmp.y >= coordAY) { // UP or LEFT
			if (tmp.y >= coordBY) { // UP
				upPressed = true;
			} else { // LEFT
				leftPressed = true;
			}
		} else {// DOWN or RIGHT
			if (tmp.y >= coordBY) { // RIGHT
				rightPressed = true;
			} else { // DOWN
				downPressed = true;
			}
		}
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (pointer == 0) {
			processTouchInput(screenX, screenY);
			
			return true;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (pointer == 0) {
			upPressed = downPressed = leftPressed = rightPressed = false;
			processTouchInput(screenX, screenY);
			return true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (pointer == 0) {
			upPressed = downPressed = leftPressed = rightPressed = false;
			return true;
		}
		return false;
	}

}
