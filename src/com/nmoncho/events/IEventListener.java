package com.nmoncho.events;

/**
 * Defines the contract that an Event listener must comply.
 * @author nMoncho
 *
 */
public interface IEventListener<T extends IEvent> {

	/**
	 * Code runed when an event is fired
	 * @param event been fired
	 */
	public void fire(T event);
}
