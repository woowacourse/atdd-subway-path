package wooteco.subway.service.dto.request;

public class PathRequest {

    private final long sourceStationId;
    private final long targetStationId;

    public PathRequest(long sourceStationId, long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public long getSourceStationId() {
        return sourceStationId;
    }

    public long getTargetStationId() {
        return targetStationId;
    }
}
