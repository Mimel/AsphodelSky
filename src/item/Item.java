package item;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import entity.Combatant;
import event.SimpleEvent;

/**
 * An item is an interactable object that exists on a CompositeGrid.
 * 
 * @see Catalog
 * @author Matt Imel
 *
 */
public class Item implements Comparable<Item> {

	/**
	 * Keeps track of the current id number when a new item is created.
	 * Thread-safe, on the off-chance that the individual item catalogs are created concurrently.
	 */
	private static final AtomicInteger AUTO_INCR_ID = new AtomicInteger(0);
	
	/**
	 * The id of the item. Each item's id is unique.
	 */
	private final int id;
	
	/**
	 * The name of the item.
	 */
	protected String name;

	protected ItemType type;
	
	/**
	 * A visual description of the item.
	 */
	protected String descVis;

	/**
	 * A description of the item shown on use.
	 */
	protected String descUse;

	/**
	 * The events to perform upon use.
	 */
	private List<SimpleEvent> useEffects;

	protected Item(String name, ItemType it, String vDesc, String uDesc, String effects) {
		id = AUTO_INCR_ID.getAndIncrement();
		this.name = name;
		this.type = it;
		this.descVis = vDesc;
		this.descUse = uDesc;

		useEffects = new LinkedList<>();
		for(String phrase : effects.split(";")) {
			useEffects.add(SimpleEvent.interpretEvent(phrase));
		}
	}

	/**
	 * Copy constructor used to preserve the id of the item, in order to maintain sameness across identical items.
	 * @param i The item to duplicate.
	 */
	Item(Item i) {
		id = i.getId();
		this.name = i.getName();
		this.type = i.type;
		this.descVis = i.getVisualDescription();
		this.descUse = i.getUseDescription();
		this.useEffects = i.getEffects();
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getVisualDescription() {
		return descVis;
	}

	public String getUseDescription() {
		return descUse;
	}

	private List<SimpleEvent> getEffects() { return useEffects; }

	/**
	 * Uses the item.
	 * @return A set of events that occur after usage.
	 */
	public List<SimpleEvent> use(Combatant target) {
		List<SimpleEvent> eventsDeepCopy = new LinkedList<>();

		for(SimpleEvent ev : useEffects) {
			SimpleEvent temporarilyRevisedEvent = new SimpleEvent(ev);
			temporarilyRevisedEvent.getData().setTargetTo(target);
			eventsDeepCopy.add(temporarilyRevisedEvent);
		}

		return eventsDeepCopy;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}

		if(this.getClass() != obj.getClass()) {
			return false;
		}

		return this.getId() == ((Item)obj).getId();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}

	/**
	 * Compares two items by id.
	 */
	@Override
	public int compareTo(Item i) {
		return Integer.compare(this.getId(), i.getId());
	}
	
	@Override
	public String toString() {
		return name;
	}
}
