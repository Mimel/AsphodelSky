package saveload;

import grid.CompositeGrid;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GridSaver {
    CompositeGrid model;

    public GridSaver(CompositeGrid model) {
        this.model = model;
    }

    public void save() {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("saves/1.asf"))) {
            bw.write(model.getGridRepresentation());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
