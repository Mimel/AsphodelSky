package grid;

import java.util.Iterator;

/**
 * Created by Owner on 8/22/2017.
 */
public class FocusGrid implements Grid<Boolean, Object> {

    @Override
    public void placeOccupant(Boolean occupant, int x, int y) {

    }

    @Override
    public boolean canOccupy(int x, int y) {
        return false;
    }

    @Override
    public Boolean getOccupantAt(int x, int y) {
        return null;
    }

    @Override
    public Boolean removeOccupantAt(int x, int y) {
        return null;
    }

    @Override
    public void clearGrid() {

    }

    @Override
    public Grid<Boolean, Object> subGrid(int x, int y, int width, int height) {
        return null;
    }

    @Override
    public Iterator<Object> iterator() {
        return null;
    }
}
