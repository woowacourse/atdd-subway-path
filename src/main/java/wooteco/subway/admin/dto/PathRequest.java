package wooteco.subway.admin.dto;

public class PathRequest {
    private String source;
    private String target;

    public PathRequest(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
