package display;

import comm.MessageManager;
import display.music.AudioPlayer;
import entity.EnemyGenerator;
import entity.Player;
import event.EventQueue;
import event.Instruction;
import event.Response;
import grid.CompositeGrid;
import grid.Tile;
import item.ItemLoader;
import item.ItemPromptLoader;
import skill.SkillLoader;

import java.awt.BorderLayout;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.*;

/**
 * The whole of the GUI of Asphodel Sky. Contains one instance of each subclass of the DisplayComponent class,
 * and coordinates, updates, and sends information about each component.
 * @author Matt Imel
 *
 */
public class GameSession extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * CompositeGrid component, based on the left side of the window between the header and footer.
	 */
	private GUIFocus gc;
	
	/**
	 * Sidebar component, based on the right side of the window between the header and footer.
	 */
	private GUISidebar sc;
	
	/**
	 * Footer component, based on the bottom of the window.
	 */
	private GUIFooter fc;

	/**
	 * The current display configuration.
	 */
	private DisplayConfiguration currentConfig;

	private AudioPlayer player;

	public GameSession(int winWidth, int winHeight, AudioPlayer ap) {
		this.setLayout(new BorderLayout());
		
		//Magic numbers soon to be replaced
		this.gc = new GUIFocus(0, 0, 624, 624, 48);
		this.sc = new GUISidebar(624, 50, winWidth - 624, 624);
		this.fc = new GUIFooter(0, 624, winWidth, winHeight - 624);

		this.add(gc, BorderLayout.WEST);
		this.add(sc, BorderLayout.EAST);
		this.add(fc, BorderLayout.SOUTH);

		this.currentConfig = DisplayConfiguration.DEFAULT;

		player = ap;

		initializeGameSession();
	}

	GUIFocus getFocus() { return gc; }
	GUISidebar getSidebar() { return sc;}
	GUIFooter getFooter() { return fc; }
	DisplayConfiguration getConfig() {
		return currentConfig;
	}

	/**
	 * Switches the configuration of the display. The configuration determines the information that is
	 * displayed on the screen. When switching the state, the previous state is overridden.
	 * @param newConfig The new configuration to use.
	 */
	void switchState(DisplayConfiguration newConfig) {
		switch(newConfig) {
			case DEFAULT:
				gc.setCurrentMode(FocusMode.PLAYER_FOCUS);
				sc.setCurrentMode(SidebarMode.COMBATANT);
				fc.setCurrentMode(FooterMode.MESSAGES);
				break;

			case TILE_SELECT:
				gc.setCurrentMode(FocusMode.SELECTION);
				sc.setCurrentMode(SidebarMode.COMBATANT);
				fc.setCurrentMode(FooterMode.DESCRIPTION);
				break;

			case SKILL_SELECT:
				gc.setCurrentMode(FocusMode.PLAYER_FOCUS);
				sc.setCurrentMode(SidebarMode.SKILL_SELECTION);
				fc.setCurrentMode(FooterMode.DESCRIPTION);
				break;

			case INVENTORY_SELECT:
				gc.setCurrentMode(FocusMode.PLAYER_FOCUS);
				sc.setCurrentMode(SidebarMode.ITEM_SELECTION);
				fc.setCurrentMode(FooterMode.DESCRIPTION);
				break;

			case DIALOGUE:
				gc.setCurrentMode(FocusMode.PLAYER_FOCUS);
				sc.setCurrentMode(SidebarMode.COMBATANT);
				fc.setCurrentMode(FooterMode.DIALOGUE);
				break;
		}

		currentConfig = newConfig;
	}

	private void initializeGameSession() {
		//Initialize message manager.
		ExecutorService threadList = Executors.newFixedThreadPool(2);
		MessageManager mm = new MessageManager(getFooter());
		threadList.execute(mm);

		Player p1 = new Player("Place Holder", "Apprentice", 1000, 22, getSidebar());

		//Mapping/Images/Assets loading.
		ImageAssets.loadImageMapping();
		ItemLoader.loadItemEffectMapping("map/item_effectmap.dat");
		ItemPromptLoader.loadItemPromptMapping("map/item_promptmap.dat");
		SkillLoader.initializeSkillMap("map/skill_effectmap.dat");
		Tile.loadTraitMapping("map/terr_infomap.dat");
		EnemyGenerator.loadEnemyMapping("map/enemy_infomap.dat");
		Instruction.loadInstructionSet();
		Response.loadResponseTable("map/responsemap.dat");

		p1.getInventory().insertItem(ItemLoader.getItemById(0), 1);
		p1.getSkillSet().addSkill(SkillLoader.getSkillByID(0));
		p1.getSkillSet().addSkill(SkillLoader.getSkillByID(1));

		//Initializing SimpleEvent Queue.
		EventQueue eq = new EventQueue();

		//PLAYGROUND TEMPORARY
		CompositeGrid compositeGrid = new CompositeGrid(getFocus());

		compositeGrid.addCombatant(p1, 1, 1);
		compositeGrid.bindTo(0);

		compositeGrid.addItem(0, 4, 4);
		compositeGrid.addItem(1, 4, 8);
		for(int x = 0; x < 10; x++) {
			compositeGrid.addCombatant(EnemyGenerator.getEnemyByName("Khweiri Dervish"), (3 + 7 * x) % 10, 3 + x);
		}

		compositeGrid.addCombatant(EnemyGenerator.getEnemyByName("Bilge Rat"), 12, 4);
		compositeGrid.addCombatant(EnemyGenerator.getEnemyByName("Fireball"), 13, 4);

		player.playSong("AttemptNo1.mp3");


		repaint();
		//END PLAYGROUND

		DisplayKeyBindings.initKeyBinds(this, compositeGrid, p1, mm, eq);
	}
}