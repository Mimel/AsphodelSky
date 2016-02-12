package item;

import entity.Player;

public class Feet extends Item {

	public Feet(String fileLoc, int xOff, int yOff) {
		super(fileLoc, xOff, yOff);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isUsable(Player p1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String use(Player p1) {
		// TODO Auto-generated method stub
		return null;
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
