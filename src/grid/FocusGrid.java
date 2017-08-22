package grid;

/**
 * Created by Owner on 8/22/2017.
 */
public class FocusGrid implements Grid<Boolean> {

    @Override
    public void placeOccupant(Boolean occupant, int x, int y) {

    }

    @Override
    public boolean isOccupied(int x, int y) {
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
}
