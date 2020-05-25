package wooteco.subway.admin.dto;

public class PathRequest {
    private String key;
    private String source;
    private String target;

    public PathRequest(final String key, final String source, final String target) {
        this.key = key;
        this.source = source;
        this.target = target;
    }

    public String getKey() {
        return key;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
