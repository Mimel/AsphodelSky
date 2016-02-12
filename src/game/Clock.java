package game;
import org.javatuples.Pair;
import com.google.common.collect.ArrayListMultimap;

import entity.Player;
import item.Item;
import item.ItemEffect;

/**
 * A singular clock used to keep track of game time, as well as store event triggers,
 * when they go off, and what method is called when it is done.
 * @author mri
 */
public final class Clock {
	/**
	 * The current game time, measured in steps. Starts at zero and increments as
	 * the game progresses.
	 */
	private static int time = 0;

	/**
	 * An event queue of triggers; whenever a time in the queue is hit or passed,
	 * the correlating item's event triggers.
	 */
	private static ArrayListMultimap<Integer, Pair<Item, ItemEffect>> itemEventQueue = ArrayListMultimap.create();
	
	private Clock() {}
	
	public static int getTime() { return time; }
	
	/**
	 * Progresses the clock by the given increment.
	 * @param inc The tiem to add to the given time.
	 */
	public static void incrementTime(int inc) {
		time += inc;
	}
	
	/**
	 * Adds an event to the item event queue, as well as its effect and when it
	 * sets off.
	 * @param interval The time after the current time when the event will trigger.
	 * @param i The item whose event will trigger.
	 * @param e The type of event to trigger.
	 */
	public static void addEvent(int interval, Item i, ItemEffect e) {
		itemEventQueue.put(time + interval, new Pair<Item, ItemEffect>(i, e));
	}
	
	public static boolean hasEventsAtTime(int time) {
		return itemEventQueue.containsKey(time);
	}
	
	/**
	 * Does all the events at the time given in order of entry.
	 * @param time The time in which to perform all the events.
	 * @param p1 The Player.
	 * @return All the messages of each event, delimited by `.
	 */
	public static String performEventsAtTime(int time, Player p1) {
		String events = "";
		for(Pair<Item, ItemEffect> p : itemEventQueue.get(time)) {
			if(p.getValue1() == ItemEffect.USE) { 
				events += p.getValue0().use(p1);
			} else if(p.getValue1() == ItemEffect.STEP) {
				events += p.getValue0().step(p1);
			} else if(p.getValue1() == ItemEffect.DIE) {
				events += p.getValue0().die(p1);
			}
			events += "`";
		}
		return events;
	}
	
	/**
	 * Removes all events at the given time.
	 * Usually happens after a performEventsAtTime() call.
	 * @param time The time to remove all events.
	 */
	public static void removeAllEventsAtTime(int time) {
		itemEventQueue.removeAll(time);
	}
}
