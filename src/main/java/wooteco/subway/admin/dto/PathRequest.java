package wooteco.subway.admin.dto;

import wooteco.subway.admin.domain.PathType;

public class PathRequest {
    private String source;
    private String target;
    private PathType type;

    public PathRequest() {
    }

    public PathRequest(String source, String target, PathType type) {
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

    public PathType getType() {
        return type;
    }
}
