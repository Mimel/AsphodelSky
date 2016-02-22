package item;

public class ItemAmountPair {
	
	private Item i;
	private int amt;
	
	public ItemAmountPair(Item i, int amount) {
		this.i = i;
		
		if(i.STACKABLE) {
			this.amt = amount;
		} else {
			amt = 1;
		}
	}
	
	public Item getItem() {
		return i;
	}
	
	public int getAmount() {
		return amt;
	}
	
	public void addItems(int addend) {
		if(i.STACKABLE) {
			amt += addend;
		} else {
			amt = 1;
		}
	}
	
	public void decrementItem() {
		if(i.STACKABLE) {
			if(amt != 0) {
				amt--;
			}
		}
	}
	
	public boolean hasItems() {
		return amt != 0;
	}
}
