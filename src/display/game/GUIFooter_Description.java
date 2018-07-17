package display.game;

import java.awt.*;

/**
 * Draws the title and description of a combatant, item, or tile.
 */
public class GUIFooter_Description {
    void drawDescription(Graphics g, String title, String desc) {
        g.setColor(Color.black);
        if(title != null) {
            g.drawString(title, 100, 100);
        }

        if(desc != null) {
            g.drawString(desc, 100, 130);
        }
    }
}
