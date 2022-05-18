package wooteco.subway.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.Station;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private int fare;

    private PathResponse() {
    }

    public PathResponse(final List<StationResponse> stations, final int distance, final int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(List<Station> stations, int distance, int fare) {
        final List<StationResponse> stationResponses = stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());

        return new PathResponse(stationResponses, distance, fare);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
