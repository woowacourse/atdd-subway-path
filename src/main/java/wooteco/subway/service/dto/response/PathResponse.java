package wooteco.subway.service.dto.response;

import java.util.List;

public class PathResponse {

    private final List<Long> stationIds;
    private final long distance;
    private final long fare;

    public PathResponse(List<Long> stationIds, long distance, long fare) {
        this.stationIds = stationIds;
        this.distance = distance;
        this.fare = fare;
    }

    public List<Long> getStationIds() {
        return stationIds;
    }

    public long getDistance() {
        return distance;
    }

    public long getFare() {
        return fare;
    }
}
