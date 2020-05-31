package wooteco.subway.admin.dto;

import javax.validation.constraints.NotNull;

import wooteco.subway.admin.domain.PathType;

public class PathRequest {
    @NotNull(message = "출발역을 지정해주세요!")
    private Long source;
    @NotNull(message = "도착역을 지정해주세요!")
    private Long target;
    @NotNull(message = "최단 거리 또는 최단 시간을 클릭해주세요!")
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
