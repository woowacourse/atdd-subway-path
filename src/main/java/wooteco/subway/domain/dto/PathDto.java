package wooteco.subway.domain.dto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PathDto {

    private final List<Long> stationIds;
    private final int distance;

    public PathDto(List<Long> stationIds, int distance) {
        this.stationIds = stationIds;
        this.distance = distance;
    }

    public List<Long> getStationIds() {
        return Collections.unmodifiableList(stationIds);
    }

    public int getDistance() {
        return distance;
    }
}
