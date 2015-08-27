package entity;

/**
 * This class represents a character present on the map, capable of very basic tasks, such as walking, having an AI, and
 * whatnot. The player derives from this class, and can do a wider array of functions.
 * @author Mimel
 */
public abstract class Entity {
	/** Wait, what? */
	protected boolean id;
	
	/** The name of the Entity in question. */
	protected String name;
	
	/** The x-Location of the Entity relative to the current map. */
	protected int xCoord;
	
	/** The y-Location of the Entity relative to the current map. */
	protected int yCoord;
	
	/** The current amount of health an entity has. */
	protected int currHP;
	
	/** The maximum amount of health an entity can have. */
	protected int maxHP;
	
	/** The current amount of energy an entity has. */
	protected int currEP;
	
	/** The maximum amount of energy an entity has. */
	protected int maxEP;
	
	/** The time it takes for an entity to move one tile in any direction. */
	protected int movementSpeed;
	
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
	
	public int getMaxHP() {
		return maxHP;
	}
	
	public int getCurrEP() {
		return currEP;
	}
	
	public int getMaxEP() {
		return maxEP;
	}
	
	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}

	public int getMovementSpeed() {
		return movementSpeed;
	}
	
	public void setMovementSpeed(int ms) {
		this.movementSpeed = ms;
	}
	
	/**
	 * Adjusts Entity's current health by integer addend.
	 * @param addend Number by which to alter the currentHealth.
	 */
	public void adjustCurrentHealth(int addend) {
		currHP += addend;
	}
	
	/**
	 * Sets the current health equal to the maximum health.
	 */
	public void equalizeHealth() {
		currHP = maxHP;
	}
	
	/**
	 * Adjusts Entity's current health by integer addend.
	 * @param addend Number by which to alter the currentHealth.
	 */
	public void adjustCurrentEnergy(int addend) {
		currEP += addend;
	}
	
	/**
	 * Sets the current health equal to the maximum health.
	 */
	public void equalizeEnergy() {
		currEP = maxEP;
	}
	
	/**
	 * Moves the entity 1 tile in any of the 8 directions. This, of course, greatly depends on the
	 * current awareness of the enemy and any presence of the walls, as well as the presence of the
	 * player.
	 */
	abstract void move(int xShift, int yShift);
}