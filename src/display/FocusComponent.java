package display;

import entity.Combatant;
import grid.Point;
import grid.Tile;
import item.Catalog;

import java.util.Map;

/**
 * The component of the window that displays the grid information.
 */
public interface FocusComponent {
	/**
	 * Updates the grid to display, which is represented as a 2D array of tiles.
	 * @param grid A 2D array of tiles to display.
	 * @param xFocus The X-coordinate focus of the grid, if one exists.
	 * @param yFocus The Y-coordinate focus of the grid, if one exists.
	 */
	void updateGrid(Iterable<Tile[]> tileGrid, Iterable<Map.Entry<Point, Combatant>> combatantGrid, Iterable<Map.Entry<Point, Catalog>> catalogGrid, int xFocus, int yFocus);
}
