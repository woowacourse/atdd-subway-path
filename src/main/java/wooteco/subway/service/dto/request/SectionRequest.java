package wooteco.subway.service.dto.request;

public class SectionRequest {

    private final long upStationId;
    private final long downStationId;
    private final long distance;

    public SectionRequest(long upStationId, long downStationId, long distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public long getUpStationId() {
        return upStationId;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public long getDistance() {
        return distance;
    }
}
