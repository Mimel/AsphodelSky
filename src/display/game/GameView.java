package display.game;

import comm.MessageManager;
import comm.SourceDescriptionPair;
import display.game.focus.GUIFocus;
import display.game.footer.FooterDialogue;
import display.game.footer.FooterMessageFeed;
import display.game.footer.FooterShortDescriptor;
import display.game.footer.GUIFooter;
import display.game.sidebar.GUISidebar;
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

import java.awt.*;

/**
 * The whole of the GUI of Asphodel Sky. Contains one instance of each subclass of the DisplayComponent class,
 * and coordinates, updates, and sends information about each component.
 * @author Matt Imel
 *
 */
public class GameView extends GameViewObserver {

	private GUIFocus focus;

	private GUISidebar sidebar;

	private GUIFooter footer;

	private DisplayConfiguration currentConfig;

	private AudioPlayer player;

	GameView(int winWidth, int winHeight, AudioPlayer ap, GameManager gm) {
		this.setLayout(new BorderLayout());

		this.setBounds(0, 0, winWidth, winHeight);

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
				sidebar.switchToNoSelect();
				footer.switchToDialogue();
				break;
			case TILE_SELECT:
				sidebar.switchToNoSelect();
				footer.switchToSrcDesc();
				break;
			case SKILL_SELECT:
				sidebar.switchToSkillSelect();
				footer.switchToSrcDesc();
				break;
			case INVENTORY_SELECT:
				sidebar.switchToItemSelect();
				footer.switchToSrcDesc();
				break;
			case DEFAULT:
				sidebar.switchToNoSelect();
				footer.switchToMessages();
				break;
		}
		currentConfig = newConfig;
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

		EventQueue eq = new EventQueue();

		//PLAYGROUND TEMPORARY
		CompositeGrid model = new CompositeGrid();
		SourceDescriptionPair sdp = new SourceDescriptionPair("", "");

		focus = new GUIFocus(0, 0, getWidth(), getHeight(), model);
		sidebar = new GUISidebar(0, 0, 500, 800, p1);
		footer = new GUIFooter(500, 0, 600, 200, new FooterMessageFeed(mm), new FooterShortDescriptor(sdp), new FooterDialogue());

		model.addCombatant(p1, 1, 1);
		model.bindTo(0);

		model.addItem(0, 4, 4);
		model.addItem(1, 4, 8);
		for(int x = 0; x < 10; x++) {
			model.addCombatant(EnemyGenerator.getEnemyByName("Khweiri Dervish"), (3 + 7 * x) % 10, 3 + x);
		}

		model.addCombatant(EnemyGenerator.getEnemyByName("Khweiri Dervish"), 19, 0);

		model.addCombatant(EnemyGenerator.getEnemyByName("Bilge Rat"), 12, 4);
		model.addCombatant(EnemyGenerator.getEnemyByName("Fireball"), 13, 4);

		player.playSong("AttemptNo1.mp3");

		System.out.println(model.getGridRepresentation());


		repaint();
		//END PLAYGROUND

		DisplayKeyBindings.initKeyBinds(this, model, p1, mm, sdp, eq);
	}

	@Override
	protected void paintComponent(Graphics g) {
		long timeStart = System.currentTimeMillis();

		focus.paint(g);
		sidebar.paint(g);
		footer.paint(g);

		long timeEnd = System.currentTimeMillis();
		System.out.println("Paint time: " + (timeEnd - timeStart) + "ms.");
	}

	void pause() {
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