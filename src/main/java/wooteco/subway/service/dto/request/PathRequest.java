package wooteco.subway.service.dto.request;

public class PathRequest {

    private final long sourceStationId;
    private final long targetStationId;
    private final long age;

    public PathRequest(long sourceStationId, long targetStationId, long age) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
        this.age = age;
    }

    public long getSourceStationId() {
        return sourceStationId;
    }

    public long getTargetStationId() {
        return targetStationId;
    }

    public long getAge() {
        return age;
    }
}
