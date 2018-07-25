package item;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * A catalog represents a parallel array between a set of Items and their amounts.
 *
 * A catalog, as a string, is represented by the following format, where "[ITEM NAME]"
 * can be replaced by the exact name of an item, and "[ITEM NUMBER]" is an integer greater
 * than zero.
 * "[ITEM NAME]([ITEM NUMBER]),[ITEM NAME]([ITEM NUMBER]),...,END"
 * @see Item
 */
public class Catalog {
	
	/**
	 * Set of all items present in the catalog, along with the amounts of each item. Duplicates by id cannot be inserted.
	 */
	private final List<Pair<Item, Integer>> catalog;
	
	/**
	 * A catalog may have a focused item; for example, when searching through the inventory,
	 * the focused item would be the one the cursor is over.
	 */
	private int focusedItemIndex;

	/**
	 * Creates an empty catalog.
	 */
	public Catalog() {
		catalog = new ArrayList<>();
		focusedItemIndex = 0;
	}

	/**
	 * Interprets a string as a catalog according to the mentioned format in the class description,
	 * and attempts to load said items in the catalog from a given item library.
	 * @param catalogRepresentation A string, ideally formatted so that it properly represents a catalog.
	 * @param itemMappings A library of items that determines how the names of the items in the
	 *                     catalog are interpreted.
	 */
	public Catalog(String catalogRepresentation, ItemLibrary itemMappings) {
		catalog = new ArrayList<>();
		focusedItemIndex = 0;

		for(String itemCoupling : catalogRepresentation.split(",")) {
			if(itemCoupling.equals("END")) {
				continue;
			}

			String itemName = itemCoupling.substring(0, itemCoupling.indexOf('('));
			String itemAmount = itemCoupling.substring(itemCoupling.indexOf('(') + 1, itemCoupling.indexOf(')'));

			insertItem(itemMappings.getItemByName(itemName), Integer.parseInt(itemAmount));
		}
	}

	/**
	 * Copy constructor for Catalog.
	 * @param old The catalog to copy from.
	 */
	public Catalog(Catalog old) {
		this.catalog = new ArrayList<>();
		this.focusedItemIndex = old.focusedItemIndex;

		for(Pair<Item, Integer> entry : old.catalog) {
			this.insertItem(entry.getValue0(), entry.getValue1());
		}
	}

	public int getFocusIndex() {
		return focusedItemIndex;
	}

	/**
	 * Resets the focus index to zero.
	 */
	public void resetFocusIndex() {
		focusedItemIndex = 0;
	}
	
	/**
	 * Gets the focused Item, without the amount.
	 * @return The focused Item, without the amount.
	 */
	public Item getFocusedItem() {
		if(catalog.get(focusedItemIndex) == null) {
			return null;
		}
		
		return catalog.get(focusedItemIndex).getValue0();
	}
	
	/**
	 * Sets the focused Item to another one, given its offset.
	 * @param offset An integer added to the current focus index to get the returned focused index.
	 * @return True if the index changed, false otherwise.
	 */
	public boolean setFocus(int offset) {
		//If the index plus the offset point to a valid item, then set the index.
		if(focusedItemIndex + offset >= 0 && focusedItemIndex + offset < catalog.size()) {
			focusedItemIndex += offset;
			return true;
		}
		
		return false;
	}
	
	/**
	 * Gets the items in the catalog, without the bound amounts, as a List.
	 * @return A List of items.
	 */
	public List<Item> getItems() {
		List<Item> items = new ArrayList<>();
		
		for(Pair<Item, Integer> inList : catalog) {
			items.add(inList.getValue0());
		}
		
		return items;
	}
	
	/**
	 * Gets the amounts of items in the catalog as a List.
	 * @return A List of amounts.
	 */
	public List<Integer> getAmounts() {
		List<Integer> amts = new ArrayList<>();
		
		for(Pair<Item, Integer> inList : catalog) {
			amts.add(inList.getValue1());
		}
		
		return amts;
	}

	/**
	 * Attempts to insert a number of the same item onto the catalog. If the item already exists, the amounts list changes.
	 * @param i The item to insert.
	 * @param amt The number of items to add.
	 */
	public void insertItem(Item i, int amt) {
		if(amt <= 0) {
			return;
		}

		//Check for duplicates; Item's bound amount increases by AMT if it already exists.
		for(int x = 0; x < catalog.size(); x++) {
			Pair<Item, Integer> entry = catalog.get(x);
			if(entry.getValue0().getId() == i.getId()) {
				catalog.set(x, new Pair<>(entry.getValue0(), entry.getValue1() + amt));
				return;
			}
		}
		
		catalog.add(new Pair<>(i, amt));
	}

	/**
	 * Consumes a number of items from the catalog, reducing the associated amount by the number given.
	 * The amount given must be an integer greater than zero. If the amount is greater
	 * than the actual number associated with the given item, then all copies of the item are
	 * consumed, as if <code>consumeAll()</code> were called.
	 * @param itemToRemove The item to consume from the catalog.
	 * @param amountToConsume The number of items to consume from the catalog.
	 * @return A Pair containing the item consumed and the number of items consumed.
	 */
	public Pair<Item, Integer> consumeItem(Item itemToRemove, int amountToConsume) {
		for(int item = 0; item < catalog.size(); item++) {
			if(catalog.get(item).getValue0().equals(itemToRemove)) {
				int numOfItems = catalog.get(item).getValue1();
				if(numOfItems <= amountToConsume) {
					return catalog.remove(item);
				} else {
					catalog.set(item, catalog.get(item).setAt1(numOfItems - amountToConsume));
					return new Pair<>(catalog.get(item).getValue0(), amountToConsume);
				}
			}
		}

		return null;
	}

	/**
	 * Consumes all instances of an item.
	 * @param itemToConsume The item to consume.
	 * @return The item and amount removed if the item can be found, null otherwise.
	 */
	public Pair<Item, Integer> consumeAll(Item itemToConsume) {
		for(int x = 0; x < catalog.size(); x++) {
			if(catalog.get(x).getValue0().equals(itemToConsume)) {
				return catalog.remove(x);
			}
		}
		
		return null;
	}
	
	/**
	 * Transfers all item/amount pairs from the donator into this catalog, clearing all items from the donator
	 * as a result.
	 * @param donator The catalog to transfer from.
	 */
	public void transferFrom(Catalog donator) {
		for(Pair<Item, Integer> entry : donator.clearCatalog()) {
			insertItem(entry.getValue0(), entry.getValue1());
		}
	}
	
	/**
	 * Removes all items in the catalog, and returns the catalog.
	 * @return The catalog.
	 */
	public List<Pair<Item, Integer>> clearCatalog() {
		List<Pair<Item, Integer>> tempCatalog = new ArrayList<>(catalog);
		
		catalog.clear();
		
		return tempCatalog;
	}
	
	/**
	 * Is the list empty?
	 * @return True if the list is empty, false otherwise.
	 */
	public boolean isEmpty() {
		return catalog.isEmpty();
	}

	@Override
	public String toString() {
		StringBuilder cat = new StringBuilder();
		for(Pair<Item, Integer> entry : catalog) {
			cat.append(entry.getValue0()).append("(").append(entry.getValue1()).append("),");
		}
		cat.append("END");
		return cat.toString();
	}
}
