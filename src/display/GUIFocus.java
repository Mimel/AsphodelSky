package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.javatuples.Pair;

import grid.Tile;
import item.Item;

/**
 * The main focus of the Asphodel Sky GUI, this is where the grid is based.
 * @author Matt Imel
 *
 */
public class GUIFocus extends GUIComponent implements FocusComponent {
	
	/**
	 * The terrain tileset to use.
	 */
	private Image TERRAIN_TILESET;
	
	/**
	 * Hash table that links characters to their representative tile images within the
	 * given tileset.
	 */
	private Map<Character, Pair<Integer, Integer>> tileMargins;
	
	/**
	 * The crosshair to use.
	 */
	private Image SELECTOR_YELLOW;
	
	/**
	 * The grid component used as a basis for updating the display. Most (not all) of the time,
	 * this exactly reflects the grid that is in the control.
	 */
	private Tile[][] grid;
	
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
		
		///
		// MOVEGRID: Directional keys move the player. All button commands are, by default, enabled.
		//
		// SCANGRID: Directional keys move the screen, focused on a crosshair. Some button commands
		//           are disabled.
		///
		modes = new String[]{"player", "crosshair"};
		selectedMode = modes[0];
		
		try {
			TERRAIN_TILESET = ImageIO.read(new File("img/terrain/terraintileset.png"));
			SELECTOR_YELLOW = ImageIO.read(new File("img/misc/selector_generic.png"));
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
		tileMargins = new HashMap<Character, Pair<Integer, Integer>>();
		
		//Initialize hashmap.
		tileMargins.put('.', new Pair<Integer, Integer>(0, 0));
	}
	
	@Override
	public void drawGrid(Tile[][] grid) {
		this.grid = grid;
		this.repaint();
	}
	
	/**
	 * Draws the grid, and all items within the grid.
	 * TODO: test.
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
					
					//Tile.
					drawImageFromTileset(g, TERRAIN_TILESET, x * squareSize, y * squareSize,
							tileMargins.get(currentTile.getTerrain()).getValue0(),
							tileMargins.get(currentTile.getTerrain()).getValue1(), squareSize);
					
					//Player.
					if(currentTile.getOccupant() != null && currentTile.getOccupant().getId() == 0) {
						g.setColor(new Color(0, 200, 100));
						g.fillRect(x * squareSize, y * squareSize, squareSize, squareSize);
					}
					
					//Items.
					if(!currentTile.getCatalog().isEmpty()) {
						currentItem = currentTile.getCatalog().getItems().get(0);
						
						drawImageFromTileset(g, currentItem.getTileset(), x * squareSize, y * squareSize,
											currentItem.getXOffset() + currentItem.getXMargin(), currentItem.getYOffset() + currentItem.getYMargin(), squareSize);
					}
					
					//Focus crosshair, if used.
					if(currentTile.isFocused()) {
						drawImageFromTileset(g, SELECTOR_YELLOW, x * squareSize, y * squareSize, 0, 0, squareSize);
					}
				}
			}
		}
	}
	
	/**
	 * Draws an image from a given tileset on a specific region.
	 * @param g Graphics object.
	 * @param tileset The tileset to take the image from.
	 * @param xSrc The X-coordinate of the top-left corner of the source image to use.
	 * @param ySrc The Y-coordinate of the top-left corner of the source image to use.
	 * @param xDest The X-coordinate of the top-left corner of the destination.
	 * @param yDest The Y-coordinate of the top-left corner of the destination.
	 * @param squareSize The length/width of the tile to draw.
	 * 
	 * TODO: swap Src, Dest.
	 */
	private void drawImageFromTileset(Graphics g, Image tileset, int xSrc, int ySrc, int xDest, int yDest, int squareSize) {
		g.drawImage(tileset, xSrc, ySrc, xSrc + squareSize, ySrc + squareSize, xDest, yDest, xDest + squareSize, yDest + squareSize, null);
	}
}
