package events;

import java.util.TreeMap;

import item.Item;

/**
 * 
 * @author Xcqtion
 *
 * @param <K> The time when V's die method is executed. All subclasses must have K be an Integer.
 * @param <V> 
 */
public abstract class EventQueue<K, V> extends TreeMap<K, V> {
	private static final long serialVersionUID = 1L;
	
	public EventQueue() {
		super();
	}
	
	//redundant?
	public void addToEventQueue(K endTime, V obj) {
		put(endTime, obj);
	}
}
