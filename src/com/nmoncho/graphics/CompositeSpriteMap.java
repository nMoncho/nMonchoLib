package com.nmoncho.graphics;

import java.util.Collection;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class CompositeSpriteMap {

	private float currentAnimState = 0.0f;
	private Collection<Animation> currentAnim;
	private String currentAnimName;

	private Multimap<String, Animation> anims;
	public IAnimationListener callback;
	private TextureRegion[][] frames;
	/** Temp array for providing the TextureRegion frames tor render */
	private TextureRegion[] currentFrames;
	
	public CompositeSpriteMap(int spriteWidth, int spriteHeight, Texture... spritesheets) {
		anims = HashMultimap.create();
		frames = new TextureRegion[spritesheets.length][];
		currentFrames = new TextureRegion[spritesheets.length];
		for (int i = 0; i < spritesheets.length; i++) {
			frames[i] = Graphic.splitSpritesheet(spritesheets[i], spriteWidth, spriteHeight);
		}
	}
	
	public Collection<Animation> add(String name, int[] frames, float framesPerSecond) {
		return add(name, frames, framesPerSecond, PlayMode.LOOP);
	}
	
	public Collection<Animation> add(String name, int[] frames, float framesPerSecond, PlayMode playMode) {
		Animation anim;
		float frameRate = 1f / framesPerSecond;
		TextureRegion[] texts;
		for (int sp = 0; sp < this.frames.length; sp++) {
			texts = new TextureRegion[frames.length];
			for (int i = 0; i < frames.length; i++) {
				texts[i] = this.frames[sp][frames[i]];
			}
			anim = new Animation(frameRate, texts);
			anim.setPlayMode(playMode);
			anims.put(name, anim);
		}
		
		return anims.get(name);
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
	
	public Collection<Animation> play(String name) {
		return play(name, false);
	}
	
	public Collection<Animation> play(String name, boolean reset) {
		if (currentAnimName != null && currentAnimName.equals(name) && !reset) {
			return currentAnim;
		}
		currentAnimState = 0.0f;
		currentAnimName = name;
		currentAnim = anims.get(name);
		
		return currentAnim;
	}
	
	public TextureRegion[] getCurrentFrame(float delta) {
		if (currentAnim != null) {
			currentAnimState += delta;
			int i = 0;
			boolean isAnimationEnded = false;
			for (Animation anim : currentAnim) {
				currentFrames[i] = anim.getKeyFrame(currentAnimState);
				isAnimationEnded = anim.isAnimationFinished(currentAnimState);
				i++;
			}

			if (isAnimationEnded) {
				if (callback != null) {
					callback.onAnimationEnd(currentAnimName, null);// TODO fix posible bug 
				}
				currentAnimState = 0;
			}
			
			return currentFrames;
		}
		
		return null;
	}

}
