package wooteco.subway.admin.dto;

public class PathRequest {
    private String key;
    private Long source;
    private Long target;

    public PathRequest(final String key, final Long source, final Long target) {
        this.key = key;
        this.source = source;
        this.target = target;
    }

    public String getKey() {
        return key;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
