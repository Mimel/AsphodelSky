package item;

/**
 * An extension of the Item class meant to be able to be fit in inventories; by adding an amount variable,
 * multiples of the same item can be fit in one inventory element.
 * @author Xcqtion
 */
public class StackableItem {
	private Item i;
	private int amount;
	
	public StackableItem(Item i, int a) {
		this.i = i;
		this.amount = a;
		
		if(!i.isStackable()) {
			this.amount = 1;
		}
	}
	
	public Item getItem() {
		return i;
	}
	
	public int getAmount() {
		return amount;
	}
	
	/**
	 * Adjust the amount of items in an inventory slot by the addend.
	 * @param addend The number of items added (or subtracted if negative).
	 */
	public void adjustItemAmount(int addend) {
		amount += addend;
	}
}
