package generator;

import grid.Tile;

public class EmptyShipGenerator implements GridGenerator {
	
	/**
	 * Creates an empty grid of '.' tiles of the given dimensions.
	 */
	@Override
	public Tile[][] generateGrid(int width, int height) {
		Tile[][] map = new Tile[height][width];
		
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				//TEMP
				if(y > 15 && (x + y) % 4 == 0) {
					map[y][x] = new Tile('#');
				} else {
					map[y][x] = new Tile('.');
				}
			}
		}
		
		return map;
	}
	
}
