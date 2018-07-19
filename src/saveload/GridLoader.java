package saveload;

import entity.Combatant;
import entity.EnemyGenerator;
import grid.CompositeGrid;
import item.Catalog;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GridLoader {
    private String filename;

    public GridLoader(String filename) {
        this.filename = filename;
    }

    public CompositeGrid loadGrid() {
        CompositeGrid model;
        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String name = br.readLine();
            String bounds = br.readLine();
            int width = Integer.parseInt(bounds.substring(0, bounds.indexOf(',')));
            int height = Integer.parseInt(bounds.substring(bounds.indexOf(',') + 1));
            model = new CompositeGrid(name, width, height);

            String tilesInRow;
            for(int row = 0; row < height; row++) {
                tilesInRow = br.readLine();
                for(int column = 0; column < width; column++) {
                    model.setTileAt(row, column, tilesInRow.charAt(column));
                }
            }

            String throwaway;
            while((throwaway = br.readLine()) != null && !throwaway.equals("&CATALOGS"));

            String catalogDetails;
            while((catalogDetails = br.readLine()) != null && !catalogDetails.equals("&ACTORS")) {
                int x = Integer.parseInt(catalogDetails.substring(catalogDetails.indexOf('{') + 1, catalogDetails.indexOf(',')));
                int y = Integer.parseInt(catalogDetails.substring(catalogDetails.indexOf(',') + 1, catalogDetails.indexOf('}')));
                String catalog = catalogDetails.substring(catalogDetails.indexOf('>') + 1);

                model.addCatalog(new Catalog(catalog), x, y);
            }

            String actorDetails;
            int id = -1;
            int x = -1;
            int y = -1;
            List<String> actorRep = new ArrayList<>();
            while((actorDetails = br.readLine()) != null) {
                if(actorDetails.charAt(0) == '{') {
                    id = Integer.parseInt(actorDetails.substring(actorDetails.indexOf('{') + 1, actorDetails.indexOf('}')));
                    actorDetails = actorDetails.substring(actorDetails.indexOf('>'));
                    x = Integer.parseInt(actorDetails.substring(actorDetails.indexOf('{') + 1, actorDetails.indexOf(',')));
                    y = Integer.parseInt(actorDetails.substring(actorDetails.indexOf(',') + 1, actorDetails.indexOf('}')));
                } else if(actorDetails.charAt(0) == '-') {
                    actorRep.add(actorDetails);
                    Combatant c = EnemyGenerator.createCombatant(actorRep);
                    c.setId(id);
                    model.addCombatant(c, x, y);
                    actorRep.clear();
                } else {
                    actorRep.add(actorDetails);
                }
            }

            model.bindTo(0);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            model = new CompositeGrid();
        } catch (IOException e) {
            e.printStackTrace();
            model = new CompositeGrid();
        }

        return model;
    }
}
