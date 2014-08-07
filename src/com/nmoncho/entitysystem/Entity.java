package com.nmoncho.entitysystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entity {

	/** Global Unique Identifier. */
	public String guid;
	
	/** Is the entity initialized? (should be initialized). */
	public boolean isInitialized;
	
	/** Is the entity active? (should be updated). */
	public boolean isActive;
	
	/** Current compoments of this entity. */
	private Map<Class<? extends Component>, Component> components;
	
	/** Components to add to this entity. */
	private List<Component> componentsToAdd;
	
	/** Components to remove to this entity. */
	private List<Class<? extends Component>> componentsToRemove;
	
	public Entity() {
		components = new HashMap<Class<? extends Component>, Component>();
		componentsToAdd = new ArrayList<Component>();
		componentsToRemove = new ArrayList<Class<? extends Component>>();
	}
	
	/**
	 * Checks if this entity HAS the specified type component. 
	 * @param componentClass type of component to check.
	 * @return true if this entity has the component, false otherwise.
	 */
	public <T extends Component> boolean has(Class<T> componentClass) {
		return components.containsKey(componentClass);
	}
	
	/**
	 * Gets the component of the class specified
	 * @param componentClass component type to get
	 * @return Component of the class
	 * @throws IllegalArgumentException if the entity hasn't the class specified
	 */
	@SuppressWarnings("unchecked")
	public <T extends Component> T get(Class<T> componentClass) {
		if (!has(componentClass)) {
			throw new IllegalArgumentException("Entity #" + guid 
					+ " doesn't have " + componentClass.getCanonicalName());
		}
		
		return (T) components.get(componentClass);
	}
	
	/**
	 * Adds a component to this entity.
	 * @param component component to add.
	 */
	public void attach(Component component) {
		componentsToAdd.add(component);
	}
	
	/**
	 * Removes a component of type T if it exists.
	 * @param componentClass The class of the component to remove.
	 */
	public <T extends Component> void detach(Class<T> componentClass) {
		if (has(componentClass) && !componentsToRemove.contains(componentClass)) {
			componentsToRemove.add(componentClass);
		}
	}
	
	/**
	 * Replaces a component type instance with the specified instance.
	 * @param component instance to replace the old one.
	 */
	public void replace(Component component) {
		if (has(component.getClass())) {
			detach(component.getClass());
		}
		attach(component);
	}
	
	/**
	 * Makes the entity active.  Initializes attached components.
	 */
	public void initialize() {
		isInitialized = true;
		isActive = true;
		for (Component component : components.values()) {
			component.initialize();
		}
	}
	
	/**
	 * Makes the entity inactive.  Cleans up attached components.
	 */
	public void cleanup() {
		isActive = false;
		for (Component component : components.values()) {
			component.cleanup();
		}
	}
	
	/**
	 * Updates components, removes components to detach, add new components to attach
	 * @param delta increment from last update
	 */
	public void update(float delta) {
		for (Component component : components.values()) {
			if (component.isActive) {
				component.update(delta);
			}
		}
		
		while (!componentsToRemove.isEmpty()) {
			remove(componentsToRemove.remove(0));
		}
		
		while (!componentsToAdd.isEmpty()) {
			add(componentsToAdd.remove(0));
		}
	}
	
	/**
	 * Adds a component to this entity.
	 * @param component component to add.
	 */
	private void add(Component component) {
		if (!has(component.getClass())) {
			throw new IllegalArgumentException("Entity #" + guid 
					+ " has " + component.getClass().getCanonicalName()+ ". Use replace.");
		}
		
		components.put(component.getClass(), component);
		component.owner = this;
		
		if (isInitialized) {
			component.initialize();
		}
	}
	
	/**
	 * Removes component type of this entity.
	 * @param componentClass component type to remove.
	 */
	private <T extends Component> void remove(Class<T> componentClass) {
		if (!has(componentClass)) {
			throw new IllegalArgumentException("Entity #" + guid 
					+ " doesn't have " + componentClass.getCanonicalName());
		}
		components.get(componentClass).cleanup();
		components.remove(componentClass);
	}
}
