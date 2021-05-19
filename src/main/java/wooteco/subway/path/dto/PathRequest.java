package wooteco.subway.path.dto;

import javax.validation.constraints.NotNull;

public class PathRequest {
    @NotNull
    private Long source;
    @NotNull
    private Long target;

    public PathRequest() {
    }

    public PathRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public Long getSourceId() {
        return source;
    }

    public Long getTargetId() {
        return target;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public void setTarget(Long target) {
        this.target = target;
    }
}
