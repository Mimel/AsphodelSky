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
		for(int room = 0; room < ship.length; room++) {
			
			do {
				//Expand North end.
				if(!ship[room].canGoNorth() || ship[room].getY() == 0) {
					ship[room].stopExpansionNorth();
				} else if(!checkHorizontalSwath(ship[room].getY()-1, ship[room].getX(), ship[room].getX() + ship[room].getWidth() - 1)) {
					ship[room].stopExpansionNorth();
				} else {
					ship[room].expandNorth();
					fillHorizontalSwath(ship[room].getY(), ship[room].getX(), ship[room].getX() + ship[room].getWidth() - 1);
				}
				
				//Expand West end.
				if(!ship[room].canGoWest() || ship[room].getX() == 0) {
					ship[room].stopExpansionWest();
				} else if(!checkVerticalSwath(ship[room].getX()-1, ship[room].getY(), ship[room].getY() + ship[room].getHeight() - 1)) {
					ship[room].stopExpansionWest();
				} else {
					ship[room].expandWest();
					fillVerticalSwath(ship[room].getX(), ship[room].getY(), ship[room].getY() + ship[room].getHeight() - 1);
				}
				
				//Expand South end.
				if(!ship[room].canGoSouth() || ship[room].getY() + ship[room].getHeight() >= height) {
					ship[room].stopExpansionSouth();
				} else if(!checkHorizontalSwath(ship[room].getY() + ship[room].getHeight(), ship[room].getX(), ship[room].getX() + ship[room].getWidth() - 1)) {
					ship[room].stopExpansionSouth();
				} else {
					ship[room].expandSouth(height);
					fillHorizontalSwath(ship[room].getY() + ship[room].getHeight() - 1, ship[room].getX(), ship[room].getX() + ship[room].getWidth() - 1);
				}
				
				//Expand East end.
				if(!ship[room].canGoEast() || ship[room].getX() + ship[room].getWidth() >= width) {
					ship[room].stopExpansionEast();
				} else if(!checkVerticalSwath(ship[room].getX() + ship[room].getWidth(), ship[room].getY(), ship[room].getY() + ship[room].getHeight() - 1)) {
					ship[room].stopExpansionEast();
				} else {
					ship[room].expandEast(width);
					fillVerticalSwath(ship[room].getX() + ship[room].getWidth() - 1, ship[room].getY(), ship[room].getY() + ship[room].getHeight() - 1);
				}
			} while(ship[room].canExpand());
		}
		
		//Place rooms into grid.
		for(int room = 0; room < ship.length; room++) {
			System.out.println("Y:" + ship[room].getY() + ", H:" + ship[room].getHeight() + ", X:" 
					+ ship[room].getX() + ", W:" + ship[room].getWidth());
			
			if(ship[room].getWidth() > 1 && ship[room].getHeight() > 1) {
				for(int y = ship[room].getY(); y < ship[room].getY() + ship[room].getHeight(); y++) {
					for(int x = ship[room].getX(); x < ship[room].getX() + ship[room].getWidth(); x++) {
						//map[y][x] = new Tile((char)(97 + room)); //DEBUG: ROOMS BY LETTERS
						
						
						//TODO: shoddy way to wall a room.
						if(x != ship[room].getX() && y != ship[room].getY() &&
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
