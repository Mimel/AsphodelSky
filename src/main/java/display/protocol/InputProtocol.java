package display.protocol;

import display.game.focus.GUIFocus;
import event.EventQueue;
import event.compound_event.CompoundEvent;

public interface InputProtocol {
    void move(EventQueue eq, GUIFocus view, int x, int y);

    CompoundEvent confirm(EventQueue eq, GUIFocus view);

    void goBack(EventQueue eq, GUIFocus view);
}
