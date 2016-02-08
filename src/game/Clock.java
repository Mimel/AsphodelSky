package game;
import com.google.common.collect.ArrayListMultimap;
import item.Item;

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
	private static ArrayListMultimap<Integer, Item> itemEventQueue = ArrayListMultimap.create();
	
	private Clock() {}
	
	public static int getTime() { return time; }
	
	public static void incrementTime(int inc) {
		time += inc;
	}
}
