package event;

/**
 * An event, that when executed, performs multiple events.
 */
public class MacroEvent {
    private int time;

    private int priority;

    private MacroOperation op;

    private int actorId;

    private int affectedId;

    private int xTile;

    private int yTile;

    public MacroEvent(int time, int priority, MacroOperation mo) {
        this.time = time;
        this.priority = priority;
        this.op = mo;
        this.actorId = -1;
        this.affectedId = -1;
        this.xTile = -1;
        this.yTile = -1;
    }

    public MacroEvent(int time, int priority, MacroOperation mo, int actorId, int affectedId, int x, int y) {
        this.time = time;
        this.priority = priority;
        this.op = mo;
        this.actorId = actorId;
        this.affectedId = affectedId;
        this.xTile = x;
        this.yTile = y;
    }

    public MacroOperation getOp() {
        return op;
    }

    public void setOp(MacroOperation op) {
        this.op = op;
    }

    public int getActorId() {
        return actorId;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public int getAffectedId() {
        return affectedId;
    }

    public void setAffectedId(int affectedId) {
        this.affectedId = affectedId;
    }

    public int getxTile() {
        return xTile;
    }

    public void setxTile(int xTile) {
        this.xTile = xTile;
    }

    public int getyTile() {
        return yTile;
    }

    public void setyTile(int yTile) {
        this.yTile = yTile;
    }

    public boolean isValid() {
        return actorId != -1 && affectedId != -1 && xTile != -1 && yTile != -1;
    }

    public Event[] performMacroEvent() {
        System.out.println("Success!");
        return null;
    }
}
