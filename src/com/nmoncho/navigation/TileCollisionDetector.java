package com.nmoncho.navigation;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.nmoncho.GameObject;

public class TileCollisionDetector implements ICollisionDectector {

	private final int tileWidth, tileHeight, mapWidth, mapHeight;
	private TiledMap map;
	private TiledMapTileLayer[] solidLayers;
	private Vector2[] collPoints;
	
	public TileCollisionDetector(TiledMap map, String[] layers) {
		this.map = map;
		this.solidLayers = new TiledMapTileLayer[layers.length];
		for (int i = 0; i < layers.length; i++) {
			this.solidLayers[i] = (TiledMapTileLayer) map.getLayers().get(layers[i]);
		}
		this.tileWidth = (Integer) map.getProperties().get("tilewidth");
		this.tileHeight = (Integer) map.getProperties().get("tileheight");
		this.mapWidth = (Integer) map.getProperties().get("width");
		this.mapHeight = (Integer) map.getProperties().get("height");
		this.collPoints = new Vector2[4];
		for (int i = 0; i < collPoints.length; i++) {
			this.collPoints[i] = new Vector2();
		}

	}
	
	public boolean collides(GameObject gameObj) {
		Rectangle hitbox = gameObj.hitbox;
		collPoints[0].set(hitbox.x / tileWidth, hitbox.y / tileHeight);
		collPoints[1].set(hitbox.x / tileWidth, (hitbox.y + hitbox.height) / tileHeight);
		collPoints[2].set((hitbox.x + hitbox.width) / tileWidth, hitbox.y / tileHeight);
		collPoints[3].set((hitbox.x + hitbox.width) / tileWidth, (hitbox.y + hitbox.height) / tileHeight);
		
		Cell cell;
		for (TiledMapTileLayer solidLayer : solidLayers) {
			for (Vector2 collPoint : collPoints) {
				cell = solidLayer.getCell((int) collPoint.x, (int) collPoint.y);
				if (cell != null && cell.getTile() != null) {
					if (cell.getTile().getProperties().containsKey("solid")) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Transforms from screen coordinates to map index.
	 * @param x x screen coordinate
	 * @param y y screen coordinate
	 * @return index of the cell.
	 */
	public int toIdx(float x, float y) {
		int cellX = (int) x / tileWidth;
		int cellY = (int) y / tileHeight;
		return cellY * mapWidth + cellX;
	}

}
