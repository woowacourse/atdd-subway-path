package wooteco.subway.path.dto;

import javax.validation.constraints.NotNull;

public class PathRequest {
    @NotNull
    private Long sourceId;
    @NotNull
    private Long targetId;

    public PathRequest() {
    }

    public PathRequest(Long sourceId, Long targetId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }
}
