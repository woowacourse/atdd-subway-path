package wooteco.subway.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.Station;

public class PathResponse {

    private List<StationResponse> stationResponses;

    private PathResponse() {
    }

    public PathResponse(final List<Station> stations) {
        this.stationResponses = stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }
}
