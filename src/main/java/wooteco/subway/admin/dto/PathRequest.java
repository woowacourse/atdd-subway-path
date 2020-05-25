package wooteco.subway.admin.dto;

import wooteco.subway.admin.domain.PathType;

public class PathRequest {
    private String source;
    private String target;
    private PathType type;

    public PathRequest() {
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public PathType getType() {
        return type;
    }

    public void setType(PathType type) {
        this.type = type;
    }
}
