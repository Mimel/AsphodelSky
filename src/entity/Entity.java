package entity;

public abstract class Entity {
	protected boolean id;
	protected String name;
	
	protected int xCoord;
	protected int yCoord;
	
	protected int currHP;
	protected int maxHP;
	
	protected double movementSpeed;
	
	/**
	 * All of these are various flags on an entity; whether or not they are invisible,
	 * immobile, etc. are determined by these various sets of flags.
	 */
	protected boolean immobile = false;
	protected boolean speaks = false;
	
	final public int getXCoord() {
		return xCoord;
	}
	
	final public int getYCoord() {
		return yCoord;
	}
	
	public int getCurrHP() {
		return currHP;
	}

	public void takeDamage(int damage) {
		this.currHP = currHP - damage;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}

	final public double getMovementSpeed() {
		return movementSpeed;
	}
	
	/**
	 * Moves the entity 1 tile in any of the 8 directions. This, of course, greatly depends on the
	 * current awareness of the enemy and any presence of the walls, as well as the presence of the
	 * player.
	 */
	abstract void move(int xShift, int yShift);
}