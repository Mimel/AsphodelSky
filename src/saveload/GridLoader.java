package saveload;

import entity.*;
import event.FlagType;
import event.Opcode;
import event.SimpleEvent;
import event.flag.Flag;
import event.flag.FlagRedirectLocation;
import grid.CompositeGrid;
import item.Catalog;
import item.ItemLoader;
import skill.SkillLibrary;
import skill.SkillSet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class GridLoader {
    private String filename;

    private ItemLoader itemMappings;

    private SkillLibrary skillMappings;

    public GridLoader(String filename, ItemLoader itemMappings, SkillLibrary skillMappings) {
        this.filename = filename;
        this.itemMappings = itemMappings;
        this.skillMappings = skillMappings;
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

                model.addCatalog(new Catalog(catalog, itemMappings), x, y);
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
                    Combatant c = createCombatant(actorRep);
                    c.setId(id);
                    model.addCombatant(c, x, y);
                    actorRep.clear();
                } else {
                    actorRep.add(actorDetails);
                }
            }

            model.bindTo(0);

        } catch (IOException e) {
            e.printStackTrace();
            model = new CompositeGrid();
        }

        return model;
    }

    private Combatant createCombatant(List<String> combatantRepresentation) {
        Combatant newCombatant = new NullCombatant();
        Flag currentFlag = null;

        for(String currLine : combatantRepresentation) {
            char firstCharacter = currLine.charAt(0);
            if (firstCharacter == '$') {
                switch (currLine.charAt(1)) {
                    case 'M':
                        newCombatant = new MindlessAI();
                        break;
                    case 'A':
                        newCombatant = new AnimalisticAI();
                        break;
                    case 'U':
                        newCombatant = new UnderdevelopedAI();
                        break;
                    case 'S':
                        newCombatant = new SapientAI();
                        break;
                    case 'B':
                        newCombatant = new BrilliantAI();
                        break;
                    case 'P':
                        newCombatant = new Player();
                        break;
                }
            } else if (firstCharacter == '!') {
                int slashLoc = currLine.indexOf('/');
                String opTrigger = currLine.substring(1, slashLoc);
                String responseState = currLine.substring(slashLoc + 1);
                currentFlag = Flag.determineFlag(Opcode.valueOf(opTrigger), FlagType.valueOf(responseState));
            } else if (firstCharacter == '-') {
                if (currentFlag != null) {
                    newCombatant.addToFlagList(currentFlag);
                }
            } else if (firstCharacter == ' ' || firstCharacter == '\t') {
                if (currentFlag != null) {
                    SimpleEvent trigger = SimpleEvent.interpretEvent(currLine.substring(1, currLine.indexOf('@')).trim());
                    trigger = new SimpleEvent(trigger, newCombatant);
                    FlagRedirectLocation loc = FlagRedirectLocation.valueOf(currLine.substring(currLine.indexOf('@') + 1));
                    currentFlag.addEventToFlag(trigger, loc);
                }
            } else if (firstCharacter == '#') {
                int equalSignLoc = currLine.indexOf('=');
                String skillCode = currLine.substring(equalSignLoc + 1);

                newCombatant.addToSkillSet(new SkillSet(skillCode, skillMappings));
            } else if (firstCharacter == '+') {
                int equalSignLoc = currLine.indexOf('=');
                String itemCode = currLine.substring(equalSignLoc + 1);
                newCombatant.addToInventory(new Catalog(itemCode, itemMappings));
            } else {
                int equalSignLoc = currLine.indexOf('=');
                String fieldToSet = currLine.substring(0, equalSignLoc);
                String valueOfField = currLine.substring(equalSignLoc + 1);
                assignToField(newCombatant, fieldToSet, valueOfField);
            }
        }

        return newCombatant;
    }

    private void assignToField(Combatant newCombatant, String setterName, String valueOfField) {
        assert (newCombatant != null);

        Method setter;
        try {
            if(isStringNumeric(valueOfField)) {
                setter = newCombatant.getClass().getMethod(setterName, int.class);
                setter.invoke(newCombatant, Integer.parseInt(valueOfField));
            } else {
                setter = newCombatant.getClass().getMethod(setterName, String.class);
                setter.invoke(newCombatant, valueOfField);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private boolean isStringNumeric(String input) {
        for(char character : input.toCharArray()) {
            if(!Character.isDigit(character)) {
                return false;
            }
        }

        return true;
    }
}
