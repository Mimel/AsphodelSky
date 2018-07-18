package comm;

public class SourceDescriptionPair {
    private String sourceName;

    private String descriptionOfSource;

    public SourceDescriptionPair(String source, String desc) {
        this.sourceName = source;
        this.descriptionOfSource = desc;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getDescription() {
        return descriptionOfSource;
    }

    public void setSourceDescPair(String src, String desc) {
        this.sourceName = src;
        this.descriptionOfSource = desc;
    }
}
