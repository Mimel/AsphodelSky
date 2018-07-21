package skill;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A skill library containing all possible skills in the game. Whenever a skill is to be added to the game state,
 * it must be copied from this library.
 */
public class SkillLibrary {

    /**
     * A map that maps the names of skills to the skills themselves.
     */
    private final Map<String, Skill> skillNameToSkill;

    /**
     * Loads the skills into the library via text file.
     * @param fileName The file to read the skill data from.
     */
    public SkillLibrary(String fileName) {
        skillNameToSkill = new HashMap<>();

        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;

            String name = "";
            String vDesc = "";
            String uDesc = "";
            StringBuilder effects = new StringBuilder();

            while((line = br.readLine()) != null) {
                if(name.equals("")) {
                    name = line;
                } else if(vDesc.equals("")) {
                    vDesc = line;
                } else if(uDesc.equals("")) {
                    uDesc = line;
                } else if(line.equals("!END")) {
                    Skill newestSkill = new Skill(name, vDesc, uDesc, effects.toString());
                    skillNameToSkill.put(name, newestSkill);
                    name = "";
                    vDesc = "";
                    uDesc = "";
                    effects = new StringBuilder();
                } else {
                    effects.append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a deep copy of a skill from the library based on the name given.
     * @param name The name of a skill.
     * @return A deep-copied skill from the map, or null if the name is not a key in it.
     */
    Skill getSkillByName(String name) {
        return new Skill(skillNameToSkill.get(name));
    }
}
