package display.game;

import comm.MessageManager;
import comm.SourceDescriptionPair;
import display.image.ImageAssets;
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

/**
 * The whole of the GUI of Asphodel Sky. Contains one instance of each subclass of the DisplayComponent class,
 * and coordinates, updates, and sends information about each component.
 * @author Matt Imel
 *
 */
public class GameSession extends GameViewObserver {

	/**
	 * The current display configuration.
	 */
	private GameView view;

	private DisplayConfiguration currentConfig;

	private AudioPlayer player;

	public GameSession(int winWidth, int winHeight, AudioPlayer ap, GameManager gm) {
		this.setLayout(new BorderLayout());

		this.setBounds(0, 0, winWidth, winHeight);
		/*
		//Magic numbers soon to be replaced
		this.sc = new GUISidebar(624, 50, winWidth - 624, 624);
		this.fc = new GUIFooter(0, 624, winWidth, winHeight - 624);

		this.add(gc, BorderLayout.WEST);
		this.add(sc, BorderLayout.EAST);
		this.add(fc, BorderLayout.SOUTH);
		*/


		this.currentConfig = DisplayConfiguration.DEFAULT;

		this.player = ap;
		this.viewManager = gm;
		this.viewManager.addObserver(this);

		initializeGameSession();
	}

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
			case DIALOGUE:
				getFooter().switchToDialogue();
				break;
			case TILE_SELECT:
			case SKILL_SELECT:
			case INVENTORY_SELECT:
				getFooter().switchToSrcDesc();
				break;
			case DEFAULT:
				getFooter().switchToMessages();
				break;
		}
		currentConfig = newConfig;
	}

	public GUISidebar getSidebar() {
		return view.getSidebar();
	}

	public GUIFooter getFooter() {
		return view.getFooter();
	}

	private void initializeGameSession() {
		MessageManager mm = new MessageManager();

		Player p1 = new Player("Place Holder", "Apprentice", 1000, 22);

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
		CompositeGrid compositeGrid = new CompositeGrid();
		SourceDescriptionPair sdp = new SourceDescriptionPair("", "");

		view = new GameView(compositeGrid, new GUISidebar(0, 0, 500, 800), new GUIFooter(500, 0, 600, 200, new FooterMessageFeed(mm), new FooterShortDescriptor(sdp), new FooterDialogue()));

		this.add(view);

		compositeGrid.addCombatant(p1, 1, 1);
		compositeGrid.bindTo(0);

		compositeGrid.addItem(0, 4, 4);
		compositeGrid.addItem(1, 4, 8);
		for(int x = 0; x < 10; x++) {
			compositeGrid.addCombatant(EnemyGenerator.getEnemyByName("Khweiri Dervish"), (3 + 7 * x) % 10, 3 + x);
		}

		compositeGrid.addCombatant(EnemyGenerator.getEnemyByName("Khweiri Dervish"), 19, 0);

		compositeGrid.addCombatant(EnemyGenerator.getEnemyByName("Bilge Rat"), 12, 4);
		compositeGrid.addCombatant(EnemyGenerator.getEnemyByName("Fireball"), 13, 4);

		player.playSong("AttemptNo1.mp3");


		repaint();
		//END PLAYGROUND

		DisplayKeyBindings.initKeyBinds(this, compositeGrid, p1, mm, sdp, eq);
	}

	public void pause() {
		viewManager.setFocusedPanel(GameSessionViewState.PAUSE_MENU_MAIN);
	}

	@Override
	public void update() {
		if(viewManager.isFocus(GameSessionViewState.GAME)) {
			enableInputs();
		} else {
			disableInputs();
		}
	}
}