package control;

//TODO LIST
///// Possible SoIfI problems with the storage of images in the Item class
///// rather than the GUI classes; consider moving.
//5.10//// Implement enemies.

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import comm.MessageManager;
import display.Display;
import entity.*;
import grid.*;
import item.Vial;

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
	
	public static void main(String args[]) {
		JFrame gameWindow = new JFrame("Asphodel Sky");
		gameWindow.setMinimumSize(new Dimension(1200, 900));
		gameWindow.setMaximumSize(new Dimension(1920, 1080));
		gameWindow.setUndecorated(true);
		gameWindow.setLocationRelativeTo(null); //Centers window
		
		Display game = new Display(1200, 900);
		initKeyBinds(game);
		
		gameWindow.add(game);
		
		ExecutorService threadList = Executors.newFixedThreadPool(2);
		
		mm = new MessageManager(game.getFooter());
		threadList.execute(mm);
		
		//PLAYGROUND
		grid = new Grid(game.getFocus());
		
		System.out.println(grid);
		p1 = new Player("Place Holder", "Apprentice", 1, 1, 16, game.getSidebar());
		
		grid.getTileAt(1, 1).fillOccupant(p1);
		grid.getTileAt(1, 4).getCatalog().insertItem(Vial.CATALOG_VIAL[0]);
		grid.getTileAt(1, 3).getCatalog().insertItem(Vial.CATALOG_VIAL[1]);
		grid.getTileAt(5, 6).getCatalog().insertItem(Vial.CATALOG_VIAL[0]);
		grid.getTileAt(2, 6).getCatalog().insertItem(Vial.CATALOG_VIAL[0]);
		
		grid.drawGrid(13,13);
		p1.drawPlayer();
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
				long start = System.currentTimeMillis(); //TEMP
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
				
				if(game.getSidebarState().equals("inventory")) {
					
					//Moves the cursor over the inventory. TODO: Magic.
					if(p1.getInventory().setFocus(xOffset * 3 + yOffset)) {
						p1.drawPlayer();
						mm.loadSourceDescPair(p1.getInventory().getFocusedItem().getName(), p1.getInventory().getFocusedItem().getVisualDescription());
					}
					
				} else if(game.getGridState().equals("player")) {
					
					//Moves the player.
					
					grid.moveEntity(p1.getX(), p1.getY(), xOffset, yOffset);
					mm.insertMessage(p1.getX() + "," + p1.getY());
				} else if(game.getGridState().equals("crosshair")) {
					
					//Moves the crosshair.
					
					grid.switchFocus(xOffset, yOffset);
					mm.insertMessage(p1.getX() + "," + p1.getY());
				}
				
				//TODO: Magic, for now; adjust to suit multiple viewport dimensions.
				//We draw grid instead of repainting to adjust the center point of the viewport;
				//either you, the player, or the crosshair.
				
				
				grid.drawGrid(13,13);
				p1.drawPlayer();
				System.out.println(System.currentTimeMillis() - start);
			}
		};
		
		Action get = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//If the tile the player is on contains at least one item,
				//then the item is transfered into the player's inventory.
				if(!grid.getTileAt(p1.getX(), p1.getY()).getCatalog().isEmpty()) {
					p1.getInventory().transferFrom(grid.getTileAt(p1.getX(), p1.getY()).getCatalog());
					mm.insertMessage("Picked up items.");
					game.repaint();
				} else {
					mm.insertMessage("There is no item!");
				}
			}
		};
		
		Action recon = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//Goes to recon if in default player mode (every non-grid state is free)
				if(game.inDefaultState()) {
					game.switchGridState("crosshair");
					grid.setFocusedTile(p1.getX(), p1.getY());
					mm.insertMessage("Arise!");
				} else if(game.getGridState().equals("crosshair")) {
					game.switchGridState("player");
					grid.clearFocusedTile();
					mm.insertMessage("Fallen!");
				}
				
				grid.drawGrid(13,13);
				p1.drawPlayer();
			}
		};
		
		Action inventory = new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(game.inDefaultState()) {
					
					if(p1.getInventory().isEmpty()) {
						mm.insertMessage("You do not have any items.");
						return;
					}
					
					game.switchFooterState("descript");
					game.switchSidebarState("inventory");
					
					String name = p1.getInventory().getFocusedItem().getName();
					String desc = p1.getInventory().getFocusedItem().getVisualDescription();
					
					mm.loadSourceDescPair(name, desc);
				} else if(game.getFooterState().equals("descript")) {
					game.switchFooterState("free");
					game.switchSidebarState("free");
				}
				game.repaint();
			}
		};
		
		Action use = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(game.getSidebarState().equals("inventory")) {
					p1.getInventory().consumeItem(p1.getInventory().getFocusedItem().getId()).use(p1);
					mm.insertMessage("Consumed.");
					
					game.switchFooterState("free");
					game.switchSidebarState("free");
					
					game.repaint();
				} else {
					mm.insertMessage("Not implemented.");
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
		
		//G = Get.
		game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('g'), "get");
		game.getActionMap().put("get", get);
		
		//R = Recon.
		game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('r'), "recon");
		game.getActionMap().put("recon", recon);
		
		//I = Inventory. TODO Implementing
		game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('i'), "inventory");
		game.getActionMap().put("inventory", inventory);
		
		//U = Use. TODO Implementing
		game.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('u'), "use");
		game.getActionMap().put("use", use);

	}
}
