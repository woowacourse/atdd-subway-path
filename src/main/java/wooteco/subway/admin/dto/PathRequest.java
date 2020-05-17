package wooteco.subway.admin.dto;

import wooteco.subway.admin.domain.PathType;

import javax.validation.constraints.NotBlank;

public class PathRequest {
    @NotBlank
    private final String source;
    @NotBlank
    private final String target;
    @NotBlank
    private final PathType type;

    public PathRequest(String source, String target, PathType type) {
        this.source = source;
        this.target = target;
        this.type = type;
    }

    public static PathRequest of(String source, String target, PathType type) {
        return new PathRequest(source, target, type);
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public PathType getPathType() {
        return type;
    }
}
