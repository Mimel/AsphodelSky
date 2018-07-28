package display;

import event.SimpleEvent;

import java.util.List;

@FunctionalInterface
public interface Procedure {
    List<SimpleEvent> execute();
}
