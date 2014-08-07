package com.nmoncho.graphics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.Multimap;
import com.google.common.collect.HashMultimap;

public class Anim extends Graphic {
	
	private float currentAnimState = 0.0f;
	private Animation currentAnim;
	private String currentAnimName;

	private Multimap<String, Animation> anims;
	public IAnimationListener callback;
	private TextureRegion[] frames;
	
	/** Anchor expressed in percentage (0 <= a <= 1), though it could be > 1 || < 0 */
	private Vector2 anchor = new Vector2();
	
	public Anim(Texture spritesheet, int spriteWidth, int spriteHeight) {
		anims = HashMultimap.create();
		frames = splitSpritesheet(spritesheet, spriteWidth, spriteHeight);
	}
	
	@Override
	public void render(SpriteBatch batch, float delta, Vector2 point, Camera camera) {
		// TODO Auto-generated method stub
	}

	public Vector2 getAnchor() {
		return anchor;
	}
	
	public void setAnchor(Vector2 anchor) {
		this.anchor = anchor;
	}
	
	public void setAnchor(float x, float y) {
		this.anchor.set(x, y);
	}
	
	protected static TextureRegion[] splitSpritesheet(Texture spritesheet, int spriteWidth, int spriteHeight) {
		TextureRegion[][] tmp = TextureRegion.split(spritesheet, spriteWidth, spriteHeight);
		int rows = spritesheet.getHeight() / spriteHeight;
		int columns = spritesheet.getWidth() / spriteWidth;
		TextureRegion[] totalFrames = new TextureRegion[rows * columns];
		for (int i = 0,k = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++, k++) {
				totalFrames[k] = tmp[i][j];
			}
		}

		return totalFrames;
	}
}
