package display;

import java.awt.Color;
import java.awt.Graphics;

import grid.Tile;
import item.Item;

/**
 * The main focus of the Asphodel Sky GUI, this is where the grid is based.
 * @author Matt Imel
 *
 */
public class GUIFocus extends GUIComponent implements FocusComponent {
	
	/**
	 * The grid component used as a basis for updating the display. Most (not all) of the time,
	 * this exactly reflects the grid that is in the control.
	 */
	private Tile[][] grid;

	private int gridFocusX;
	private int gridFocusY;

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

		modes = new String[]{"player", "crosshair"};
		selectedMode = modes[0];
	}
	
	@Override
	public void updateGrid(Tile[][] grid, int xFocus, int yFocus) {
		this.grid = grid;
		this.gridFocusX = xFocus;
		this.gridFocusY = yFocus;
	}
	
	/**
	 * Draws the grid, and all items within the grid.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(new Color(200, 0, 0));
		g.fillRect(0, 0, width, height);
		
		if(grid != null) {
			Tile currentTile;
			Item currentItem;

			//Draws the base grid.
			for(int y = 0; y < grid.length; y++) {
				for(int x = 0; x < grid[y].length; x++) {
					currentTile = grid[y][x];
					
					g.drawImage(ImageAssets.getTerrainImage(currentTile.getTerrain()), x*squareSize, y*squareSize, null);
					
					//Player, Enemies.
					if(currentTile.getOccupant() != null) {
						if(currentTile.getOccupant().getId() == 0) {
							g.setColor(new Color(0, 200, 100));
							g.fillRect(x * squareSize, y * squareSize, squareSize, squareSize);
						} else {
							g.drawImage(ImageAssets.getCharImage(currentTile.getOccupant().getName()), x*squareSize, y*squareSize, null);
						}	
					}
					
					//Items.
					if(!currentTile.getCatalog().isEmpty()) {
						currentItem = currentTile.getCatalog().getItems().get(0);
						g.drawImage(ImageAssets.getItemImage(currentItem.getName()), x*squareSize, y*squareSize, null);
					}
					
					//Focus crosshair, if used.
					if(selectedMode.equals("crosshair") && x == gridFocusX && y == gridFocusY) {
						g.drawImage(ImageAssets.getMiscImage('+'), x*squareSize, y*squareSize, null);
					}
				}
			}
		}
	}
}
