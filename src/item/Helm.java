package item;

import entity.Player;

public class Helm extends Item implements Equippable {
	public static final Helm TEST_HELM = new Helm("Ayy", "Lmao", 0, 0) {
		@Override
		public String use(Player p1) {
			return null;
		}
	};

	private Helm(String t, String d, int xLoc, int yLoc) {
		super("Ayy!", 12, 54);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String die(Player p1) {
		// TODO Auto-generated method stub
		return null;
	}
}
