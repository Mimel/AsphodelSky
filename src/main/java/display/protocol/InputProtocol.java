package display.protocol;

import display.game.focus.GUIFocus;
import event.EventQueue;
import event.compound_event.CompoundEvent;

public interface InputProtocol {
    public void move(EventQueue eq, int x, int y);

    public void confirm(EventQueue eq, GUIFocus view);

    public void goBack(EventQueue eq, GUIFocus view);

    public CompoundEvent getQueuedEvent();
}
