package wooteco.subway.admin.dto;

public class PathRequest {
    private String sourceName;
    private String targetName;

    public PathRequest() {
    }

    public PathRequest(String sourceName, String targetName) {
        this.sourceName = sourceName;
        this.targetName = targetName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getTargetName() {
        return targetName;
    }
}
