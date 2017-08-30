package grid;

import entity.Combatant;
import item.Catalog;

import java.util.Map;

/**
 * The focus of the grid.
 */
class GridFocus {

    private Point focalPoint;

    private Grid<Combatant, Map.Entry<Point, Combatant>> combatantView;

    private Grid<Catalog, Map.Entry<Point, Catalog>> itemView;

    private Grid<Tile, Tile[]> tileView;

    GridFocus(int x, int y, Grid<Combatant, Map.Entry<Point, Combatant>> combatantView, Grid<Catalog, Map.Entry<Point, Catalog>> itemView, Grid<Tile, Tile[]> tileView) {
        this.focalPoint = new Point(x, y);
        this.combatantView = combatantView;
        this.itemView = itemView;
        this.tileView = tileView;
    }

    Point getFocus() {
        return focalPoint;
    }

    void setFocus(int newX, int newY) {
        focalPoint = new Point(newX, newY);
    }

    void setFocus(Point p) {
        focalPoint = p;
    }

    Combatant getFocusedCombatant() {
        return combatantView.getOccupantAt(focalPoint.x(), focalPoint.y());
    }

    Tile getFocusedTile() {
        return tileView.getOccupantAt(focalPoint.x(), focalPoint.y());
    }

    Catalog getFocusedCatalog() {
        return itemView.getOccupantAt(focalPoint.x(), focalPoint.y());
    }
}
