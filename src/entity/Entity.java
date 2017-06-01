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
	public int getId();
	
	/**
	 * Returns the name of a given entity. Implementors must declare a name global.
	 * @return The name of an entity.
	 */
	public String getName();
	
	/**
	 * Returns the title of a given entity. Implementors must declare a title global.
	 * @return The title of the entity.
	 */
	public String getTitle();

	/**
	 * Returns the description of a given entity. Implementors must declare a description global.
	 * @return The description of the entity.
	 */
	public String getDesc();
	
	/**
	 * Sets the name of a given entity. Implementors must declare a name global.
	 * @param name The new name of the entity.
	 */
	public void setTitle(String title);
	
}
