package grid;

/**
 * A two-dimensional array of Tiles.
 */
public class TileGrid implements Grid<Tile> {

    private Tile[][] tiles;

    TileGrid(int xMax, int yMax) {
        tiles = new Tile[xMax][yMax];
        clearGrid();
    }

    @Override
    public void placeOccupant(Tile occupant, int x, int y) {
        tiles[x][y] = occupant;
    }

    @Override
    public boolean isOccupied(int x, int y) {
        return true;
    }

    @Override
    public Tile getOccupantAt(int x, int y) {
        return tiles[x][y];
    }

    @Override
    public Tile removeOccupantAt(int x, int y) {
        Tile previousTile = tiles[x][y];
        tiles[x][y] = new Tile('.');
        return previousTile;
    }

    @Override
    public void clearGrid() {
        for(int x = 0; x < tiles.length; x++) {
            for(int y = 0; y < tiles[x].length; y++) {
                tiles[x][y] = new Tile('.');
            }
        }
    }
}
