package display.game;

import comm.SourceDescriptionPair;

import java.awt.*;

public class FooterShortDescriptor implements FooterContent {
    private final SourceDescriptionPair sdpair;

    public FooterShortDescriptor(SourceDescriptionPair sdp) {
        this.sdpair = sdp;
    }

    @Override
    public void paintFooter(Graphics g) {
        g.drawString(sdpair.getSourceName(), 100, 50);
        g.drawString(sdpair.getDescription(), 100, 100);
    }
}
