package display;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Map;

import entity.Combatant;
import grid.*;
import item.Catalog;
import item.Item;

/**
 * The main focus of the Asphodel Sky GUI, this is where the grid is based.
 * @author Matt Imel
 *
 */
public class GUIFocus extends GUIComponent<FocusMode> implements FocusComponent {
	
	/**
	 * The grid component used as a basis for updating the display. Most (not all) of the time,
	 * this exactly reflects the grid that is in the control.
	 */
	private Tile[][] grid;

	private Iterable<Tile[]> tiles;
	private Iterable<Map.Entry<Point, Combatant>> actors;
	private Iterable<Map.Entry<Point, Catalog>> catalogs;

	private int gridFocusX;
	private int gridFocusY;

	private int offsetX;
	private int offsetY;

	/**
	 * The size of each individual square when it is drawn, in pixels.
	 */
	private int squareSize;
	
	/**
	 * Creates the grid component.
	 * @param x The X Position of the grid relative to the display.
	 * @param y The Y Position of the grid relative to the display.
	 * @param w The width of the grid, in pixels.
	 * @param h The height of the grid, in pixels.
	 * @param sqsize The size of each drawn Tile, in pixels.
	 */
	public GUIFocus(int x, int y, int w, int h, int sqsize) {
		super(x, y, w, h);

		this.squareSize = sqsize;

		selectedMode = FocusMode.PLAYER_FOCUS;
	}
	
	@Override
	public void updateGrid(Iterable<Tile[]> tileGrid, Iterable<Map.Entry<Point, Combatant>> combatantGrid, Iterable<Map.Entry<Point, Catalog>> catalogGrid, int xFocus, int yFocus, int offsetX, int offsetY) {
		this.tiles = tileGrid;
		this.actors = combatantGrid;
		this.catalogs = catalogGrid;

		this.gridFocusX = xFocus;
		this.gridFocusY = yFocus;

		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}
	
	/**
	 * Draws the grid, and all items within the grid.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(new Color(200, 0, 0));
		g.fillRect(0, 0, width, height);
		
		if(tiles != null) {
			Tile currentTile;
			Item currentItem;

			int x = 0;
			for(Tile[] line: tiles) {
				for(int y = 0; y < line.length; y++) {

					g.drawImage(ImageAssets.getTerrainImage(line[y].getTerrain()), x*squareSize, y*squareSize, null);
				}
				x++;
			}

			for(Map.Entry<Point, Combatant> combatant : actors) {
				Point loc = combatant.getKey();
				if(combatant.getValue().getId() == 0) {
					g.setColor(new Color(0, 200, 100));
					g.fillRect((loc.x() - offsetX) * squareSize, (loc.y() - offsetY) * squareSize, squareSize, squareSize);
				} else {
					g.drawImage(ImageAssets.getCharImage(combatant.getValue().getName()), (loc.x() - offsetX)*squareSize, (loc.y() - offsetY)*squareSize, null);
				}
			}

			for(Map.Entry<Point, Catalog> catalog : catalogs) {
				if(!catalog.getValue().isEmpty()) {
					Point loc = catalog.getKey();
					g.drawImage(ImageAssets.getItemImage(catalog.getValue().getFocusedItem().getName()), (loc.x() - offsetX)*squareSize, (loc.y() - offsetY)*squareSize, null);
				}
			}

			if(selectedMode == FocusMode.SELECTION) {
				g.drawImage(ImageAssets.getMiscImage('+'), (gridFocusX - offsetX)*squareSize, (gridFocusY - offsetY)*squareSize, null);
			}
		}
	}
}
