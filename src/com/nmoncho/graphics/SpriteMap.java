package com.nmoncho.graphics;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class SpriteMap {

	private float currentAnimState = 0.0f;
	private Animation currentAnim;
	private String currentAnimName;

	private Map<String, Animation> anims;
	public IAnimationListener callback;
	private TextureRegion[] frames;
	
	public SpriteMap(Texture spritesheet, int spriteWidth, int spriteHeight) {
		anims = new HashMap<String, Animation>();
		frames = Graphic.splitSpritesheet(spritesheet, spriteWidth, spriteHeight);
	}
	
	public Animation add(String name, int[] frames, float framesPerSecond) {
		return add(name, frames, framesPerSecond, PlayMode.LOOP);
	}
	
	public Animation add(String name, int[] frames, float framesPerSecond, PlayMode playMode) {
		float frameRate = 1f / framesPerSecond;
		TextureRegion[] texts = new TextureRegion[frames.length];
		for (int i = 0; i < frames.length; i++) {
			texts[i] = this.frames[frames[i]];
		}
		Animation anim = new Animation(frameRate, texts);
		anim.setPlayMode(playMode);
		anims.put(name, anim);
		
		return anim;
	}
	
	public boolean isPlayingAnimation(String animationName) {
		if (animationName != null){
			return animationName.equals(currentAnimName);
		} else {
			return false;
		}
	}
	
	public String getCurrentAnimationName() {
		return currentAnimName;
	}
	
	public Animation play(String name) {
		return play(name, false);
	}
	
	public Animation play(String name, boolean reset) {
		if (currentAnimName != null && currentAnimName.equals(name) && !reset) {
			return currentAnim;
		}
		currentAnimState = 0.0f;
		currentAnimName = name;
		currentAnim = anims.get(currentAnimName);
		
		return currentAnim;
	}
	
	public TextureRegion getCurrentFrame(float delta) {
		if (currentAnim != null) {
			currentAnimState += delta;
			TextureRegion text = currentAnim.getKeyFrame(currentAnimState);
			if (isAnimationFinished(delta)) {
				if (callback != null) {
					callback.onAnimationEnd(currentAnimName, currentAnim);
				}
				//currentAnimState = 0;
			}
			return text;
		}
		
		return null;
	}
	
	private boolean isAnimationFinished(float delta) {
		boolean finished = false;
		switch(currentAnim.getPlayMode()) {
			case NORMAL:
				//va a lanzar true todo el tiempo luego finalizar (quitar el callback)
				finished = currentAnim.isAnimationFinished(currentAnimState);
				break;
			case LOOP:
			case LOOP_PINGPONG:
			case LOOP_REVERSED:
				int currentAnimSpan = (int)(currentAnimState / currentAnim.getAnimationDuration());
				int previousAnimSpan = (int)((currentAnimState - delta) / currentAnim.getAnimationDuration());
				finished = currentAnimSpan != previousAnimSpan;
			default:// No se como tratar los otros caso (cual es el fin de una animacion Random?)	
		}
		return finished;
	}
}
