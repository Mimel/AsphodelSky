package display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import entity.Combatant;
import item.Item;

/**
 * A component of the Asphodel Sky GUI containing player information.
 * @author Matt Imel
 */
public class GUISidebar extends GUIComponent implements SidebarComponent {

	/**
	 * The combatant that is being focused on. If the component is
	 * in mode "CombatantDisplay", this combatant's stats is shown
	 * on the sidebar.
	 * 
	 * TODO: reciprocation is dangerous, switch out of Combatant.
	 */
	private Combatant combatantFocus;

	public GUISidebar(int x, int y, int w, int h) {
		super(x, y, w, h);
		
		modes = new String[]{"free", "inventory"};
		selectedMode = modes[0];
	}
	
	@Override
	public void updateCombatantInfo(Combatant c) {
		this.combatantFocus = c;
	}
	
	/**
	 * Draws the sidebar graphics.
	 */
	@Override
	protected void paintComponent(Graphics g) { 
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(new Color(0, 200, 0));
		g2.fillRect(0, 0, width, height);
		
		g2.setColor(new Color(0, 0, 0));
		g2.setFont(new Font("Verdana", Font.PLAIN, 24));
		if(combatantFocus != null) {
			
			g2.drawString(combatantFocus.getName(), 35, 25);
			g2.drawString(combatantFocus.getTitle(), 35, 50);
			g2.drawString("Health: " + combatantFocus.getHealth() + "/" + combatantFocus.getMaxHealth(), 35, 75);
			g2.drawString("Momentum: " + combatantFocus.getMomentum(), 35, 100);
			g2.drawString("Science: " + combatantFocus.getCurrentScience() + "/" + combatantFocus.getMaximumScience(), 35, 125);
			
			//Inventory takes 26 slots, one for each letter of the alphabet.
			//Also, TODO get rid of all magic.
			
			List<Item> items = combatantFocus.getInventory().getItems();
			List<Integer> amts = combatantFocus.getInventory().getAmounts();
			
			for(int slot = 0; slot < 26; slot++) {
				
				//Background for item.
				g2.setColor(new Color(slot * 8, 0, 0));
				g2.fillRect(35 + (slot/3)*48, 250 + (slot%3)*48, 48, 48);
				g2.setColor(Color.BLACK);
				g2.drawRect(35 + (slot/3)*48, 250 + (slot%3)*48, 48, 48);
				
				//Item.
				if(!items.isEmpty()) {
					Item currentItem = items.remove(0);
					g2.drawImage(ImageAssets.getItemImage(currentItem.getName()), 35 + (slot/3)*48, 250 + (slot%3)*48, null);
					
					g2.setColor(Color.WHITE);
					g2.drawString(amts.remove(0).toString(), 35 + (slot/3)*48 + 5, 250 + (slot%3)*48 + 24);
				}
				
				//Draw crosshair over focused slot, if applicable.
				if(selectedMode.equals(modes[1])) {
					if(slot == combatantFocus.getInventory().getFocusIndex()) {
						g2.drawImage(ImageAssets.getMiscImage('+'), 35 + (slot/3)*48, 250 + (slot%3)*48, null);
					}
				}
			}
		}
	}
}
