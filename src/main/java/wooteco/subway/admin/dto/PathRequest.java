package wooteco.subway.admin.dto;

import wooteco.subway.admin.domain.PathType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PathRequest {
    @NotBlank
    private String source;

    @NotBlank
    private String target;

    @NotNull
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

    public void setSource(String source) {
        this.source = source;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setType(PathType type) {
        this.type = type;
    }
}
