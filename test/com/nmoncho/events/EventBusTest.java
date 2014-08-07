package com.nmoncho.events;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.nmoncho.events.EventBus;
import com.nmoncho.events.IEvent;
import com.nmoncho.events.IEventListener;

public class EventBusTest {

	EventBus bus;
	Integer firedCounter;
	
	@Before
	public void beforeCreateBus() {
		bus = new EventBus();
		firedCounter = 0;
	}
	
	@Test
	public void testRegisterNameListener() {
		FooEventListener listener =  new FooEventListener();
		assertTrue(bus.register(FooEvent.EVENT_NAME, listener));
	}

	@Test
	public void testRegisterClassListener() {
		FooEventListener listener =  new FooEventListener();
		assertTrue(bus.register(FooEvent.EVENT_NAME, FooEvent.class, listener));
	}

	@Test
	public void testRemoveListener() {
		FooEventListener listener =  new FooEventListener();
		assertFalse(bus.remove(listener));
		
		bus.register(FooEvent.EVENT_NAME, listener);
		assertTrue(bus.remove(listener));
	}
	
	class FooEvent implements IEvent {

		public static final String EVENT_NAME = "Foo_event";
		
		@Override
		public String getName() {
			return EVENT_NAME;
		}

		@Override
		public Object getTarget() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

	class FooEventListener implements IEventListener<FooEvent> {

		@Override
		public void fire(FooEvent event) {
			firedCounter++;
			System.out.println("FooEvent fired!!");
		}
		
	}
}
