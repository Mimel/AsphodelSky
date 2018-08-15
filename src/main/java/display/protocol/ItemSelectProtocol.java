package display.protocol;

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
    public void confirm(EventQueue eq, ProtocolHistory actions) {
        queuedEvent.setItem(targetCatalog.getFocusedItem());
    }

    @Override
    public void goBack(EventQueue eq, ProtocolHistory actions) {

    }

    @Override
    public void reset(EventQueue eq, ProtocolHistory actions) {

    }
}
