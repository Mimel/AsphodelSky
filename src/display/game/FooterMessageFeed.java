package display.game;

import comm.MessageManager;

import java.awt.*;

public class FooterMessageFeed implements FooterContent {
     private MessageManager feed;

     public FooterMessageFeed(MessageManager mm) {
         this.feed = mm;
     }

    @Override
    public void paintFooter(Graphics g) {
         int yOffset = 0;
         for(String feedItem : feed.getFeedContents()) {
             g.drawString(feedItem, 100, yOffset += 50);
         }
    }
}
