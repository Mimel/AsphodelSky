package display.game;

import java.awt.Point;

public class DrawingArea {
    private Point origin;
    private Point limit;

    public DrawingArea(int x, int y, int width, int height) {
        this.origin = new Point(x, y);
        this.limit = new Point(origin.x + width, origin.y + height);
    }

    public int getXOffset() {
        return origin.x;
    }

    public int getYOffset() {
        return origin.y;
    }

    public int getWidth() {
        return limit.x - origin.x;
    }

    public int getHeight() {
        return limit.y - origin.y;
    }
}
