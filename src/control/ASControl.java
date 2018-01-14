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

		Player p1 = new Player("Place Holder", "Apprentice", 47, 22, game.getSidebar());

		//Mapping/Images/Assets loading.
		ImageAssets.loadImageMapping();
		Item.loadItemMapping("map/item_effectmap.dat");
		Tile.loadTraitMapping("map/terr_infomap.dat");
		EnemyGenerator.loadEnemyMapping("map/enemy_infomap.dat");
		Instruction.loadInstructionSet();
		Response.loadResponseTable("map/responsemap.dat");

		p1.getInventory().insertItem(Item.getItemById(0), 1);

		//Initializing SimpleEvent Queue.
		EventQueue eq = new EventQueue();
		
		//PLAYGROUND TEMPORARY
		CompositeGrid compositeGrid = new CompositeGrid(game.getFocus());

		compositeGrid.addCombatant(p1, 1, 1);
		compositeGrid.bindTo(0);

		compositeGrid.addItem(0, 4, 4);
		for(int x = 0; x < 10; x++) {
			compositeGrid.addCombatant(EnemyGenerator.getEnemyByName("Khweiri Dervish"), (3 + 7 * x) % 10, 3 + x);
		}


		game.repaint();
		//END PLAYGROUND

		DisplayKeyBindings.initKeyBinds(game, compositeGrid, p1, mm, eq);
		gameWindow.pack();
		
		gameWindow.setVisible(true);
	}
}
