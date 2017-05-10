package generator;

public class RoomSquare {
	
	private int x;
	private int y;
	
	private boolean canGoNorth;
	private boolean canGoSouth;
	private boolean canGoEast;
	private boolean canGoWest;
	
	/**
	 * The number of columns the room occupies, DISCOUNTING the initial column.
	 */
	private int width;
	
	/**
	 * The number of rows the room occupies, DISCOUNTING the initial row.
	 */
	private int height;
	
	public RoomSquare(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		this.canGoNorth = true;
		this.canGoSouth = true;
		this.canGoEast = true;
		this.canGoWest = true;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean expandWest() {
		if(canGoWest && x > 0) {
			x--;
			width++;
			return true;
		} else {
			return (canGoWest = false);
		}
	}
	
	public boolean expandNorth() {
		if(canGoNorth && y > 0) {
			y--;
			height++;
			return true;
		} else {
			return (canGoNorth = false);
		}
	}
	
	public boolean expandEast(int xConstraint) {
		if(canGoEast && x + width < xConstraint) {
			width++;
			return true;
		} else {
			return (canGoEast = false);
		}
	}
	
	public boolean expandSouth(int yConstraint) {
		if(canGoSouth && y + height < yConstraint) {
			height++;
			return true;
		} else {
			return (canGoSouth = false);
		}
	}
	
	public void stopExpansionNorth() {
		canGoNorth = false;
	}
	
	public void stopExpansionSouth() {
		canGoSouth = false;
	}
	
	public void stopExpansionEast() {
		canGoEast = false;
	}
	
	public void stopExpansionWest() {
		canGoWest = false;
	}
	
	public boolean canGoNorth() {
		return canGoNorth;
	}
	
	public boolean canGoSouth() {
		return canGoSouth;
	}
	
	public boolean canGoEast() {
		return canGoEast;
	}
	
	public boolean canGoWest() {
		return canGoWest;
	}
	
	public boolean canExpand() {
		return canGoNorth || canGoSouth || canGoEast || canGoWest;
	}
}