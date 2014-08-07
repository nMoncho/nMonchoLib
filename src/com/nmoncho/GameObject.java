package com.nmoncho;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.nmoncho.graphics.CompositeSpriteMap;
import com.nmoncho.graphics.Graphic;
import com.nmoncho.graphics.SpriteMap;

public class GameObject {

	public static final float TRANSPARENT = 0f;
	public static final float OPAQUE = 1f;
	
	public SpriteBatch batch;
	
	// GRAPHICAL VALUES, SHOUDN'T BE IN HERE, BUT TextureRegion & Texture haven't what I need 
	public boolean flipped;
	public float animStateTime = 0.0f;
	public float alpha = 1f;
	public Color tinting;
	public boolean flashing;
	public float flashSpeedFactor = 1;
	private float sinFlashing;
	
	/** Backing graphical object */
	public Object graphic;
	
	/** Flashing Backing graphical object */
	public Object flashingGraphic;
	
	/** If the Entity should be updated. */
	public Boolean active = true;
	
	/** If the Entity should render. */
	public Boolean visible = true;
		
	/** X position of the Entity in the World. */
	public float x = 0;
	
	/** Y position of the Entity in the World. */
	public float y = 0;
	
	/** X offset of the Entity's hitbox. */
	public float xHitbox;
	
	/** Y offset of the Entity's hitbox. */
	public float yHitbox;
	
	/** Width of the Entity's hitbox. */
	public float width;
	
	/** Height of the Entity's hitbox. */
	public float height;
	
	public World world;
	
	public Rectangle hitbox = new Rectangle();
	
	public GameObject() {
		
	}
	
	public GameObject(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Override this, called when the Entity is added to a World.
	 */
	public void added()	{
		
	}
	
	/**
	 * Override this, called when the Entity is removed from a World.
	 */
	public void removed() {
		
	}
	
	/**
	 * Updates and renders the Entity. Not supposed to be overriden
	 */
	public void updateAndRender(float delta, Camera camera) {
		//hitbox.set(x, y, width, height);// could improve mutation (w/ immutability)
		updateHitbox();
		if (active) {
			update(delta);
		}
		render(delta, camera);
	}
	
	/**
	 * Updates but don't renders the Entity. Not supposed to be overriden
	 */
	public void updateWORender(float delta) {
		updateHitbox();
		if (active) {
			update(delta);
		}
	}
	
	public void updateHitbox() {
		if (hitbox != null) {
			hitbox.setPosition(x + xHitbox, y + yHitbox);
		}
	}
	
	public void render(float delta, Camera camera) {
		if (graphic == null || !visible) {
			return;
		}
		
		animStateTime += delta;
		if (alpha < OPAQUE) {
			Color color = batch.getColor();
			float oldAlpha = color.a;
			color.a = oldAlpha * alpha;
			batch.setColor(color);
			renderGraphic(graphic, delta, camera);
	        color.a = oldAlpha;
	        batch.setColor(color);
		} else if (tinting != null) {
			Color prev = batch.getColor();
			batch.setColor(tinting);
			renderGraphic(graphic, delta, camera);
			batch.setColor(prev);
		} else {
			renderGraphic(graphic, delta, camera);
		}
		
		if (flashing && flashingGraphic != null) {
			sinFlashing += delta;
			sinFlashing %= MathUtils.PI /2;
			Color color = batch.getColor();
			float oldAlpha = color.a;
			color.a = Math.abs(MathUtils.cos(sinFlashing) * flashSpeedFactor);
			batch.setColor(color);
			renderGraphic(flashingGraphic, delta, camera);
	        color.a = oldAlpha;
	        batch.setColor(color);
		}
	}
	
	private void renderGraphic(Object obj, float delta, Camera camera) {
		Vector2 point = new Vector2(x, y);
		if (obj instanceof Animation) {// animStateTime managed outside this function for sync
			renderTextureRegion(((Animation) obj).getKeyFrame(animStateTime), camera);
		} else if (obj instanceof TextureRegion) {
			renderTextureRegion((TextureRegion) obj, camera);
		} else if (obj instanceof Texture) {
			renderTexture((Texture) obj, camera);
		} else if (obj instanceof Sprite) {
			renderSprite((Sprite) obj);
		} else if (obj instanceof SpriteMap) {
			renderTextureRegion(((SpriteMap) obj).getCurrentFrame(delta), camera);
		} else if (obj instanceof CompositeSpriteMap) {
			TextureRegion[] frames = ((CompositeSpriteMap) obj).getCurrentFrame(delta);
			for (TextureRegion frame : frames) {
				renderTextureRegion(frame, camera);
			}
		} else if (obj instanceof Graphic) {
			((Graphic) obj).render(batch, delta, point, camera);
		} else if (obj instanceof Array) {
			Array<Object> graphicList = (Array) obj;
			for (Object objB : graphicList) {
				renderGraphic(objB, delta, camera);
			}
		}
	}
	
	protected void renderTextureRegion(TextureRegion textureRegion, Camera camera) {
		if (flipped != textureRegion.isFlipX()) {
			textureRegion.flip(true, false);
		}
		batch.draw(textureRegion, x, y);
	}
	
	protected void renderTexture(Texture texture, Camera camera) {// cant flip??
		batch.draw(texture, x, y);
	}
	
	protected void renderSprite(Sprite sprite) {
		sprite.setPosition(x, y);
		if (flipped != sprite.isFlipX()) {
			sprite.flip(true, false);
		}
		sprite.draw(batch);
	}
	
	/**
	 * Updates the Entity. Override to add behaviour 
	 */
	public void update(float delta) {
		
	}
	
	public float getHalfWidth() {
		return width / 2;
	}
	
	public float getHalfHeight() {
		return height / 2;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		hitbox.setPosition(x + xHitbox, y + yHitbox);
	}
	
	public void setPosition(Vector2 vect) {
		setPosition(vect.x, vect.y);
	}
	
	public Vector2 getPosition() {
		return new Vector2(x, y);// TODO podria mejorarlo usando una var temp
	}
	
	public void setHitbox(float w, float h) {
		hitbox.set(x + xHitbox, y + yHitbox, w, h);
	}

	public void setHitbox(float w, float h, float xOffset, float yOffset) {
		xHitbox = xOffset;
		yHitbox = yOffset;
		setHitbox(w, h);
	}
	
	public boolean collides(float x, float y) {
		return hitbox != null && hitbox.contains(x, y);
	}
	
	public boolean collides(GameObject other) {
		return hitbox != null && other.hitbox != null ?
				hitbox.overlaps(other.hitbox) : false;
	}
	
	public float distance(GameObject other) {
		Vector2 o = new Vector2(this.x, this.y);
		return o.dst(other.x, other.y);
	}
}
