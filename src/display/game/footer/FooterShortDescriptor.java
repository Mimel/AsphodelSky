package display.game.footer;

import comm.SourceDescriptionPair;
import display.game.DrawingArea;

import java.awt.*;

public class FooterShortDescriptor implements FooterContent {
    private final SourceDescriptionPair sdpair;

    public FooterShortDescriptor(SourceDescriptionPair sdp) {
        this.sdpair = sdp;
    }

    @Override
    public void paintFooter(Graphics g, DrawingArea bounds) {
        g.drawString(sdpair.getSourceName(), bounds.getXOffset() + 100, bounds.getYOffset() + 50);
        g.drawString(sdpair.getDescription(), bounds.getXOffset() + 100, bounds.getYOffset() + 100);
    }
}
