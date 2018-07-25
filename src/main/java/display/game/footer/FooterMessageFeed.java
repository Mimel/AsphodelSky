package display.game.footer;

import comm.MessageManager;
import display.game.DrawingArea;

import java.awt.*;

public class FooterMessageFeed implements FooterContent {
     private final MessageManager feed;

     public FooterMessageFeed(MessageManager mm) {
         this.feed = mm;
     }

    public void paintFooter(Graphics g, DrawingArea bounds) {
         int messageSpace = 25;
         for(String feedItem : feed.getFeedContents()) {
             if(feedItem != null) {
                 g.drawString(feedItem, bounds.getXOffset(), bounds.getYOffset() + (messageSpace += 25));
             }
         }
    }
}
