package display;

import java.awt.*;

/**
 * Draws the title and description of a combatant, item, or tile.
 */
public class GUIFooter_Description {
    void drawDescription(Graphics g, String title, String desc) {
        g.setColor(new Color(0, 200, 0));
        g.fillRect(30, 30, 60, 60);
    }
}
