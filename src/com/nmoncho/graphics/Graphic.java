package com.nmoncho.graphics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class Graphic {
	
	public static final float TRANSPARENT = 0f;
	public static final float OPAQUE = 1f;
	
	/** If the graphic should render. */
	public boolean visible = true;
	
	/** X offset. */
	public float x = 0;
	
	/** Y offset. */
	public float y = 0;
	
	/** If the graphic should be renderer flipped */
	public boolean flipped;
	
	/** alpha value for the graphic */
	public float alpha = 1f;
	
	/** tinting color (if null or white, the image is unaltered) */
	public Color tinting;
	
	/** If the graphic should flash (Flashing works like a sin wave
	 * , blending the flashing graphic with the graphical object)*/
	public boolean flashing;
	
	/** flash frequency (how rapidly flashes) */
	public float flashFrequency = 1;
	
	/** flash current amplitude */
	private float flashCurrentAmplitude;
	
	/** Flashing Backing graphical object */
	public Object flashingGraphic;
	
	private Vector2 tmpPlusOffset = new Vector2();
	
	/**
	 * Renders the graphic to the sprite batch.
	 * @param	point	The position to draw the graphic (adds to the offset).
	 */
	public abstract void render(SpriteBatch batch, float delta, Vector2 point, Camera camera);
	
	/**
	 * Top down method to help rendering the classes from LibGDX, is intended to be used only once on a top
	 * level fashion because it handles alpha, tinting & flashing
	 * @param obj objet to be rendered (Animation, TextureRegion, Texture, Sprite, SpriteMap, Graphic or Array (GraphicList))
	 * @param batch batch to use for rendering
	 * @param delta increment of time (for com.badlogic.gdx.graphics.g2d.Animation should be the current animation state time (added))
	 * @param point translation origin (its added to the graphic offset)
	 * @param camera camera used for rendering
	 */
	protected void renderGraphic(Object obj, SpriteBatch batch, float delta
			, Vector2 point, Camera camera) {
		Vector2 pointPlusOffset = tmpPlusOffset.set(x + point.x, y + point.y);
		float oldAlphaValue = 1;
		Color oldColorValue = null;
		if (alpha < OPAQUE) {
			oldAlphaValue = beginHandleAlpha(batch);
		}
		if (tinting != null) {
			oldColorValue = beginHandleTinting(batch);
		}
		
		if (obj instanceof Animation) {// animStateTime managed outside this function for sync
			renderTextureRegion(((Animation) obj).getKeyFrame(delta, true), batch, pointPlusOffset);
		} else if (obj instanceof TextureRegion) {
			renderTextureRegion((TextureRegion) obj, batch, pointPlusOffset);
		} else if (obj instanceof Texture) {
			renderTexture((Texture) obj, batch, pointPlusOffset);
		} else if (obj instanceof Sprite) {
			renderSprite((Sprite) obj, batch, pointPlusOffset);
		} else if (obj instanceof SpriteMap) {
			renderTextureRegion(((SpriteMap) obj).getCurrentFrame(delta), batch, pointPlusOffset);
		} else if (obj instanceof Graphic) {
			((Graphic) obj).render(batch, delta, pointPlusOffset, camera);
		} else if (obj instanceof Array) {
			Array<Object> graphicList = (Array) obj;
			for (Object objB : graphicList) {
				renderGraphic(objB, batch, delta, point, camera);
			}
		}
		
		if (alpha < OPAQUE) {
			endHandleAlpha(batch, oldAlphaValue);
		}
		
		if (tinting != null) {
			endHandleTinting(batch, oldColorValue);
		}
		
		if (flashing && flashingGraphic != null) {
			renderFlashing(batch, delta, point, camera);
		}
	}
	
	protected float beginHandleAlpha(SpriteBatch batch) {
		Color color = batch.getColor();
		float oldAlpha = color.a;
		color.a = oldAlpha * alpha;
		batch.setColor(color);
		return oldAlpha;
	}
	
	protected void endHandleAlpha(SpriteBatch batch, float oldAlphaValue) {
		Color color = batch.getColor();
		color.a = oldAlphaValue;
        batch.setColor(color);
	}
	
	protected Color beginHandleTinting(SpriteBatch batch) {
		Color prev = batch.getColor();
		batch.setColor(tinting);
		
		return prev;
	}
	
	protected void endHandleTinting(SpriteBatch batch, Color oldColorValue) {
		batch.setColor(oldColorValue);
	}
	
	protected void renderFlashing(SpriteBatch batch, float delta
			, Vector2 point, Camera camera) {
		flashCurrentAmplitude += delta;
		flashCurrentAmplitude %= MathUtils.PI /2;
		Color color = batch.getColor();
		float oldAlpha = color.a;
		color.a = Math.abs(MathUtils.cos(flashCurrentAmplitude) * flashFrequency);
		batch.setColor(color);
		renderGraphic(flashingGraphic, batch, delta, point, camera);
        color.a = oldAlpha;
        batch.setColor(color);
	}
	
	/**
	 * Renders a texture region to the specified position (FLIPPING ENABLED)
	 * @param texture texture to render
	 * @param batch batch to use
	 * @param point position to render (offset should be already added)
	 */
	protected void renderTextureRegion(TextureRegion textureRegion, SpriteBatch batch 
			, Vector2 point) {
		if (flipped != textureRegion.isFlipX()) {
			textureRegion.flip(true, false);
		}
		batch.draw(textureRegion, point.x, point.y);
	}
	
	/**
	 * Renders a texture to the specified position (FLIPPING NOT ENABLED)
	 * @param texture texture to render
	 * @param batch batch to use
	 * @param point position to render (offset should be already added)
	 */
	protected void renderTexture(Texture texture, SpriteBatch batch 
			, Vector2 point) {// cant flip??
		batch.draw(texture, point.x, point.y);
	}
	
	/**
	 * Renders a sprite to the specified position (FLIPPLING ENABLED)
	 * @param sprite sprite to render
	 * @param batch batch to use
	 * @param point position to render (offset should be already added)
	 */
	protected void renderSprite(Sprite sprite, SpriteBatch batch, Vector2 point) {
		sprite.setPosition(point.x, point.y);
		if (flipped != sprite.isFlipX()) {
			sprite.flip(true, false);
		}
		sprite.draw(batch);
	}
	
	public void setOffset(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setOffset(Vector2 offset) {
		this.x = offset.x;
		this.y = offset.y;
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
