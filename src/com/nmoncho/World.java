package com.nmoncho;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class World {

	private Array<GameObject> entities;
	private SpriteBatch batch;
	
	public World(SpriteBatch batch) {
		entities = new Array<GameObject>();
		this.batch = batch;
	}
	
	public void add(GameObject entity) {
		entities.add(entity);
		entity.world = this;
		entity.batch = batch;
		entity.added();
	}
	
	public boolean remove(GameObject entity) {
		return entities.removeValue(entity, true);
	}
	
	public void removeAll() {
		entities.clear();
	}
	
	/**
	 * Actualiza el mundo, la idea es que sea llamado desde un screen o un application listener (o Game)
	 * @param delta tiempo transcurrido desde el ultimo cuadro
	 * @param camera camara que se esta utilizando
	 */
	public void update(float delta, Camera camera) {
		for (GameObject entity : entities) {
			entity.updateAndRender(delta, camera);
		}
	}

	public Array<GameObject> getEntities() {
		return entities;
	}

	public <T> int countClassEntities(Class<T> clazz) {
		int count = 0;
		for (GameObject entity : entities) {
			if (clazz.isAssignableFrom(entity.getClass())) {
				count++;
			}
		}
		return count;
	}
	
	@SuppressWarnings("unchecked")
	public <T> Array<T> getClassEntities(Class<? extends GameObject> clazz) {
		Array<T> classEntities = new Array<T>();
		for (GameObject entity : entities) {
			if (clazz.isAssignableFrom(entity.getClass())) {
				classEntities.add((T) entity);
			}
		}
		return classEntities;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getFirstEntity(Class<? extends GameObject> clazz) {
		Array.ArrayIterator<GameObject> it = new Array.ArrayIterator<GameObject>(entities);
		GameObject en;
		while (it.hasNext()) {
			en = it.next();
			if (clazz.isAssignableFrom(en.getClass())) {
				return (T) en;
			}
		}
		
		return null;
	}
	
	public SpriteBatch getSpriteBatch() {
		return batch;
	}
}
