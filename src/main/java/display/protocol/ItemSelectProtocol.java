package display.protocol;

import display.game.focus.GUIFocus;
import event.EventQueue;
import event.compound_event.CompoundEvent;
import item.Catalog;

public class ItemSelectProtocol implements InputProtocol {
    private Catalog targetCatalog;
    private CompoundEvent queuedEvent;

    public ItemSelectProtocol(Catalog c, CompoundEvent ce) {
        this.targetCatalog = c;
        this.queuedEvent = ce;
    }

    @Override
    public void move(EventQueue eq, int x, int y) {
        targetCatalog.setFocus(x);
    }

    @Override
    public void confirm(EventQueue eq, GUIFocus view) {
        queuedEvent.setItem(targetCatalog.getFocusedItem());
        view.hideItemSelector();
    }

    @Override
    public void goBack(EventQueue eq, GUIFocus view) {
        view.hideItemSelector();
    }

    @Override
    public CompoundEvent getQueuedEvent() {
        return queuedEvent;
    }
}
