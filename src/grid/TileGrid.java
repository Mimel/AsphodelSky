package grid;

import java.util.Arrays;
import java.util.Iterator;

/**
 * A two-dimensional array of Tiles.
 */
public class TileGrid implements Grid<Tile, Tile[]> {

    private final Tile[][] tiles;

    TileGrid(int xMax, int yMax) {
        tiles = new Tile[xMax][yMax];
        clearGrid();
    }

    @Override
    public void placeOccupant(Tile occupant, int x, int y) {
        tiles[x][y] = occupant;
    }

    @Override
    public boolean canOccupy(int x, int y) {
        return (x >= 0 && y >= 0 && x < tiles.length && y < tiles[x].length);
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

    @Override
    public Grid<Tile, Tile[]> subGrid(int x, int y, int width, int height) {
        Grid<Tile, Tile[]> subGrid = new TileGrid(width, height);
        int origX = x;
        int origY = y;
        int subX = 0;
        int subY = 0;
        for(x = origX; x < origX + width; x++) {
            for(y = origY; y < origY + height; y++) {
                subGrid.placeOccupant(tiles[x][y], subX, subY++);
            }
            subX++;
            subY = 0;
        }
        return subGrid;
    }

    @Override
    public Iterator<Tile[]> iterator() {
        return Arrays.asList(tiles).iterator();
    }
}
