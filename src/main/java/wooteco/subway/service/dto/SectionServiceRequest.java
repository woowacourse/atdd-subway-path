package wooteco.subway.service.dto;

public class SectionServiceRequest {

    private final Long lindId;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;


    public SectionServiceRequest(Long lineId, Long upStationId, Long downStationId, int distance) {
        this.lindId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getLindId() {
        return lindId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
