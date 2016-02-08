package item;

import entity.Player;

public class Vial extends Item implements Consumable {
	
	public static final Vial HEALING_VIAL = new Vial("Healing Vial", "Restores a small portion of your health.", 0, 0) {
		public boolean isUsable(Player p1) {
			return p1.getCurrHP() != p1.getMaxHP();
		}
		
		public String use(Player p1) {
			if(!this.isUsable(p1)) {
				return "Health is already at a maximum.";
			} else {
				p1.adjustCurrentHealth(6);
				if(p1.getCurrHP() > p1.getMaxHP()) {
					p1.equalizeHealth();
				}	
				return "You feel rejuvenated.";	
			}
		}
	};
	
	public static final Vial ENERGY_VIAL = new Vial("Energy Vial", "Restores a portion of your energy.", 36, 0) {
		public boolean isUsable(Player p1) {
			return p1.getCurrEP() != p1.getMaxEP();
		}
		
		public String use(Player p1) {
			return "This is a cantrip, but we'll see how it goes.";
		}
	};

	private Vial(String t, String d, int xLoc, int yLoc) {
		super("Constant Location", 30, 60);
		this.title = t;
		this.desc = d;
		this.xItemLoc = xLoc;
		this.yItemLoc = yLoc;
	}

	@Override
	public String use(Player p1) {
		return null;
	}

	@Override
	public boolean isUsable(Player p1) {
		return false;
	}

	@Override
	public String step(Player p1) {
		return null;
	}

	@Override
	public String die(Player p1) {
		return null;
	}

}
