package com.nmoncho.graphics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Image extends Graphic {

	private Object backingGraphic;
	
	public Image() {}
	
	public Image(Texture texture) {
		this.backingGraphic = texture;
	}
	
	public Image(TextureRegion region) {
		this.backingGraphic = region;
	}
	
	@Override
	public void render(SpriteBatch batch, float delta, Vector2 point,
			Camera camera) {
		renderGraphic(backingGraphic, batch, delta, point, camera);
	}

}
