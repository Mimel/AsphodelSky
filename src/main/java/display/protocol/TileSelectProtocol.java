package display.protocol;

import display.game.focus.GUIFocus;
import event.EventQueue;
import event.compound_event.CompoundEvent;
import grid.Point;

public class TileSelectProtocol implements InputProtocol {
    private int xCurr;
    private int yCurr;

    private int xMax;
    private int yMax;

    private CompoundEvent queuedEvent;

    public TileSelectProtocol(int x, int y, int xMax, int yMax, CompoundEvent ce) {
        this.xCurr = x;
        this.yCurr = y;
        this.xMax = xMax;
        this.yMax = yMax;

        this.queuedEvent = ce;
    }

    @Override
    public void move(EventQueue eq, GUIFocus view, int x, int y) {
        view.hideTileSelector(xCurr, yCurr);

        xCurr += x;
        yCurr += y;

        if(xCurr < 0) {
            xCurr = 0;
        } else if(xCurr >= xMax) {
            xCurr = xMax - 1;
        }

        if(yCurr < 0) {
            yCurr = 0;
        } else if(yCurr >= yMax) {
            yCurr = yMax - 1;
        }

        view.showTileSelector(xCurr, yCurr);
    }

    @Override
    public CompoundEvent confirm(EventQueue eq, GUIFocus view) {
        queuedEvent.setTile(new Point(xCurr, yCurr));
        view.hideTileSelector(xCurr, yCurr);

        return queuedEvent;
    }

    @Override
    public void goBack(EventQueue eq, GUIFocus view) {
        view.hideTileSelector(xCurr, yCurr);
    }
}
