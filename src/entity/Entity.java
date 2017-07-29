package entity;

/**
 * An entity is one that is able to, on it's own (or AI controlled) volition, interact with the grid state.
 * @author Matt Imel
 */
public interface Entity {
	
	/**
	 * Returns the unique id of a given entity. Implementors must declare an id global (preferably final).
	 * @return The id of the entity.
	 */
	int getId();
	
	/**
	 * Returns the name of a given entity. Implementors must declare a name global.
	 * @return The name of an entity.
	 */
	String getName();
	
	/**
	 * Returns the title of a given entity. Implementors must declare a title global.
	 * @return The title of the entity.
	 */
	String getTitle();

	/**
	 * Returns the description of a given entity. Implementors must declare a description global.
	 * @return The description of the entity.
	 */
	String getDesc();
	
	/**
	 * Sets the name of a given entity. Implementors must declare a name global.
	 * @param title The new title of the entity.
	 */
	void setTitle(String title);
	
}
