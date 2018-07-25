package grid;

/**
 * A two-dimensional array of Tiles.
 */
public class TileGrid implements Grid<Tile> {

    private final Tile[][] tiles;

    TileGrid(int xMax, int yMax) {
        tiles = new Tile[xMax][yMax];
        clearGrid();
    }

    @Override
    public void placeOccupant(Tile occupant, Point location) {
        tiles[location.x()][location.y()] = occupant;
    }

    @Override
    public boolean canOccupy(Point location) {
        return (location.x() >= 0 && location.y() >= 0 && location.x() < tiles.length && location.y() < tiles[location.x()].length && getOccupantAt(location).canOccupy());
    }

    @Override
    public Tile getOccupantAt(Point location) {
        return tiles[location.x()][location.y()];
    }

    private void clearGrid() {
        for(int x = 0; x < tiles.length; x++) {
            for(int y = 0; y < tiles[x].length; y++) {
                tiles[x][y] = new Tile('.');
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder tileGrid = new StringBuilder();
        for(Tile[] row : tiles) {
            for(Tile cell: row) {
                tileGrid.append(cell.getTerrain());
            }
            tileGrid.append('\n');
        }

        return tileGrid.toString();
    }
}
