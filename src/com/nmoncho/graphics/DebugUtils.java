package com.nmoncho.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.nmoncho.GameObject;
import com.nmoncho.World;

public class DebugUtils {

	public static boolean showFPS, showHitboxs, showLines;
	public static Color colorFPS = new Color(0xffffffff), 
			colorHitboxs = new Color(0xff00ffff), 
			colorLines = new Color(0xff00ffff);
	
	public static ShapeRenderer shapeRenderer = new ShapeRenderer();
	public static BitmapFont debugBF = new BitmapFont();
	
	private static Array<Object[]> lines = new Array<Object[]>(); 
	
	public static void renderFramesPerSecond(float delta, SpriteBatch batch) {
		debugBF.draw(batch, "FPS: "+Gdx.graphics.getFramesPerSecond(), 10, 20);
	}
	
	public static void renderHitboxs(World world, Camera camera) {
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Line);
        for (GameObject entity : world.getEntities()) {
        	if (entity.hitbox != null) {
        		shapeRenderer.setColor(0, 1, 1, 1);
        		shapeRenderer.rect(entity.hitbox.x, entity.hitbox.y, entity.hitbox.width, entity.hitbox.height);
        	}
        }
        shapeRenderer.end();
	}
	
	public static void debug(float delta, World world, Camera camera) {
		if (showFPS) {
			renderFramesPerSecond(delta, world.getSpriteBatch());
		}
		if (showHitboxs) {
			renderHitboxs(world, camera);
		}
		if (showLines) {
			renderLines(camera);
		}
	}
	
	public static void renderLine(float x0, float y0, float x1, float y1) {
		lines.add(new Object[]{new Vector2(x0, y0), new Vector2(x1, y1), null});
	}
	
	public static void renderLine(Vector2 p0, Vector2 p1) {
		lines.add(new Object[]{p0, p1, null});
	}
	
	public static void renderLine(float x0, float y0, float x1, float y1, Color color) {
		lines.add(new Object[]{new Vector2(x0, y0), new Vector2(x1, y1), color});
	}
	
	public static void renderLine(Vector2 p0, Vector2 p1, Color color) {
		lines.add(new Object[]{p0, p1, color});
	}
	
	private static void renderLines(Camera camera) {
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Line);
		Vector2 p0, p1;
        for (Object[] line : lines) {
        	p0 = (Vector2) line[0];
        	p1 = (Vector2) line[1];
        	shapeRenderer.setColor(line[2] != null ? (Color) line[2] : colorLines);
        	
        	shapeRenderer.line(p0, p1);
        }
        lines.clear();
        shapeRenderer.end();
	}
}
