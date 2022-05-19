package wooteco.subway.dto.info;

public class SectionDeleteRequest {
    private final long lineId;
    private final long stationId;

    public SectionDeleteRequest(long lineId, long stationId) {
        this.lineId = lineId;
        this.stationId = stationId;
    }

    public long getLineId() {
        return lineId;
    }

    public long getStationId() {
        return stationId;
    }
}
