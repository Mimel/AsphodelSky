package game;

/**
 * TODO List:
 * - Account for array overfill
 * - Add inventory navigation system
 * - Successfully use an item, implement primitive health bar
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import entity.Player;

import item.*;

/**
 * This class represents the entire display of the game; everything pertinent to this game
 * is displayed via this class and its paintComponent method.
 * 
 * @author Matt Imel
 */
public class Display extends JPanel {
	
	private static final long serialVersionUID = -3124428691024905366L;
	
	/** The map of the level, made of a 2D array of Tiles. [y][x]-oriented. */
	private Tile[][] currentMap;
	
	/**
	 * In the map section of the viewport, determines the square dimension of the grid. 
	 * Note that this integer operates best as an odd number, so the player is correctly centered on screen.
	 */
	private int viewportDimension;
	
	/** Length of the top margin. */
	private final int topMargin = 35;
	
	/** Length of the left margin. */
	private final int leftMargin = 40;
	
	/** The name of the current map. */
	private String mapName;
	
	/** The player. */
	private Player p1;
	
	/** The stack of messages, used in the bottom of the game window. */
	private ArrayList<FlavorText> messageStack;
	
	/** The current time. Used to coordinate events. */
	private double time;
	
	private final int inventoryHeight = 3;
	
	private final int inventoryWidth = 12;
	
	/* Note: All Images with prefix "t_" are tilesets. */
	
	/** Main tileset from which floor/wall tiles are derived. */
	private Image t_ground;
	
	/** Main tileset from which item tiles are derived. */
	private Image t_vials;
	
	/** Main tileset for display icons, such as selection markers. */
	private Image t_icons;
	
	/* END Images */
	
	/** This determines the focus of the directional keys. Should be the only user of the enum DirectionMode.
	 * @see DirectionMode 
	 */
	private DirectionMode focusState;
	
	/**
	 * Only applicable in DirectionMode FOCUS_INVENTORY: The slot in the player's inventory in which the selector is on.
	 * @see focusState;
	 */
	private int inventorySlotSelected;
	
	/**
	 * All the possible states from which the variable focusState can be.
	 * @see focusState
	 */
	private enum DirectionMode {
		FOCUS_MAP, FOCUS_INVENTORY;
	}
	
	/**
	 * Text which also has a color assigned to it. Used nearly everywhere in the GUI
	 * where text exists.
	 */
	private class FlavorText {
		private String text;
		private Color color;
		
	//	FlavorText(String text, Color color) {
	//		this.text = text;
	//		this.color = color;
	//	}
	}

	public Display() {
		this.initializeKeyBinds();
		this.viewportDimension = 17;
		this.messageStack = new ArrayList<FlavorText>();
		
		try {
			this.importMap("maps/m_test.dat");
			this.importImages();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//TEMP
		currentMap[2][3].pushOntoInv(Item.HEALING_VIAL);
		//ENDTEMP
		
		this.time = 0;
		this.focusState = DirectionMode.FOCUS_MAP;
		this.inventorySlotSelected = 0;
		this.calcSightBoundaries();
	}
	
	/**
	 * Initializes all keybinds used in the game. 
	 */
	private void initializeKeyBinds() {
		Action exitProgram = new AbstractAction() {
			private static final long serialVersionUID = -8817332391432139373L;
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};
		
		Action move = new AbstractAction() {
			private static final long serialVersionUID = -8817332391432139373L;
			public void actionPerformed(ActionEvent e) {
				char key = e.getActionCommand().charAt(0);
				int xOffset;
				int yOffset;
				
				boolean ableLeft = false;
				boolean ableRight = false;
				boolean ableUp = false;
				boolean ableDown = false;
				
				if(focusState == DirectionMode.FOCUS_MAP) {
					ableLeft = p1.getXCoord() != 0;
					ableRight = p1.getXCoord() != currentMap[0].length - 1;
					ableUp = p1.getYCoord() != 0;
					ableDown = p1.getYCoord() != currentMap[0].length - 1;
				} else if(focusState == DirectionMode.FOCUS_INVENTORY) {
					ableLeft = inventorySlotSelected % inventoryWidth != 0;
					ableRight = inventorySlotSelected % inventoryWidth != inventoryWidth - 1;
					ableUp = inventorySlotSelected / inventoryWidth != 0;
					ableDown = inventorySlotSelected / inventoryWidth != inventoryHeight - 1;
				}
				
				boolean canMove;
				
				switch(key) {
					case 'q':
						xOffset = -1;
						yOffset = -1;
						canMove = ableLeft && ableUp;
						break;
					case 'w':
						xOffset = 0;
						yOffset = -1;
						canMove = ableUp;
						break;
					case 'e':
						xOffset = 1;
						yOffset = -1;
						canMove = ableRight && ableUp;
						break;
					case 'a':
						xOffset = -1;
						yOffset = 0;
						canMove = ableLeft;
						break;
					case 'd':
						xOffset = 1;
						yOffset = 0;
						canMove = ableRight;
						break;
					case 'z':
						xOffset = -1;
						yOffset = 1;
						canMove = ableLeft && ableDown;
						break;
					case 'x':
						xOffset = 0;
						yOffset = 1;
						canMove = ableDown;
						break;
					case 'c':
						xOffset = 1;
						yOffset = 1;
						canMove = ableRight && ableDown;
						break;
					default:
						xOffset = 0;
						yOffset = 0;
						canMove = false;
						break;
				}
				if(focusState == DirectionMode.FOCUS_MAP) {
					if(canMove && !currentMap[p1.getYCoord() + yOffset][p1.getXCoord() + xOffset].isImpassable()) {
						p1.move(xOffset, yOffset);
						shiftTime(p1.getMovementSpeed());
					}
				} else if(focusState == DirectionMode.FOCUS_INVENTORY) {
					if(canMove) {
						inventorySlotSelected += (yOffset * inventoryWidth + xOffset);
						shiftTime(0);
					}
				}
			}
		};
		
		Action stall1sec = new AbstractAction() {
			private static final long serialVersionUID = -8817332391432139373L;
			public void actionPerformed(ActionEvent e) {
				shiftTime(1);
			}
		};
		
		Action get = new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				if(currentMap[p1.getYCoord()][p1.getXCoord()].hasItems()) {
					p1.pushToInventory(currentMap[p1.getYCoord()][p1.getXCoord()].popItem());
					shiftTime(0);
				}
			}
		};
		
		Action toggleInventory = new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				if(focusState == DirectionMode.FOCUS_MAP) {
					focusState = DirectionMode.FOCUS_INVENTORY;
				} else {
					focusState = DirectionMode.FOCUS_MAP;
				}
				shiftTime(0);
			}
		};
		
		this.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ESCAPE"), "exitProgram");
		this.getActionMap().put("exitProgram", exitProgram);
		
		this.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('a'), "moveLeft");
		this.getActionMap().put("moveLeft", move);
		
		this.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('d'), "moveRight");
		this.getActionMap().put("moveRight", move);
		
		this.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('w'), "moveUp");
		this.getActionMap().put("moveUp", move);
		
		this.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('x'), "moveDown");
		this.getActionMap().put("moveDown", move);
		
		this.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('q'), "moveNW");
		this.getActionMap().put("moveNW", move);
		
		this.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('e'), "moveNE");
		this.getActionMap().put("moveNE", move);

		this.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('z'), "moveSW");
		this.getActionMap().put("moveSW", move);

		this.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('c'), "moveSE");
		this.getActionMap().put("moveSE", move);
		
		this.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('s'), "stall1sec");
		this.getActionMap().put("stall1sec", stall1sec);
		
		this.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('g'), "get");
		this.getActionMap().put("get", get);
		
		this.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('i'), "toggleInventory");
		this.getActionMap().put("toggleInventory", toggleInventory);
	}
	
	/**
	 * Imports a .dat file from the current file path parameter. This is used to test unlike the ones 
	 * randomly generated in the game. This is used to test various mechanics and check for errors
	 * and quirks in difficulty.
	 * 
	 * File must be structured as so (everything being separated by new lines):
	 * Map Name
	 * Map Dimensions (x, then y separated by newline)
	 * Player Location(x, then y separated by newline)
	 * Grid Layout, according to Map Dimensions
	 * 
	 * @param fileName	the file name or the file path of the target .dat file.
	 * @throws IOException
	 */
	private void importMap(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		mapName = br.readLine();
		currentMap = new Tile[Integer.parseInt(br.readLine())][Integer.parseInt(br.readLine())];
		p1 = new Player("Chara", Integer.parseInt(br.readLine()), Integer.parseInt(br.readLine()), 6.5);
		
		for(int y = 0; y < currentMap.length; y++) {
			String currLine = br.readLine();
			for(int x = 0; x < currLine.length(); x++) {
				currentMap[y][x] = new Tile(currLine.charAt(x));
			}
		}
		
		br.close();
	}
	
	/**
	 * Imports all the images, tilesets, and other graphics used in the game. 
	 * @throws IOException
	 */
	private void importImages() throws IOException {
		t_ground = ImageIO.read(new File("images/Ground.png"));
		t_vials = ImageIO.read(new File("images/Vials.png"));
		t_icons = ImageIO.read(new File("images/icons.png"));
	}
	
	/**
	 * Draws the entire GUI for the program.
	 * @param g
	 */
	protected void paintComponent(Graphics g) {
		//Painting methods. These must be done last.
		super.paintComponent(g);
		this.drawGrid(g);
		this.drawPlayerInfo(g);
	}
	
	/**
	 * Draws the grid and message GUI. First draws the grid, then the messages.
	 * @param g
	 */
	private void drawGrid(Graphics g) {
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString(this.mapName, 20, 25);
		g.drawString("Time: " + this.time + " seconds", 200, 25);
		
		//Determine bounds of currentMap to draw 
		int startingXBound = p1.getXCoord() - ((viewportDimension - 1) / 2);
		int startingYBound = p1.getYCoord() - ((viewportDimension - 1) / 2);
		
		if(startingXBound < 0) { startingXBound = 0; }
		if(startingYBound < 0) { startingYBound = 0; }
		
		if(startingXBound > currentMap[0].length - viewportDimension) { startingXBound = currentMap[0].length - viewportDimension; }
		if(startingYBound > currentMap.length - viewportDimension) { startingYBound = currentMap.length - viewportDimension; }
		
		for(int y = startingYBound; y < startingYBound + viewportDimension; y++) {
			for(int x = startingXBound; x < startingXBound + viewportDimension; x++) {
				
				int dX = x - startingXBound;
				int dY = y - startingYBound;
				
				//Draws the base tile based on what tile representation it is.
				switch(currentMap[y][x].getRep()) {
					case '0':
						drawImageFromTileset(g, t_ground, 20, 35, Tile.tileSize, dX * Tile.tileSize, dY * Tile.tileSize, 0, 0);
						break;
					case 'X':
						switch(this.determineWallType(x, y)) {
							case "+":
								drawImageFromTileset(g, t_ground, 20, 35, Tile.tileSize, dX * Tile.tileSize, dY * Tile.tileSize, 288, 0);
								break;
							case "upT":
								drawImageFromTileset(g, t_ground, 20, 35, Tile.tileSize, dX * Tile.tileSize, dY * Tile.tileSize, 144, 36);
								break;
							case "downT":
								drawImageFromTileset(g, t_ground, 20, 35, Tile.tileSize, dX * Tile.tileSize, dY * Tile.tileSize, 180, 36);
								break;
							case "leftT":
								drawImageFromTileset(g, t_ground, 20, 35, Tile.tileSize, dX * Tile.tileSize, dY * Tile.tileSize, 144, 0);
								break;
							case "rightT":
								drawImageFromTileset(g, t_ground, 20, 35, Tile.tileSize, dX * Tile.tileSize, dY * Tile.tileSize, 180, 0);
								break;
							case "NW":
								drawImageFromTileset(g, t_ground, 20, 35, Tile.tileSize, dX * Tile.tileSize, dY * Tile.tileSize, 36, 0);
								break;
							case "NE":
								drawImageFromTileset(g, t_ground, 20, 35, Tile.tileSize, dX * Tile.tileSize, dY * Tile.tileSize, 108, 0);
								break;
							case "SW":
								drawImageFromTileset(g, t_ground, 20, 35, Tile.tileSize, dX * Tile.tileSize, dY * Tile.tileSize, 36, 36);
								break;
							case "SE":
								drawImageFromTileset(g, t_ground, 20, 35, Tile.tileSize, dX * Tile.tileSize, dY * Tile.tileSize, 72, 36);
								break;
							case "horiz":
								drawImageFromTileset(g, t_ground, 20, 35, Tile.tileSize, dX * Tile.tileSize, dY * Tile.tileSize, 72, 0);
								break;
							case "vert":
								drawImageFromTileset(g, t_ground, 20, 35, Tile.tileSize, dX * Tile.tileSize, dY * Tile.tileSize, 108, 36);
								break;
							case "N":
								drawImageFromTileset(g, t_ground, 20, 35, Tile.tileSize, dX * Tile.tileSize, dY * Tile.tileSize, 216, 36);
								break;
							case "S":
								drawImageFromTileset(g, t_ground, 20, 35, Tile.tileSize, dX * Tile.tileSize, dY * Tile.tileSize, 252, 0);
								break;
							case "E":
								drawImageFromTileset(g, t_ground, 20, 35, Tile.tileSize, dX * Tile.tileSize, dY * Tile.tileSize, 216, 0);
								break;
							case "W":
								drawImageFromTileset(g, t_ground, 20, 35, Tile.tileSize, dX * Tile.tileSize, dY * Tile.tileSize, 252, 36);
								break;
							case "island":
								drawImageFromTileset(g, t_ground, 20, 35, Tile.tileSize, dX * Tile.tileSize, dY * Tile.tileSize, 0, 36);
								break;
							default:
								g.setColor(Color.GREEN);
								g.fillRect(dX * Tile.tileSize + 20, dY * Tile.tileSize + 35, Tile.tileSize, Tile.tileSize); 
						}
						break;
					default:
						g.setColor(Color.PINK);
						break;
				}
				
				if(x == p1.getXCoord() && y == p1.getYCoord()) {
					g.setColor(Color.YELLOW);
					g.fillRect(dX * Tile.tileSize + 20, dY * Tile.tileSize + 35, Tile.tileSize, Tile.tileSize);
				}
				
				/* For tile coordinate reference */
				//g.setColor(Color.WHITE);
				//g.setFont(new Font("Georgia", Font.PLAIN, 10));
				//g.drawString(x + "," + y, x * Tile.tileSize + 28, y * Tile.tileSize + 50);
				
				//Draws Items currently on floor.
				if(currentMap[y][x].hasItems()) {
					drawImageFromTileset(g, t_vials, 20, 35, Tile.tileSize, dX * Tile.tileSize, dY * Tile.tileSize, currentMap[y][x].peekItem().getxStart(), currentMap[y][x].peekItem().getyStart());
				}
				
				//Draws light shading; blacks out tile if unseen, light shades if was once seen but currently
				//is not, and ignore shading if tile is in sight.
				if(currentMap[y][x].getRevealed() == 0) {
					g.setColor(new Color(0, 0, 0));
					g.fillRect(dX * Tile.tileSize + 20, dY * Tile.tileSize + 35, Tile.tileSize, Tile.tileSize);
				} else if(currentMap[y][x].getRevealed() == 1) {
					g.setColor(new Color(0, 0, 0, 100));
					g.fillRect(dX * Tile.tileSize + 20, dY * Tile.tileSize + 35, Tile.tileSize, Tile.tileSize);
				}
			}
		}
		
		g.setColor(new Color(255, 255, 255));
		g.fillRect(20, 675, 612, 125);
		
		g.setFont(new Font("Arial", Font.PLAIN, 15));
		g.setColor(new Color(0, 0, 0));
		for(int x = 0; x < Math.min(messageStack.size(), 6); x++) {
			g.setColor(new Color(messageStack.get(messageStack.size() - 1 - x).color.getRed(),
					   messageStack.get(messageStack.size() - 1 - x).color.getGreen(),
					   messageStack.get(messageStack.size() - 1 - x).color.getBlue(),
					   255 - (x * 42)));
			g.drawString(messageStack.get(messageStack.size() - 1 - x).text, 25, (100 - (x * 20)) + 765);
		}
	}
	
	/**
	 * Determines the wall piece by determining whether or not the cardinally adjacent tiles are walls.
	 * @param x The X-coordinate of the wall piece in question.
	 * @param y The Y-coordinate of the wall piece in question.
	 * @return A string that is based on the number and direction(s) of adjacent wall tiles.
	 */
	private String determineWallType(int x, int y) {
		boolean Nwalled = (y != 0) && (currentMap[y-1][x].getRep() == 'X');
		boolean Swalled = (y != currentMap.length - 1) && (currentMap[y+1][x].getRep() == 'X');
		boolean Wwalled = (x != 0) && (currentMap[y][x-1].getRep() == 'X');
		boolean Ewalled = (x != currentMap.length - 1) && (currentMap[y][x+1].getRep() == 'X');
		
		//Absolutely dreadful. Wow.
		if(Swalled && Ewalled && Nwalled && Wwalled) {
			return "+";
		} else if(Swalled && Ewalled && !Nwalled && Wwalled) {
			return "upT";
		} else if(!Swalled && Ewalled && Nwalled && Wwalled) {
			return "downT";
		} else if(Swalled && Ewalled && Nwalled && !Wwalled) {
			return "leftT";
		} else if(Swalled && !Ewalled && Nwalled && Wwalled) {
			return "rightT";
		} else if(!Swalled && Ewalled && !Nwalled && Wwalled) {
			return "horiz";
		} else if(Swalled && !Ewalled && Nwalled && !Wwalled) {
			return "vert";
		} else if(Swalled && Ewalled && !Nwalled && !Wwalled) {
			return "NW";
		} else if(Swalled && !Ewalled && !Nwalled && Wwalled) {
			return "NE";
		} else if(!Swalled && Ewalled && Nwalled && !Wwalled) {
			return "SW";
		} else if(!Swalled && !Ewalled && Nwalled && Wwalled) {
			return "SE";
		} else if(!Swalled && !Ewalled && Nwalled && !Wwalled) {
			return "N";
		} else if(Swalled && !Ewalled && !Nwalled && !Wwalled) {
			return "S";
		} else if(!Swalled && Ewalled && !Nwalled && !Wwalled) {
			return "E";
		} else if(!Swalled && !Ewalled && !Nwalled && Wwalled) {
			return "W";
		} else {
			return "island";
		}
	}
	
	/**
	 * Draws image from a specified tileset onto the grid; note that this method can only be correctly called if
	 * the destination of the image is onto the playing grid. This is meant to de-clutter the main paintComponent method.
	 * @param g
	 * @param i The tileset from which to derive the tile.
	 * @param marginX The X margin between the grid and the JPanel. May also include the difference between the std. tile size (36px.) and the tile size.
	 * @param marginY The Y margin between the grid and the JPanel. May also include the difference between the std. tile size (36px.) and the tile size.
	 * @param tileSize The size of the tile in question. Because this method only applies to drawings on the grid, all tiles must be square.
	 * @param displayOffsetX The X distance from the origin on the grid.
	 * @param displayOffsetY The Y distance from the origin on the grid.
	 * @param imageOffsetX The X distance from the origin on the tileset.
	 * @param imageOffsetY The Y distance from the origin on the tileset.
	 */
	private void drawImageFromTileset(Graphics g, Image i, int marginX, int marginY, int tileSize, int displayOffsetX, int displayOffsetY, int imageOffsetX, int imageOffsetY) {
		g.drawImage(i, marginX + displayOffsetX, marginY + displayOffsetY, marginX + displayOffsetX + tileSize, marginY + displayOffsetY + tileSize, imageOffsetX, imageOffsetY, tileSize + imageOffsetX, tileSize + imageOffsetY, this);
	}
	
	/**
	 * Draws the block containing the player information; this area is located to the right
	 * of the grid. Note that this area also encapsulates other methods, which will be added later.
	 * TODO: WOW, this is magic. De-uglify this asap.
	 * @param g
	 */
	private void drawPlayerInfo(Graphics g) {
		int playerInfoLeftMargin = viewportDimension*Tile.tileSize + leftMargin;
		
		g.setColor(new Color(220, 220, 220));
		g.fillRect(viewportDimension*Tile.tileSize + leftMargin, topMargin, inventoryWidth*Tile.tileSize, viewportDimension*Tile.tileSize);
		
		//Draws player id
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString(p1.getName() + ", the " + p1.getTitle() + " " + p1.getSpecies(), 19*36 + 40 + 10, 60);
		
		//Draws player inventory
		int inventoryTopMargin = topMargin + Tile.tileSize*(viewportDimension - inventoryHeight);
		g.setColor(new Color(200, 200, 200));
		for(int x = 0; x < p1.getInventory().length; x++) {
			if((x/inventoryWidth)%2 != x%2) {
				g.setColor(new Color(200, 200, 200));
			} else {
				g.setColor(new Color(170, 170, 170));
			}
			g.fillRect((x%inventoryWidth)*Tile.tileSize + playerInfoLeftMargin, (x/inventoryWidth)*Tile.tileSize + inventoryTopMargin, Tile.tileSize, Tile.tileSize);
			
			if(inventorySlotSelected == x && focusState == DirectionMode.FOCUS_INVENTORY) {
				drawImageFromTileset(g, t_icons, playerInfoLeftMargin, inventoryTopMargin, Tile.tileSize, (x%inventoryWidth) * Tile.tileSize, (x/inventoryWidth) * Tile.tileSize, 0, 0);
			}
			if(p1.getItemAt(x) != null) {
				drawImageFromTileset(g, t_vials, playerInfoLeftMargin, inventoryTopMargin, Tile.tileSize, (x%inventoryWidth) * Tile.tileSize, (x/inventoryWidth) * Tile.tileSize, p1.getItemAt(x).getxStart(), p1.getItemAt(x).getyStart());
			}
		}
	}
	
	/**
	 * Increases the time by the timeAddition parameter given; this method also checks all enemies and others
	 * and does their actions if the time is right. shiftTime is called after the player has performed a time-consuming action.
	 * It is extremely important to note that shiftTime also repaints the grid; this is something that will be fixed in a later
	 * update, but is sufficient for the time being.
	 * @param timeAddition	The amount of time done by the player.
	 */
	public void shiftTime(double timeAddition) {
		time += timeAddition;
		this.calcSightBoundaries();
		repaint();
	}
	
	/**
	 * Determines the Tiles (after movement) which are seen, unseen, and seen at least once, and adjusts each Tile's
	 * revealStatus field accordingly. In this method, all Tiles within a radius of the player's sightRadius that are also
	 * impassable are put in an ArrayList; these are then converted to vectors that go from the player's position to the tile
	 * in question. These vectors have a unsight threshold dependent on the magnitude and direction which is represented by a
	 * maximum and minimum angle measure. All tiles in the sightRadius are then checked against each vector; if any of them both
	 * exceed the magnitude and go in the threshold of a vector, then it is currently unseen.
	 */
	private void calcSightBoundaries() {
		ArrayList<Vector> walls = new ArrayList<Vector>();
		boolean[][] isSeen = new boolean[currentMap.length][currentMap[0].length];
		
		for(int y = 0; y < currentMap.length; y++) {
			for(int x = 0; x < currentMap[y].length; x++) {
				//If tile is within unimpeded sight radius
				if(Vector.hypotenuse(p1.getXCoord() - x, p1.getYCoord() - y) > p1.getSightRadius()) {
						isSeen[y][x] = false;
				} else {
					isSeen[y][x] = true;
					//If tile at location is a wall
					if(currentMap[y][x].isImpassable()) {
						walls.add(new Vector(p1.getXCoord() - x, p1.getYCoord() - y));
					}
				}
			}
		}
		
		for(int y = 0; y < currentMap.length; y++) {
			for(int x = 0; x < currentMap[y].length; x++) {
				double mag = Vector.hypotenuse(p1.getXCoord() - x, p1.getYCoord() - y);
				double dir = Vector.angleMeasure(p1.getXCoord() - x, p1.getYCoord() - y);
				if(mag <= p1.getSightRadius()) {
					for(int a = 0; a < walls.size(); a++) {
						if(walls.get(a).getMagnitude() < mag && walls.get(a).getDirection() == 0 && 
								(walls.get(a).getLowerBound() < dir || walls.get(a).getUpperBound() > dir)) {
							isSeen[y][x] = false;
						} else if(walls.get(a).getMagnitude() < mag && (walls.get(a).getLowerBound() < dir && walls.get(a).getUpperBound() > dir)) {
							isSeen[y][x] = false;
						}
					}
				}
			}
		}
		
		for(int y = 0; y < currentMap.length; y++) {
			for(int x = 0; x < currentMap[y].length; x++) {
				if(isSeen[y][x]) {
					currentMap[y][x].setRevealed(2);
				} else {
					if(currentMap[y][x].getRevealed() == 2) {
						currentMap[y][x].setRevealed(1);
					}
				}
			}
		}
	}
}
