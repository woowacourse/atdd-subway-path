package wooteco.subway.admin.dto;

public class PathRequestWithId {
    private Long sourceId;
    private Long targetId;
    private PathType pathType;

    public PathRequestWithId(Long sourceId, Long targetId, PathType pathType) {
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.pathType = pathType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public PathType getPathType() {
        return pathType;
    }
}
