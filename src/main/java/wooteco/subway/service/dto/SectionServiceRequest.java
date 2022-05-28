package wooteco.subway.service.dto;

public class SectionServiceRequest {

    private final long lindId;
    private final long upStationId;
    private final long downStationId;
    private final int distance;


    public SectionServiceRequest(long lineId, long upStationId, long downStationId, int distance) {
        this.lindId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public long getLindId() {
        return lindId;
    }

    public long getUpStationId() {
        return upStationId;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
