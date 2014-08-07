package com.nmoncho.entitysystem;

public abstract class Component {

	/** Is this component active? (should be considered for update). */
	public boolean isActive;
	
	/** Owner of this component instance. */
	protected Entity owner;
	
	/** Initialization logic. */
	public void initialize() {
		
	}
	
	/** Clean up (destroy) logic. */
	public void cleanup() {
		
	}
	
	/**
	 * Updates component.
	 * @param delta delta increment for update.
	 */
	public void update(float delta) {
		
	}
}
