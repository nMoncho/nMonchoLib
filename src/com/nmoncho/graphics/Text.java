package com.nmoncho.graphics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Text extends Graphic {

	private BitmapFont font;
	public String text;
	
	public Text() {
		this(new BitmapFont());
		font.setColor(0, 0, 0, 1);
	}
	
	public Text(BitmapFont font) {
		this.font = font;
		text = "";
	}

	public Text(String text) {
		this();
		this.text = text;
	}
	
	public Text(BitmapFont font, String text, float x, float y) {
		this.font = font;
		this.text = text;
		this.x = x;
		this.y = y;
	}

	@Override
	public void render(SpriteBatch batch, float delta, Vector2 point, Camera camera) {
		font.draw(batch, text, x + point.x, y + point.y);
	}

}
