package wooteco.subway.admin.dto;

import wooteco.subway.admin.service.PathType;

public class PathRequest {
    private Long source;
    private Long target;
    private PathType type;

    public PathRequest() {
    }

    public PathRequest(final Long source, final Long target, final PathType type) {
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

    public PathType getType() {
        return type;
    }

    public void setSource(final Long source) {
        this.source = source;
    }

    public void setTarget(final Long target) {
        this.target = target;
    }

    public void setType(PathType type) {
        this.type = type;
    }
}
