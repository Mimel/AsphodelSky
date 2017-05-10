package item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.javatuples.Pair;

/**
 * A catalog represents a parallel array between a set of Items and their amounts.
 * @see Item
 *
 */
public class Catalog {
	
	/**
	 * Set of all items present in the catalog, along with the amounts of each item. Duplicates by id cannot be inserted.
	 */
	private List<Pair<Item, Integer>> catalog;
	
	/**
	 * A catalog may have a focused item; for example, when searching through the inventory,
	 * the focused item would be the one the cursor is over.
	 */
	private int focusedItemIndex;
	
	public Catalog() {
		catalog = new ArrayList<Pair<Item, Integer>>();
		focusedItemIndex = 0;
	}
	
	//TODO: consider one get method instead of two
	
	public int getFocusIndex() {
		return focusedItemIndex;
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
		List<Item> items = new ArrayList<Item>();
		
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
		List<Integer> amts = new ArrayList<Integer>();
		
		for(Pair<Item, Integer> inList : catalog) {
			amts.add(inList.getValue1());
		}
		
		return amts;
	}
	
	/**
	 * Attempts to insert an item onto the catalog. If the item exists, the amounts list does NOT change.
	 * @param i The item to insert.
	 */
	public void insertItem(Item i) {
		
		//Check for duplicates; Item does not get inserted if there is a duplicate.
		for(Pair<Item, Integer> inList : catalog) {
			if(inList.getValue0().getId() == i.getId()) {
				return;
			}
		}
		
		catalog.add(new Pair<Item, Integer>(i, 1));
	}
	
	/**
	 * Attempts to insert a number of the same item onto the catalog. If the item already exists, the amounts list changes.
	 * @param i The item to insert.
	 * @param amt The number of items to add.
	 */
	public void insertItem(Item i, int amt) {
		//Check for duplicates; Item's bound amount increases by AMT if it already exists.
		for(int x = 0; x < catalog.size(); x++) {
			Pair<Item, Integer> entry = catalog.get(x);
			if(entry.getValue0().getId() == i.getId()) {
				catalog.set(x, new Pair<Item, Integer>(entry.getValue0(), entry.getValue1() + amt));
				return;
			}
		}
		
		catalog.add(new Pair<Item, Integer>(i, amt));
	}
	
	/**
	 * Consumes an item, reducing its associated amount by one. If this brings the item's amount to zero, the item and it's associated amount are both removed.
	 * @param ID The Item id.
	 * @return The item removed if the item can be found in the catalog, null otherwise.
	 */
	public Item consumeItem(int itemID) {
		for(int x = 0; x < catalog.size(); x++) {
			if(catalog.get(x).getValue0().getId() == itemID) {
				if(catalog.get(x).getValue1() == 1) {
					return catalog.remove(x).getValue0();
				} else {
					catalog.set(x, new Pair<Item, Integer>(catalog.get(x).getValue0(), catalog.get(x).getValue1() - 1));
					return catalog.get(x).getValue0();
				}	
			}
		}
		
		return null;
	}
	
	/**
	 * Consumes all instances of an item.
	 * @param itemID The Item id.
	 * @return The item and amount removed if the item can be found, null otherwise.
	 */
	public Pair<Item, Integer> consumeAll(int itemID) {
		for(int x = 0; x < catalog.size(); x++) {
			if(catalog.get(x).getValue0().getId() == itemID) {
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
		List<Pair<Item, Integer>> tempCatalog = new ArrayList<Pair<Item, Integer>>(catalog);
		
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
	
	/**
	 * Sorts the given catalog by id in ascending order.
	 * @param catalog The catalog.
	 * @return A sorted list.
	 */
	public void sortCatalogById() {
		Collections.sort(catalog);
	}
	
	/**
	 * Gets the entirety of all items in the game, with amounts set to one.
	 * TODO: A bit obtrusive, since the method must be updated every time a new class is created.
	 * @return
	 */
	public static List<Item> getEntireItemCatalog() {
		return Arrays.asList(mergeCatalogs(Vial.CATALOG_VIAL));
	}
	
	/**
	 * Merges a number of catalogs together.
	 * @param items The catalogs to merge. Null-safe.
	 * @return The merged catalog.
	 */
	private static Item[] mergeCatalogs(Item[]... catalogs) {
		Item[] combinedCatalog;
		int ccSum = 0;
		
		for(Item[] catalog : catalogs) {
			ccSum += catalog.length;
		}
		
		combinedCatalog = new Item[ccSum];
		
		for(Item[] catalog : catalogs) {
			for(Item item : catalog) {
				combinedCatalog[--ccSum] = item;
			}
		}
		
		return combinedCatalog;
	}
	
	@Override
	public String toString() {
		String cat = "";
		for(Pair<Item, Integer> entry : catalog) {
			cat += entry.getValue0() + " (" + entry.getValue1() + "),\n";
		}
		cat +="END";
		return cat;
	}
}
