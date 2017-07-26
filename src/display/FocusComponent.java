package display;

import grid.Tile;

public interface FocusComponent {
	void updateGrid(Tile[][] grid, int xFocus, int yFocus);
}
