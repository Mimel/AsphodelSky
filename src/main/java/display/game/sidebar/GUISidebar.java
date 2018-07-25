package display.game.sidebar;

import display.game.DrawingArea;
import display.image.ImageAssets;
import entity.Combatant;
import item.Item;
import skill.Skill;

import java.awt.*;
import java.util.List;

/**
 * A component of the Asphodel Sky GUI containing player information.
 * @author Matt Imel
 */
public class GUISidebar {

	public static final int INVENTORY_ROWS = 3;
	private static final int INVENTORY_SPCS = 26;
	private static final int SKILL_SPCS = 10;

	static final Point INVENTORY_OFFSET = new Point(35, 250);
	static final Point SKILLSET_OFFSET = new Point(35, 450);

	private final DrawingArea bounds;
	private final ImageAssets iAssets;

	private final SidebarSelectLayer none;
	private final SidebarSelectLayer itemSelect;
	private final SidebarSelectLayer skillSelect;
	private SidebarSelectLayer currentSelection;

	/**
	 * The combatant that is being focused on. If the component is
	 * in mode "CombatantDisplay", this combatant's stats is shown
	 * on the footer.
	 */
	private final Combatant combatantFocus;

	public GUISidebar(int x, int y, int w, int h, Combatant focus, ImageAssets iAssets) {
		this.bounds = new DrawingArea(x, y, w, h);
		this.combatantFocus = focus;
		this.iAssets = iAssets;

		this.none = new SidebarNoSelect();
		this.itemSelect = new SidebarSelectItem(focus.getInventory(), iAssets);
		this.skillSelect = new SidebarSelectSkill(focus.getSkillSet(), iAssets);


		this.currentSelection = none;
	}

	public void switchToNoSelect() {
		currentSelection = none;
	}

	public void switchToItemSelect() {
		currentSelection = itemSelect;
	}

	public void switchToSkillSelect() {
		currentSelection = skillSelect;
	}
	
	/**
	 * Draws the footer graphics.
	 */
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(new Color(0, 200, 0));
		g2.fillRect(bounds.getXOffset(), bounds.getYOffset(), bounds.getWidth(), bounds.getHeight());
		
		g2.setColor(new Color(0, 0, 0));
		g2.setFont(new Font("Verdana", Font.PLAIN, 24));
		if(combatantFocus != null) {
			
			g2.drawString(combatantFocus.getName(), 35, 25);
			g2.drawString(combatantFocus.getTitle(), 35, 50);
			g2.drawString("Health: " + combatantFocus.getHealth() + "/" + combatantFocus.getMaxHealth(), 35, 75);
			g2.drawString("Momentum: " + combatantFocus.getMomentum(), 35, 100);
			g2.drawString("Science: " + combatantFocus.getCurrentScience() + "/" + combatantFocus.getMaximumScience(), 35, 125);
			
			//Inventory takes 26 slots, one for each letter of the alphabet.
			
			List<Item> items = combatantFocus.getInventory().getItems();
			List<Integer> amts = combatantFocus.getInventory().getAmounts();
			
			for(int slot = 0; slot < INVENTORY_SPCS; slot++) {
				
				//Background for item.
				g2.setColor(new Color(slot * 8, 0, 0));
				g2.fillRect(INVENTORY_OFFSET.x + (slot/INVENTORY_ROWS)*48, INVENTORY_OFFSET.y + (slot%INVENTORY_ROWS)*48, 48, 48);
				g2.setColor(Color.BLACK);
				g2.drawRect(INVENTORY_OFFSET.x + (slot/INVENTORY_ROWS)*48, INVENTORY_OFFSET.y + (slot%INVENTORY_ROWS)*48, 48, 48);

				//Item.
				if(!items.isEmpty()) {
					Item currentItem = items.remove(0);
					g2.drawImage(iAssets.getItemImage(currentItem.getName()), INVENTORY_OFFSET.x + (slot/INVENTORY_ROWS)*48, INVENTORY_OFFSET.y + (slot%INVENTORY_ROWS)*48, null);
					
					g2.setColor(Color.WHITE);
					g2.drawString(amts.remove(0).toString(), INVENTORY_OFFSET.x + (slot/INVENTORY_ROWS)*48 + 5, INVENTORY_OFFSET.y + (slot%INVENTORY_ROWS)*48 + 24);
				}
			}

			List<Skill> skills = combatantFocus.getSkillSet().getSkillsDeepCopy();

			for(int slot = 0; slot < SKILL_SPCS; slot++) {

				//Background for skills
				g2.setColor(new Color(220, 220, 220));
				g2.fillRect(SKILLSET_OFFSET.x + (slot)*48, SKILLSET_OFFSET.y, 48, 48);
				g2.setColor(Color.BLACK);
				g2.drawRect(SKILLSET_OFFSET.x + (slot)*48, SKILLSET_OFFSET.y, 48, 48);

				//Skill images.
				if(!skills.isEmpty()) {
					Skill currentSkill = skills.remove(0);
					g2.drawImage(iAssets.getSkillImage(currentSkill.getName()), SKILLSET_OFFSET.x + (slot)*48, SKILLSET_OFFSET.y, null);
				}
			}

			currentSelection.paintSelection(g, bounds);
		}
	}
}
