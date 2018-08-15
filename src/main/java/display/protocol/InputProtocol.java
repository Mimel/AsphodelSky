package display.protocol;

import event.EventQueue;

public interface InputProtocol {
    public void move(EventQueue eq, int x, int y);

    public void confirm(EventQueue eq, ProtocolHistory actions);

    public void goBack(EventQueue eq, ProtocolHistory actions);

    public void reset(EventQueue eq, ProtocolHistory actions);
}
