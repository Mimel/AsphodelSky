package item;

import entity.Player;
import game.FlavorText;

public class ItemTrigger {
	private Item i;
	private int methodToCall;
	
	ItemTrigger(Item i, int status) {
		this.i = i;
		this.methodToCall = status;
	}
	
	public FlavorText activate(Player p1) {
		switch(methodToCall) {
			case Item.ON_ELAPSE_FADE:
				return i.fade(p1);
			case Item.ON_ELAPSE_DIE:
				return i.die(p1);
		}
		return null;
	}
}
