package wooteco.subway.admin.dto;

import javax.validation.constraints.NotNull;

import wooteco.subway.admin.domain.PathType;

@PathForm
public class PathRequest {
    @NotNull
    private Long source;
    @NotNull
    private Long target;
    @NotNull
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

    public void setSource(final Long source) {
        this.source = source;
    }

    public Long getTarget() {
        return target;
    }

    public void setTarget(final Long target) {
        this.target = target;
    }

    public PathType getType() {
        return type;
    }

    public void setType(PathType type) {
        this.type = type;
    }
}
