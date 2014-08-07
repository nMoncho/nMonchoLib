package com.nmoncho.events;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Defines an Event Bus for which event can come through, dispatched
 * @author nMoncho
 *
 */
public class EventBus {

	private ObjectMap<String, Array<IEventListener<? extends IEvent>>> nameListeners = 
			new ObjectMap<String, Array<IEventListener<? extends IEvent>>>();
	
	private ObjectMap<NameClassEventKey, Array<IEventListener<? extends IEvent>>> nameClassListeners =
			new ObjectMap<NameClassEventKey, Array<IEventListener<? extends IEvent>>>();
	
	/**
	 * Fires an event through the bus
	 * @param event fired
	 */
	public void fire(IEvent event) {
		String name = event.getName();
		if (name != null) {
			Array<IEventListener<? extends IEvent>> nameLists = nameListeners.get(name);
			if (nameLists != null && nameLists.size > 0) {
				for (IEventListener listener : nameLists) {
					listener.fire(event);
				}
			}
			
		}
		NameClassEventKey key = new NameClassEventKey(name, event.getClass());
		Array<IEventListener<? extends IEvent>> nameClassLists = nameClassListeners.get(key);
		if (nameClassLists != null && nameClassLists.size > 0) {
			for (IEventListener listener : nameClassLists) {
				listener.fire(event);
			}
		}
	}
	
	/**
	 * Register an Event Listener that should be fired with the event name specified, 
	 * indistinctly of the class of the event. 
	 * @param name name of the event like "click", "double_click", "enemy moved", etc.
	 * @param listener listener to be called when the event is fired
	 * @return true if the listener was registers, false otherwise
	 */
	public boolean register(String name, IEventListener<? extends IEvent> listener) {
		if (name == null || "".equalsIgnoreCase(name)) {
			throw new IllegalArgumentException("Name can't be null or empty.");
		} else if (listener == null) {
			throw new IllegalArgumentException("Listener can't be null.");
		}
		
		Array<IEventListener<? extends IEvent>> listeners =  nameListeners.get(name);
		if (listeners == null) {
			listeners = new Array<IEventListener<? extends IEvent>>();
			nameListeners.put(name, listeners);
		}
		
		listeners.add(listener);
		
		return true;
	}
	
	/**
	 * Register an Event Listener that should be fired with the event name 
	 * and event class specified.
	 * @param name name of the event like "click", "double_click", "enemy moved", etc.
	 * @param clazz class of the event
	 * @param listener listener to be called when the event is fired
	 * @return true if the listener was registers, false otherwise
	 */
	public boolean register(String name, Class<? extends IEvent> clazz
			, IEventListener<? extends IEvent> listener) {
		if (listener == null) {
			throw new IllegalArgumentException("Listener can't be null.");
		} else if (clazz == null) {
			throw new IllegalArgumentException("Clazz can't be null, use name listener instead.");
		}
		
		NameClassEventKey key = new NameClassEventKey(name, clazz);
		Array<IEventListener<? extends IEvent>> listeners =  nameClassListeners.get(key);
		if (listeners == null) {
			listeners = new Array<IEventListener<? extends IEvent>>();
			nameClassListeners.put(key, listeners);
		}
		
		listeners.add(listener);
		
		return true;
	}
	
	/**
	 * Removes a listener of the bus.
	 * @param listener listener to be removed
	 * @return true if removed, false otherwise
	 */
	public boolean remove(IEventListener<? extends IEvent> listener) {
		// TODO not performant enough, brute force... I should have a different structure to do this.
		boolean nameRemoves = false, classRemoves = false;
		for (Array<IEventListener<? extends IEvent>> arr : nameListeners.values()) {
			nameRemoves = arr.removeValue(listener, true);
		}
		
		for (Array<IEventListener<? extends IEvent>> arr : nameClassListeners.values()) {
			classRemoves = arr.removeValue(listener, true);
		}
		
		return nameRemoves || classRemoves;
	}
	
	class NameClassEventKey {
		protected final String name;
		protected final Class<? extends IEvent> clazz;
		
		NameClassEventKey (String name, Class<? extends IEvent> clazz) {
			this.name = name;
			this.clazz = clazz;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((clazz == null) ? 0 : clazz.getCanonicalName().hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			NameClassEventKey other = (NameClassEventKey) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (clazz == null) {
				if (other.clazz != null)
					return false;
			} else if (!clazz.equals(other.clazz))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

		private EventBus getOuterType() {
			return EventBus.this;
		}
		
	}
}
