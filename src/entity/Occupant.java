package entity;

/**
 * An occupant is an extension of an entity that is able to occupy a tile on the grid.
 * @author Matt Imel
 */
public interface Occupant extends Entity {
	
	/**
	 * The X-coordinate on the grid the occupant is located on. The implementor must declare a global X-coordinate.
	 * @return The X-coordinate.
	 */
	public int getX();
	
	/**
	 * The Y-coordinate on the grid the occupant is located on. The implementor must declare a global Y-coordinate.
	 * @return The Y-coordinate.
	 */
	public int getY();
	
	/**
	 * Sets the X-coordinate of the occupant. The implementor must declare a global X-coordinate.
	 * @param newX The new X-coordinate.
	 */
	public void setX(int newX);
	
	/**
	 * Sets the Y-coordinate of the occupant. The implementor must declare a global Y-coordinate.
	 * @param newY The new Y-coordinate.
	 */
	public void setY(int newY);
	
}