package com.nmoncho.events;

/**
 * Defines the contract that all Events must comply.
 * @author nMoncho
 *
 */
public interface IEvent {

	/**
	 * Returns the event name, for the same class can be multiple name 
	 * (ie MouseEvent, click, move, hover, etc.)
	 * @return event name
	 */
	String getName();
	
	/**
	 * Returns the target of this event (the object who fired this event).
	 * @return Creator Object
	 */
	Object getTarget();
}
