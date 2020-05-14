package wooteco.subway.admin.dto;

public class PathRequest {
    private String sourceName;
    private String targetName;
    private String type;

    public PathRequest() {
    }

    public PathRequest(String sourceName, String targetName, String type) {
        this.sourceName = sourceName;
        this.targetName = targetName;
        this.type = type;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getTargetName() {
        return targetName;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "PathRequest{" +
                "sourceName='" + sourceName + '\'' +
                ", targetName='" + targetName + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
