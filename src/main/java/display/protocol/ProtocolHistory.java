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

    public void pushPreviousProtocol() {
        protocolStack.push(protocolUsed.pop());
    }

    public InputProtocol peek() {
        return protocolStack.peek();
    }

    public InputProtocol pop() {
        InputProtocol ip = protocolStack.pop();
        protocolUsed.push(ip);

        return ip;
    }

    public void clear() {
        protocolStack.clear();
        protocolUsed.clear();
    }

    public boolean isEmpty() {
        return protocolStack.isEmpty();
    }
}
