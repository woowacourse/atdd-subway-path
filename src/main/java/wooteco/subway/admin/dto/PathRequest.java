package wooteco.subway.admin.dto;

public class PathRequest {
    private String source;
    private String target;
    private String type;

    public PathRequest(String source, String target, String type) {
        this.source = source;
        this.target = target;
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public String getType() {
        return type;
    }
}
