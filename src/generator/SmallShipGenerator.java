package generator;

import java.util.Random;

import grid.Tile;

public class SmallShipGenerator implements GridGenerator {
	private Random r;
	
	private boolean[][] switchboard;
	
	public SmallShipGenerator() {
		r = new Random();
	}
	
	/**
	 * Generates a grid.
	 * Steps:
	 * Points on the map are selected equal to the number of rooms requested.
	 * After this, process continues in a sequence of steps.
	 * Each room expands around the center.
	 * If two rooms intersect, that intersection is a wall, and the rooms stop expanding.
	 * Continue until all rooms can no longer grow.
	 * 
	 * TODO: Test.
	 */
	@Override
	public Tile[][] generateGrid(int width, int height) {
		Tile[][] map = new Tile[height][width];
		
		//Initialize map.
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				map[y][x] = new Tile('#');
			}
		}
		
		//Provides point collision detection.
		switchboard = new boolean[height][width];
		
		// Rooms must take up a minimum of 36 squares. The number of rooms that will be present
		// is equal to the number of tiles/36, with at least one room.
		int numRooms = 10;
		RoomSquare[] ship = new RoomSquare[numRooms];
		
		// Initializes points.
		for(int room = 0; room < ship.length; room++) {
			int w;
			int h;
			
			do {
				w = r.nextInt(width);
				h = r.nextInt(height);
			} while(switchboard[h][w]);
			
			ship[room] = new RoomSquare(w, h, 1, 1);
			switchboard[h][w] = true;
		}
		
		//Expands all rooms.
		for (RoomSquare aShip : ship) {

			do {
				//Expand North end.
				if (!aShip.canGoNorth() || aShip.getY() == 0) {
					aShip.stopExpansionNorth();
				} else if (!checkHorizontalSwath(aShip.getY() - 1, aShip.getX(), aShip.getX() + aShip.getWidth() - 1)) {
					aShip.stopExpansionNorth();
				} else {
					aShip.expandNorth();
					fillHorizontalSwath(aShip.getY(), aShip.getX(), aShip.getX() + aShip.getWidth() - 1);
				}

				//Expand West end.
				if (!aShip.canGoWest() || aShip.getX() == 0) {
					aShip.stopExpansionWest();
				} else if (!checkVerticalSwath(aShip.getX() - 1, aShip.getY(), aShip.getY() + aShip.getHeight() - 1)) {
					aShip.stopExpansionWest();
				} else {
					aShip.expandWest();
					fillVerticalSwath(aShip.getX(), aShip.getY(), aShip.getY() + aShip.getHeight() - 1);
				}

				//Expand South end.
				if (!aShip.canGoSouth() || aShip.getY() + aShip.getHeight() >= height) {
					aShip.stopExpansionSouth();
				} else if (!checkHorizontalSwath(aShip.getY() + aShip.getHeight(), aShip.getX(), aShip.getX() + aShip.getWidth() - 1)) {
					aShip.stopExpansionSouth();
				} else {
					aShip.expandSouth(height);
					fillHorizontalSwath(aShip.getY() + aShip.getHeight() - 1, aShip.getX(), aShip.getX() + aShip.getWidth() - 1);
				}

				//Expand East end.
				if (!aShip.canGoEast() || aShip.getX() + aShip.getWidth() >= width) {
					aShip.stopExpansionEast();
				} else if (!checkVerticalSwath(aShip.getX() + aShip.getWidth(), aShip.getY(), aShip.getY() + aShip.getHeight() - 1)) {
					aShip.stopExpansionEast();
				} else {
					aShip.expandEast(width);
					fillVerticalSwath(aShip.getX() + aShip.getWidth() - 1, aShip.getY(), aShip.getY() + aShip.getHeight() - 1);
				}
			} while (aShip.canExpand());
		}
		
		//Place rooms into grid.
		for (RoomSquare aShip : ship) {
			System.out.println("Y:" + aShip.getY() + ", H:" + aShip.getHeight() + ", X:"
					+ aShip.getX() + ", W:" + aShip.getWidth());

			if (aShip.getWidth() > 1 && aShip.getHeight() > 1) {
				for (int y = aShip.getY(); y < aShip.getY() + aShip.getHeight(); y++) {
					for (int x = aShip.getX(); x < aShip.getX() + aShip.getWidth(); x++) {
						//map[y][x] = new Tile((char)(97 + room)); //DEBUG: ROOMS BY LETTERS


						//TODO: shoddy way to wall a room.
						if (x != aShip.getX() && y != aShip.getY() &&
								x != width - 1 && y != height - 1) {
							map[y][x] = new Tile('.');
						}
					}
				}
			}
		}
		
		return map;
	}
	
	public boolean checkHorizontalSwath(int y, int xStart, int xEnd) {
		for(int x = xStart; x <= xEnd; x++) {
			if(switchboard[y][x]) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean checkVerticalSwath(int x, int yStart, int yEnd) {
		for(int y = yStart; y <= yEnd; y++) {
			if(switchboard[y][x]) {
				return false;
			}
		}
		
		return true;
	}
	
	public void fillHorizontalSwath(int y, int xStart, int xEnd) {
		for(int x = xStart; x <= xEnd; x++) {
			switchboard[y][x] = true;
		}
	}
	
	public void fillVerticalSwath(int x, int yStart, int yEnd) {
		for(int y = yStart; y <= yEnd; y++) {
			switchboard[y][x] = true;
		}
	}
}
