package wooteco.subway.admin.dto;

public class PathRequest {
    private Long source;
    private Long target;
    private String type;

    public PathRequest() {
    }

    public PathRequest(final Long source, final Long target, final String type) {
        this.source = source;
        this.target = target;
        this.type = type;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public String getType() {
        return type;
    }

    public void setSource(final Long source) {
        this.source = source;
    }

    public void setTarget(final Long target) {
        this.target = target;
    }

    public void setType(final String type) {
        this.type = type;
    }
}
