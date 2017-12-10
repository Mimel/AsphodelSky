package display;

import java.awt.*;

/**
 * Draws a set of messages.
 */
public class GUIFooter_Messages {

    void drawMessages(Graphics g, String[] messages) {
        if(messages != null) {
            g.setColor(Color.black);
            for(int i = 0; i < messages.length; i++) {
                if(messages[i] != null) {
                    g.drawString(messages[i], 50, 100 + (30*i));
                }
            }
        }
    }
}
