package control;

import java.awt.Dimension;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.*;

import comm.MessageManager;
import display.Display;
import display.DisplayKeyBindings;
import display.ImageAssets;
import entity.*;
import event.EventQueue;
import event.Instruction;
import event.Response;
import grid.*;
import item.Item;

/**
 * The executing class.
 * @author Matt Imel
 */
public class ASControl {

	public static void main(String args[]) {
		JFrame gameWindow = new JFrame("Asphodel Sky");
		gameWindow.setMinimumSize(new Dimension(1200, 900));
		gameWindow.setMaximumSize(new Dimension(1920, 1080));
		gameWindow.setUndecorated(true);
		gameWindow.setLocationRelativeTo(null); //Centers window
		
		Display game = new Display(1200, 900);
		
		gameWindow.add(game);

		//Initialize message manager.
		ExecutorService threadList = Executors.newFixedThreadPool(2);
		MessageManager mm = new MessageManager(game.getFooter());
		threadList.execute(mm);

		//TODO: p1 must be instantiated before enemy map loading in order to ensure that p1 has id 0; fix.
		Player p1 = new Player("Place Holder", "Apprentice", 16, 22, game.getSidebar());

		//Mapping/Images/Assets loading.
		ImageAssets.loadImageMapping();
		Item.loadItemMapping("map/item_effectmap.dat");
		Tile.loadTraitMapping("map/terr_infomap.dat");
		EnemyGenerator.loadEnemyMapping("map/enemy_infomap.dat");
		Instruction.loadInstructionSet();
		Response.loadResponseTable("map/responsemap.dat");

		//Initializing Event Queue.
		EventQueue eq = new EventQueue();
		
		//PLAYGROUND TEMPORARY
		Grid grid = new Grid(game.getHeader(), game.getFocus());

		grid.addCombatant(p1, 1, 1);
		grid.bindFocusToPlayer();

		grid.addItem("Cardiotic Fluid", 4, 4);
		grid.addItem("Cardiotic Fluid", 4, 5);
		grid.addItem("Solution of Finesse", 3, 4);
		grid.addItem("Solution of Finesse", 5, 5);
		for(int y = 2; y < 12; y++) {
			grid.addItem("Solution of Finesse", 3, y);
		}

		eq.progressTimeBy(5, grid);

		grid.addCombatant("Khweiri Dervish", 6, 6);

		game.repaint();
		//END PLAYGROUND

		DisplayKeyBindings.initKeyBinds(game, grid, p1, mm, eq);
		gameWindow.pack();
		
		gameWindow.setVisible(true);
	}
}
