package wooteco.subway.admin.dto;

public class PathRequestWithId {
    private Long sourceId;
    private Long targetId;

    public PathRequestWithId(Long sourceId, Long targetId) {
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
