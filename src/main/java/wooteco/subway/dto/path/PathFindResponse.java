package wooteco.subway.dto.path;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.path.Path;
import wooteco.subway.dto.station.StationResponse;

public class PathFindResponse {

    private List<StationResponse> stations;
    private int distance;
    private int fare;

    private PathFindResponse() {
    }

    public PathFindResponse(final List<StationResponse> stations, final int distance, final int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathFindResponse from(final Path path, final int fare) {
        List<StationResponse> stationResponses = path.getStations()
                .stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
        return new PathFindResponse(stationResponses, path.getDistance(), fare);
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
