package wooteco.subway.service.dto.request;

public class SectionServiceRequest {

    private final Long upStationId;
    private final Long downStationId;
    private final Long distance;

    public SectionServiceRequest(Long upStationId, Long downStationId, Long distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }
}
