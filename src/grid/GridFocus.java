package grid;

/**
 * Created by Owner on 7/26/2017.
 */
public class GridFocus {
    private int xPosition;

    private int yPosition;

    private FocusRange rangeOfSelection;

    private boolean boundToCombatant;

    private int combatantIdFocus;

    GridFocus(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
        this.boundToCombatant = false;
        this.combatantIdFocus = 0;
    }

    int getxPosition() {
        return xPosition;
    }

    void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    int getyPosition() {
        return yPosition;
    }

    void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public FocusRange getRangeOfSelection() {
        return rangeOfSelection;
    }

    public void setRangeOfSelection(FocusRange rangeOfSelection) {
        this.rangeOfSelection = rangeOfSelection;
    }

    boolean isBoundToCombatant() {
        return boundToCombatant;
    }

    void bindToCombatant(int combatantId) {
        combatantIdFocus = combatantId;
        boundToCombatant = true;
    }

    void unbind() {
        boundToCombatant = false;
    }

    int getFocusedCombatantId() {
        return combatantIdFocus;
    }
}
