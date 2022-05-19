package wooteco.subway.service.dto;

public class SectionServiceDeleteRequest {

    private final Long lineId;
    private final Long stationId;

    public SectionServiceDeleteRequest(Long lineId, Long stationId) {
        this.lineId = lineId;
        this.stationId = stationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getStationId() {
        return stationId;
    }
}
