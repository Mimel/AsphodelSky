package display;

import event.InstructionData;
import event.Opcode;
import event.SimpleEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class GraphicInstructionSet {
    private Map<Opcode, BiConsumer<InstructionData, Stage>> graphicInstructionSet;

    GraphicInstructionSet() {
        graphicInstructionSet = new HashMap<>();
        loadGraphicInstructionSet();
    }

    private void loadGraphicInstructionSet() {
        graphicInstructionSet.put(Opcode.COMBATANT_MOVE, (data, stage) -> {
            stage.getDrawnCombatant(data.getTarget()).move(data.getTile());
            stage.moveCameraToCombatant(data.getTarget());
        });
    }

    public void alterViewByEvent(SimpleEvent actionToPerform, Stage s) {
        graphicInstructionSet.get(actionToPerform.getSimpleOperation()).accept(actionToPerform.getData(), s);
    }
}
