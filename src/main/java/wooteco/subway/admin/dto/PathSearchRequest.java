package wooteco.subway.admin.dto;

import wooteco.subway.admin.domain.PathType;

public class PathSearchRequest {
    private String source;
    private String target;
    private PathType type;

    public PathSearchRequest(String source, String target, String type) {
        this.source = source;
        this.target = target;
        this.type = PathType.valueOf(type.toUpperCase());
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
