package display;

import java.awt.*;

/**
 * Created by Owner on 8/3/2017.
 */
public class GUIFooter_Dialogue {
    void drawDialogue(Graphics g, String dialogue, int pathChoice, String... replies) {
        g.setColor(Color.BLACK);
        g.drawString(dialogue, 100, 100);

        //Show replies if there are choices.
        if(replies.length > 1) {
            int replyPositioningCounter = 0;
            for(String reply : replies) {
                if(replyPositioningCounter == pathChoice) {
                    g.fillRect(80, 130 + (replyPositioningCounter * 25), 20, 20);
                }
                g.drawString(reply, 100, 150 + (replyPositioningCounter++ * 25));
            }
        }
    }
}
