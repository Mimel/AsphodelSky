package comm;

public class SourceDescriptionTriplet {
    private String sourceName;

    private String descriptionOfSource;

    private String effectsOfSource;

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
