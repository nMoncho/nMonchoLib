package com.nmoncho.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.nmoncho.GameObject;

public class ImageButton extends GameObject {
	
	public static final String CENTER_ORIENTATION = "label_center_orientation";
	public static final String UP_ORIENTATION = "label_up_orientation";
	public static final String DOWN_ORIENTATION = "label_down_orientation";
	public static final String LEFT_ORIENTATION = "label_left_orientation";
	public static final String RIGHT_ORIENTATION = "label_right_orientation";
	public static final String ABSOLUTE_ORIENTATION = "label_absolute_orientation";
	private static final float LABEL_OFFSET = 8;
	private static final float BUTTON_CLICK_OFFSET = 1;
	
	protected final int NORMAL = 1;
	protected final int HOVER = 2;
	protected final int DOWN = 3;
	
	private Array graphicList;
	public Object sprite;
	public Text label;
	private int state = NORMAL;
	private String labelOrientation;
	
	private Array<IButtonEventListener> listeners;
	
	public ImageButton(Object buttonGraphic, String label, String labelOrientation) {
		this.sprite = buttonGraphic;
		calculateHitbox(buttonGraphic);
		this.labelOrientation = labelOrientation;
		this.label = new Text(new BitmapFont());
		this.label.text = label;
		this.listeners = new Array<IButtonEventListener>();
		Vector2 point = calculateLabelCoordinates();
		this.label.x = point.x;
		this.label.y = point.y;
		graphicList = new Array();
		graphicList.add(this.sprite);
		graphicList.add(this.label);
		
		graphic = graphicList;
	}
	
	private void calculateHitbox(Object buttonGraphic) {
		if (buttonGraphic instanceof Texture) {
			width = ((Texture) buttonGraphic).getWidth();
			height = ((Texture) buttonGraphic).getHeight();
			setHitbox(width, height);
		} else if (buttonGraphic instanceof TextureRegion) {
			width = ((TextureRegion) buttonGraphic).getRegionWidth();
			height = ((TextureRegion) buttonGraphic).getRegionHeight();
			setHitbox(width, height);
		}
	}
	
	@Override
	public void update(float delta) {
		if (clicked()) {
			if (state != DOWN) {
				x += BUTTON_CLICK_OFFSET;
				y += BUTTON_CLICK_OFFSET;
				fireTouchDown(Gdx.input.getX(), Gdx.input.getY());
			}
			state = DOWN;
		} else if (!clicked() && state != NORMAL) {
			if (state == DOWN) {
				x -= BUTTON_CLICK_OFFSET;
				y -= BUTTON_CLICK_OFFSET;
				fireTouchUp(Gdx.input.getX(), Gdx.input.getY());
			}
			state = NORMAL;
		}
	}
	
	private boolean clicked() {
		if (Gdx.input.justTouched()){
			return hitbox.contains(Gdx.input.getX(), Gdx.input.getY());
		}
		return false;
	}
	
	private Vector2 calculateLabelCoordinates() {
		Vector2 textMidpoint = new Vector2((label.text.length() * 8) / 2, 16);
		Vector2 spriteMidpoint = new Vector2(width / 2, height / 2);
		if (UP_ORIENTATION.equals(labelOrientation)) {
			return new Vector2(spriteMidpoint.x - textMidpoint.x, -LABEL_OFFSET - 16);
		} else if (DOWN_ORIENTATION.equals(labelOrientation)) {
			return new Vector2(spriteMidpoint.x - textMidpoint.x, height + LABEL_OFFSET);
		} else if (CENTER_ORIENTATION.equals(labelOrientation)) {
			return new Vector2(spriteMidpoint.x - textMidpoint.x, spriteMidpoint.y - textMidpoint.y);
		} else {
			return new Vector2();
		}
	}
	
	public void addListener(IButtonEventListener listener) {
		listeners.add(listener);
	}
	
	private void fireTouchDown(float x, float y) {
		for (IButtonEventListener listener : listeners) {
			listener.onTouchDown(this, x, y);
		}
	}
	
	private void fireTouchUp(float x, float y) {
		for (IButtonEventListener listener : listeners) {
			listener.onTouchUp(this, x, y);
		}
	}
}
