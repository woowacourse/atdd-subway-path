package wooteco.subway.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.Station;

public class PathResponse {

    private int distance;
    private List<StationResponse> stationResponses;

    private PathResponse() {
    }

    public PathResponse(final List<Station> stations, final int distance) {
        this.stationResponses = stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }
}
