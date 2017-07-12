package control;

//TODO LIST
//Test redraw methods.
//AI operations must return array of Events.
//Maybe add different methods for item select/tile select.
//Event queue testing, AI operation testing (NOT implementation)
//General cleanup, resolve TODOs.
//Consider moving mapping to display.
//Persistent//Lag on startup - examine.


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.*;

import comm.MessageManager;
import display.Display;
import display.DisplayConfiguration;
import display.ImageAssets;
import entity.*;
import event.Event;
import event.EventQueue;
import event.Instruction;
import event.Opcode;
import grid.*;
import item.Item;

/**
 * The executing class.
 * @author Matt Imel
 */
public class ASControl {
	
	/**
	 * You!
	 */
	private static Player p1;

	/**
	 * The current grid on which the game currently operates.
	 */
	private static Grid grid;
	
	/**
	 * Manager for the message service.
	 */
	private static MessageManager mm;

	/**
	 * The queue of events to take place.
	 */
	private static EventQueue eq;

	/**
	 * An event that is pushed onto the event queue as a result of a key press.
	 */
	private static Event pendingEvent;

	/**
	 * The Input map that is
	 */
	private static InputMap restrictedSelectInputMap;

	private static ActionMap restrictedSelectActionMap;

	public static void main(String args[]) {
		JFrame gameWindow = new JFrame("Asphodel Sky");
		gameWindow.setMinimumSize(new Dimension(1200, 900));
		gameWindow.setMaximumSize(new Dimension(1920, 1080));
		gameWindow.setUndecorated(true);
		gameWindow.setLocationRelativeTo(null); //Centers window
		
		Display game = new Display(1200, 900);
		initKeyBinds(game);
		
		gameWindow.add(game);

		restrictedSelectInputMap = new InputMap();
		restrictedSelectActionMap = new ActionMap();

		//Initialize message manager.
		ExecutorService threadList = Executors.newFixedThreadPool(2);
		mm = new MessageManager(game.getFooter());
		threadList.execute(mm);

		//TODO: p1 must be instantiated before enemy map loading in order to ensure that p1 has id 0; fix.
		p1 = new Player("Place Holder", "Apprentice",  16, game.getSidebar());

		//Mapping/Images/Assets loading.
		ImageAssets.loadImageMapping();
		Item.loadItemMapping("map/item_effectmap.dat");
		Tile.loadTraitMapping("map/terr_infomap.dat");
		EnemyGenerator.loadEnemyMapping("map/enemy_infomap.dat");
		Instruction.loadInstructionSet();

		//Initializing Event Queue.
		eq = new EventQueue();
		
		//PLAYGROUND TEMPORARY
		grid = new Grid(game.getHeader(), game.getFocus());

		grid.addCombatant(p1, 1, 1);

		grid.addItem("Cardiotic Fluid", 4, 4);
		grid.addItem("Solution of Finesse", 5, 5);

		eq.addEvent(4, 100, Opcode.ECHOPARAM, 0, 100, 30);
		eq.addEvent(4, 50, Opcode.ECHOPARAM, 1, 50, 40);
		eq.addEvent(3, 100, Opcode.ECHOPARAM, 2, 6, 20);
		eq.addEvent(2, 400, Opcode.ADJUSTHP, 0, -4, 2);
		eq.progressTimeBy(5, grid);

		grid.addCombatant("Khweiri Dervish", 6, 6);

		updateOutput();
		game.repaint();
		//END PLAYGROUND
		
		gameWindow.pack();
		
		gameWindow.setVisible(true);
	}
	
	/**
	 * Creates keybinds for the game.
	 * @param game The GUI.
	 */
	private static void initKeyBinds(Display game) {
		Action exitProgram = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};
		
		Action move = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				int xOffset = 0;
				int yOffset = 0;
				
				switch(e.getActionCommand().charAt(0)) {
					case 'a': //W
						xOffset = -1;
						break;
					case 'w': //N
						yOffset = -1;
						break;
					case 'd': //E
						xOffset = 1;
						break;
					case 'x': //S
						yOffset = 1;
						break;
					case 'q': //NW
						xOffset = -1;
						yOffset = -1;
						break;
					case 'e': //NE
						xOffset = 1;
						yOffset = -1;
						break;
					case 'z': //SW
						xOffset = -1;
						yOffset = 1;
						break;
					case 'c': //SE
						xOffset = 1;
						yOffset = 1;
						break;
				}
				
				if(game.getConfig() == DisplayConfiguration.INVENTORY_SELECT) {
					
					//Moves the cursor over the inventory. TODO: Magic.
					if(p1.getInventory().setFocus(xOffset * 3 + yOffset)) {
						p1.updatePlayer();
						mm.loadSourceDescPair(p1.getInventory().getFocusedItem().getName(), p1.getInventory().getFocusedItem().getVisualDescription());
					}
					
				} else if(game.getConfig() == DisplayConfiguration.DEFAULT) {
					
					//Moves the player.					
					grid.moveCombatant(0, grid.getXOfCombatant(0) + xOffset, grid.getYOfCombatant(0) + yOffset);
					eq.progressTimeBy(1, grid);

				} else if(game.getConfig() == DisplayConfiguration.TILE_SELECT) {

					//Moves the crosshair.
					grid.switchFocus(xOffset, yOffset);

					//The following if-else chain searches a tile by order of priority; First for occupants, then items, then floor features, then floor types.
					if (grid.getFocusedTile().getOccupant() != null) {
						//If the crosshair overlaps an occupant, prints their name, title, and description to the Message manager.
						Combatant o = grid.getFocusedTile().getOccupant();

						mm.loadSourceDescPair(o.toString(), o.getDesc());
					} else if (!grid.getFocusedTile().getCatalog().isEmpty()) {
						//If the crosshair overlaps an item, prints the items name and description to the Message manager.
						Item i = grid.getFocusedTile().getCatalog().getFocusedItem();
						mm.loadSourceDescPair(i.getName(), i.getVisualDescription());
					} else {
						//If the crosshair overlaps nothing, prints the tile name and description.
						Tile t = grid.getFocusedTile();
						mm.loadSourceDescPair(t.getName(), t.getDesc());
					}
				}

				updateOutput();
				game.repaint();
			}
		};

		Action confirm = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

			}
		};
		
		Action get = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//If the tile the player is on contains at least one item,
				//then the item is transfered into the player's inventory.
				if(!grid.getTileAt(grid.getXOfCombatant(0), grid.getYOfCombatant(0)).getCatalog().isEmpty()) {
					p1.getInventory().transferFrom(grid.getTileAt(grid.getXOfCombatant(0), grid.getYOfCombatant(0)).getCatalog());
					mm.insertMessage("Picked up items.");

					eq.progressTimeBy(10, grid);
					updateOutput();
					game.repaint();
				} else {
					mm.insertMessage("There are no items to pick up.");
				}
			}
		};
		
		Action recon = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {

				if(game.getConfig() == DisplayConfiguration.DEFAULT) {

					game.switchState(DisplayConfiguration.TILE_SELECT);
					grid.setFocusedTile(grid.getXOfCombatant(0), grid.getYOfCombatant(0));
					mm.loadSourceDescPair(p1.toString(), p1.getDesc());

				} else if(game.getConfig() == DisplayConfiguration.TILE_SELECT) {

					game.switchState(DisplayConfiguration.DEFAULT);
					grid.clearFocusedTile();
					mm.insertMessage("Exiting Recon mode.");

				}

				updateOutput();
				game.repaint();
			}
		};
		
		Action inventory = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(game.getConfig() == DisplayConfiguration.DEFAULT) {
					
					if(p1.getInventory().isEmpty()) {
						mm.insertMessage("You do not have any items.");
						return;
					}

					game.switchState(DisplayConfiguration.INVENTORY_SELECT);
					String name = p1.getInventory().getFocusedItem().getName();
					String desc = p1.getInventory().getFocusedItem().getVisualDescription();
					mm.loadSourceDescPair(name, desc);

				} else if(game.getConfig() == DisplayConfiguration.INVENTORY_SELECT) {
					game.switchState(DisplayConfiguration.DEFAULT);
				}

				updateOutput();
				game.repaint();
			}
		};
		
		Action use = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(game.getConfig() == DisplayConfiguration.INVENTORY_SELECT) {
					eq.addEvents(p1.getInventory().consumeItem(p1.getInventory().getFocusedItem().getId()).use(p1, grid));
					p1.getInventory().resetFocusIndex();

					mm.insertMessage("Consumed.");

					eq.progressTimeInstantaneous(grid);
					game.switchState(DisplayConfiguration.DEFAULT);
					updateOutput();
					game.repaint();
				} else {
					inventory.actionPerformed(null);
				}
			}
		};

		Action toss = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(game.getConfig() == DisplayConfiguration.DEFAULT) {

				}
			}
		};
		
		//ESC = Exits the program.
		game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ESCAPE"), "exitProgram");
		game.getActionMap().put("exitProgram", exitProgram);
		
		//Q,W,E,A,D,Z,X,C = Move. The following eight binds reflect moving.
		game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('a'), "moveLeft");
		game.getActionMap().put("moveLeft", move);
		
		game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('w'), "moveUp");
		game.getActionMap().put("moveUp", move);
		
		game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('d'), "moveRight");
		game.getActionMap().put("moveRight", move);

		game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('x'), "moveDown");
		game.getActionMap().put("moveDown", move);

		game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('q'), "moveNW");
		game.getActionMap().put("moveNW", move);
		
		game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('e'), "moveNE");
		game.getActionMap().put("moveNE", move);
		
		game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('z'), "moveSW");
		game.getActionMap().put("moveSW", move);
		
		game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('c'), "moveSE");
		game.getActionMap().put("moveSE", move);

		//ENTER = yes.
		game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ENTER"), "confirm");
		game.getActionMap().put("confirm", confirm);

		/*//All keybinds needed for restricted set are above.
		for(KeyStroke key : game.getInputMap().keys()) {
			Object value = game.getInputMap().get(key);
			restrictedSelectInputMap.put(key, value);
			restrictedSelectActionMap.put(value, game.getActionMap().get(value));
		}*/

		//G = Get.
		game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('g'), "get");
		game.getActionMap().put("get", get);
		
		//R = Recon.
		game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('r'), "recon");
		game.getActionMap().put("recon", recon);

		//I = Inventory.
		game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('i'), "inventory");
		game.getActionMap().put("inventory", inventory);
		
		//U = Use.
		game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('u'), "use");
		game.getActionMap().put("use", use);

		//T = Toss.
		game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('t'), "toss");
		game.getActionMap().put("toss", toss);
	}

	private static void updateOutput() {
		grid.updateHeader(eq.getTime());
		//TODO magic
		grid.updateGrid(13, 13);
		p1.updatePlayer();
	}

	/**
	 *
	 */
	private static void pushPendingEvent() {

	}
}
