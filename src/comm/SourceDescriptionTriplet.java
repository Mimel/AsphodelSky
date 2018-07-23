package comm;

/**
 * A set of three strings that can be shown as a general description of a given object in the game state.
 * It consists of:
 * A sourceName, which is the name of the object in question.
 * A visualDescription, which is a flowery, colorful description of the object.
 * An effectDescription, which tells all the important, game-relevant details of the object.
 */
public class SourceDescriptionTriplet {

    /**
     * The name of the object in question.
     */
    private String sourceName;

    /**
     * A flowery, colorful description of the object.
     */
    private String descriptionOfSource;

    /**
     * All the important, game-relevant details of the object.
     */
    private String effectsOfSource;

    public SourceDescriptionTriplet() {
        this.sourceName = "";
        this.descriptionOfSource = "";
        this.effectsOfSource = "";
    }

    public SourceDescriptionTriplet(String source, String visualDescription, String effectDescription) {
        this.sourceName = source;
        this.descriptionOfSource = visualDescription;
        this.effectsOfSource = effectDescription;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getVisualDescription() {
        return descriptionOfSource;
    }

    public String getEffectDescription() {
        return effectsOfSource;
    }

    public void setSourceDescPair(String src, String visualDesc, String effectDesc) {
        this.sourceName = src;
        this.descriptionOfSource = visualDesc;
        this.effectsOfSource = effectDesc;
    }
}
