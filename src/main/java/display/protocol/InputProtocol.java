package display.protocol;

import display.game.focus.GUIFocus;
import event.EventQueue;
import event.compound_event.CompoundEvent;

public interface InputProtocol {
    public void move(EventQueue eq, GUIFocus view, int x, int y);

    public CompoundEvent confirm(EventQueue eq, GUIFocus view);

    public void goBack(EventQueue eq, GUIFocus view);
}
