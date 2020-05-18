package wooteco.subway.admin.dto.res;

import java.util.List;

public class GraphResultResponse {
    private final List<Long> stationIds;
    private final int distance;
    private final int duration;

    public GraphResultResponse(List<Long> stationIds, int distance, int duration) {
        this.stationIds = stationIds;
        this.distance = distance;
        this.duration = duration;
    }

    public List<Long> getStationIds() {
        return stationIds;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
