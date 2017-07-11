package entity;

import grid.Grid;
import item.Catalog;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A combatant is an occupant that is able to fight; this implies that they have all the attributes that a typical player has,
 * such as health, momentum, an inventory, and equipment.
 * @author Matt Imel
 */
public abstract class Combatant implements Entity {

	private static final AtomicInteger auto_incr_id = new AtomicInteger(0);
	
	/**
	 * The id of the combatant. Each instance of any enemy has a unique integer id,
	 * with the player having an id of 0.
	 */
	private int id;
	
	/**
	 * The name of the combatant. Can be a unique name (e.g. Matt, Joselyn) or
	 * a description (Fighter, Stag Beetle).
	 */
	private String name;
	
	/**
	 * An adjective(s) describing the combatant. If the combatant does not
	 * have a title, this field is an empty string.
	 */
	private String title;

	/**
	 * The description of the combatant. Liable to change during runtime.
	 */
	private String desc;
	
	/**
	 * The current health of the combatant, as a positive integer. Must
	 * be smaller or equal to the maximum health.
	 * @see #maximumHealth
	 */
	private int currentHealth;
	
	/**
	 * The maximum health of the combatant, as a non-zero positive integer.
	 */
	private int maximumHealth;

	/**
	 * The current momentum of the combatant, which represents a positive integer between 0 and 99 inclusively.
	 */
	private int momentum;
	
	/**
	 * The current science of the combatant; the "mana" analogue.
	 */
	private int currentScience;
	
	/**
	 * The maximum science of the combatant; the "mana" analogue.
	 */
	private int maximumScience;
	
	/**
	 * The fighting prowess of the combatant. Primarily determines melee and heavier weapon efficacy.
	 */
	private int poise;
	
	/**
	 * The ability to be unseen and do the unseen. Primarily determines smaller weapon efficacy, as well as a modicum of charisma's job.
	 */
	private int subtlety;
	
	/**
	 * The mental capability of the combatant. Primarily determines efficacy of all science.
	 */
	private int acumen;
	
	/**
	 * The glibness of the combatant. Primarily affects efficacy of allies, as well as enemy manipulation.
	 */
	private int charisma;
	
	/**
	 * The intuition of the combatant. All-rounder.
	 */
	private int intuition;
	
	/**
	 * The inventory of the combatant.
	 */
	private Catalog inventory;

	/**
	 * Creates a combatant with zeroed stats.
	 * @param name The name of the combatant.
	 * @param title The title of the combatant.
	 * @param desc The visual description of the combatant.
	 * @param health The health of the combatant.
	 * @param science The science of the combatant.
	 */
	Combatant(String name, String title, String desc, int health, int science) {
		this.id = auto_incr_id.getAndIncrement(); //TODO causes error in grid drawing
		this.name = name;
		this.title = title;
		this.desc = desc;

		this.maximumHealth = health;
		this.currentHealth = health;
		this.maximumScience = science;
		this.currentScience = science;

		this.inventory = new Catalog();
	}

	/**
	 * Creates a complete combatant.
	 * @param name The name.
	 * @param title The title.
	 * @param desc The visual description.
	 * @param health The health.
	 * @param momentum The momentum.
	 * @param science The science.
	 * @param pse The poise.
	 * @param sub The subtlety.
	 * @param acu The acumen.
	 * @param cha The charisma.
	 * @param itt The intuition.
	 */
	Combatant(String name, String title, String desc, int health, int momentum, int science, int pse, int sub, int acu, int cha, int itt) {
		this.id = auto_incr_id.getAndIncrement();
		this.name = name;
		this.title = title;
		this.desc = desc;

		this.maximumHealth = health;
		this.currentHealth = health;
		this.momentum = momentum;
		this.currentScience = science;
		this.maximumScience = science;
		
		this.poise = pse;
		this.subtlety = sub;
		this.acumen = acu;
		this.charisma = cha;
		this.intuition = itt;
	}
	
	/**
	 * Gets the id of the combatant.
	 * @return The id of the combatant.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the name of the combatant.
	 * @return The name of the combatant.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the title of the combatant.
	 * @return The title of the combatant.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Gets the description of the combatant.
	 * @return The description of the combatant.
	 */
	public String getDesc() { return desc; }
	
	/**
	 * Sets the title of the combatant to the specified title.
	 * @param newTitle The new title for the combatant.
	 */
	public void setTitle(String newTitle) {
		title = newTitle;
	}

	/**
	 * Retrieves the current health of the combatant.
	 * @return The current health of the combatant.
	 * @see #getMaxHealth
	 */
	public int getHealth() {
		return currentHealth;
	}
	
	/**
	 * Retrieves the maximum health of the combatant.
	 * @return The maximum health of the combatant.
	 */
	public int getMaxHealth() {
		return maximumHealth;
	}
	
	public int getMomentum() {
		return momentum;
	}

	public int getCurrentScience() {
		return currentScience;
	}

	public int getMaximumScience() {
		return maximumScience;
	}

	public int getPoise() {
		return poise;
	}

	public int getSubtlety() {
		return subtlety;
	}

	public int getAcumen() {
		return acumen;
	}

	public int getCharisma() {
		return charisma;
	}

	public int getIntuition() {
		return intuition;
	}
	
	/**
	 * Decreases the amount of current health by a given amount.
	 * @param healthAdjustment The amount of health to add to the current health.
	 */
	public void adjustHealthBy(int healthAdjustment) {
		if(healthAdjustment + currentHealth < 0) {
			currentHealth = 0;
		} else if(currentHealth + healthAdjustment >= maximumHealth) {
			currentHealth = maximumHealth;
		} else {
			currentHealth += healthAdjustment;
		}
	}
	
	/**
	 * Adjusts the current momentum of the combatant by a given amount. This adjusted amount cannot
	 * exceed 99 or fall below 0.
	 * @param momentumAdjustment The amount to add to the current momentum.
	 */
	public void adjustMomentumBy(int momentumAdjustment) {
		if(momentumAdjustment > momentum) {
			momentum = 0;
		} else if (momentum + momentumAdjustment >= 99) {
			momentum = 99;
		} else {
			momentum += momentumAdjustment;
		}
	}
	
	/**
	 * Decreases the amount of current science by a given amount.
	 * @param scienceAdjustment The amount of science to add to the current science.
	 */
	public void adjustScienceBy(int scienceAdjustment) {
		if(scienceAdjustment + currentScience < 0) {
			currentScience = 0;
		} else if(currentScience + scienceAdjustment >= maximumScience) {
			currentScience = maximumScience;
		} else {
			currentScience += scienceAdjustment;
		}
	}
	
	/**
	 * Retrieves the inventory of the combatant.
	 * @return The inventory of the combatant.
	 */
	public Catalog getInventory() {
		return inventory;
	}

	public abstract void act(OperationAI opai, int time, Grid gr);

	@Override
	public String toString() {
		if(title == "") {
			return name;
		} else {
			return name + " the " + title;
		}
	}
}
