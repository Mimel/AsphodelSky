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
    public void move(EventQueue eq, GUIFocus view, int x, int y) {
        targetCatalog.setFocus(x);
    }

    @Override
    public CompoundEvent confirm(EventQueue eq, GUIFocus view) {
        queuedEvent.setItem(targetCatalog.getFocusedItem());
        view.hideItemSelector();

        return queuedEvent;
    }

    @Override
    public void goBack(EventQueue eq, GUIFocus view) {
        view.hideItemSelector();
    }

}
