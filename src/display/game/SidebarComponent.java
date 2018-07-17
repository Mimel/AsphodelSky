package display.game;

import entity.Combatant;

/**
 * The component of the window that typically shows combatant info.
 */
public interface SidebarComponent {
	/**
	 * Updates the combatant information based on the given combatant.
	 * @param c The combatant to show.
	 */
	void updateCombatantInfo(Combatant c);
}
