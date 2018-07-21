package display.game.footer;

import comm.SourceDescriptionTriplet;
import display.game.DrawingArea;

import java.awt.*;

public class FooterShortDescriptor implements FooterContent {
    private final SourceDescriptionTriplet sdpair;

    public FooterShortDescriptor(SourceDescriptionTriplet sdp) {
        this.sdpair = sdp;
    }

    @Override
    public void paintFooter(Graphics g, DrawingArea bounds) {
        g.drawString(sdpair.getSourceName(), bounds.getXOffset() + 100, bounds.getYOffset() + 50);
        g.drawString(sdpair.getVisualDescription(), bounds.getXOffset() + 100, bounds.getYOffset() + 100);
        g.drawString(sdpair.getEffectDescription(), bounds.getXOffset() + 100, bounds.getYOffset() + 150);
    }
}
