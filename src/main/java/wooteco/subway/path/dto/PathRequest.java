package wooteco.subway.path.dto;

public class PathRequest {

    private Long sourceStationId;
    private Long targetStationId;

    public PathRequest(final Long sourceStationId, final Long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    public void setSourceStationId(Long sourceStationId) {
        this.sourceStationId = sourceStationId;
    }

    public void setTargetStationId(Long targetStationId) {
        this.targetStationId = targetStationId;
    }
}
