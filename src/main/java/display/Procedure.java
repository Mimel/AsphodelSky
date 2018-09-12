package display;

import event.SimpleEvent;

import java.util.List;

@FunctionalInterface
interface Procedure {
    List<SimpleEvent> execute();
}
