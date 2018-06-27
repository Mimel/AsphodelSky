package item;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import event.SimpleEvent;

/**
 * A template for creating an item. All final properties are tied to the class the item is in (Say, Vial or Head),
 * while the non-final properties are tied to each individual item.
 * 
 * The ID is an exception to this.
 * 
 * @see Catalog
 * @author Matt Imel
 *
 */
public class Item implements Comparable<Item> {

	/**
	 * Map that maps the item's name to the item as an object.
	 */
	private static Map<String, Item> itemNameToItemMap;
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
	private Item(Item i) {
		id = i.getId();
		this.name = i.getName();
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

	public List<SimpleEvent> getEffects() { return useEffects; }

	public static void loadItemMapping(String fileName) {
		if(itemNameToItemMap == null) {
			itemNameToItemMap = new HashMap<>();
		}

		try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String name = "";
			ItemType type = ItemType.NONE;
			String vDesc = "";
			String uDesc = "";
			StringBuilder effects = new StringBuilder();

			String line;

			while((line = br.readLine()) != null) {
				if(name.equals("")) {
					int tildeLoc = line.indexOf('~');
					name = line.substring(0, tildeLoc);
					type = ItemType.valueOf(line.substring(tildeLoc + 1).trim());
				} else if(vDesc.equals("")) {
					vDesc = line;
				} else if(uDesc.equals("")) {
					uDesc = line;
				} else if(line.equals("!END")) {
					itemNameToItemMap.put(name, new Item(name, type, vDesc, uDesc, effects.toString()));
					name = "";
					vDesc = "";
					uDesc = "";
					effects = new StringBuilder();
				} else {
					effects.append(line);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static Item getItem(String itemName) {
		return new Item(itemNameToItemMap.get(itemName));
	}

	public static Item getItemById(int id) {
		for(Item i : itemNameToItemMap.values()) {
			if(id == i.getId()) {
				return new Item(i);
			}
		}

		return null;
	}

	/**
	 * Uses the item.
	 * @return A set of events that occur after usage.
	 */
	public List<SimpleEvent> use(int targetID) {
		List<SimpleEvent> eventsDeepCopy = new LinkedList<>();

		for(SimpleEvent ev : useEffects) {
			SimpleEvent temporarilyRevisedEvent = new SimpleEvent(ev).withTargetID(targetID);
			eventsDeepCopy.add(temporarilyRevisedEvent);
		}

		return eventsDeepCopy;
	}
	
	/**
	 * Compares two items by id.
	 */
	@Override
	public int compareTo(Item i) {
		return Integer.compare(this.id, i.id);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
