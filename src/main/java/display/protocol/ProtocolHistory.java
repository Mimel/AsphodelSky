package display.protocol;

import java.util.Stack;

public class ProtocolHistory {
    private Stack<InputProtocol> protocolStack;

    private Stack<InputProtocol> protocolUsed;

    public ProtocolHistory() {
        protocolStack = new Stack<>();
        protocolUsed = new Stack<>();
    }

    public void push(InputProtocol ip) {
        protocolStack.push(ip);
    }

    public InputProtocol peek() {
        return protocolStack.peek();
    }

    public void clear() {
        protocolStack.clear();
        protocolUsed.clear();
    }

    public boolean isEmpty() {
        return protocolStack.isEmpty();
    }
}
