package com.nmoncho.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animator {

	public static Animation getAnimation(Texture spritesheet, int rows, int columns
			, float framesPerSecond, int[] frameIndices) {
		TextureRegion[][] tmp = TextureRegion.split(spritesheet, spritesheet.getWidth() / 
				columns, spritesheet.getHeight() / rows);
		TextureRegion[] totalFrames = new TextureRegion[rows * columns];
		TextureRegion[] frames = new TextureRegion[frameIndices.length];
		for (int i = 0,k = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++, k++) {
				totalFrames[k] = tmp[i][j];
			}
		}
		
		for (int i = 0, k = 0; i < rows * columns; i++) {
			for (int j = 0; j < frameIndices.length; j++) {
				if (i == frameIndices[j]) {
					frames[k] = totalFrames[i];
					k++;
				}
			}
		}
		float frameRate = 1f / framesPerSecond;
		return new Animation(frameRate, frames);
	}
}
