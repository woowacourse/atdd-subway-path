package wooteco.subway.dto;

import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Path;
import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private final List<StationResponse> stations;
    private final int distance;
    private final int fare;

    public PathResponse(final List<StationResponse> stations, final int distance, final int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(final Path path, final int discountedFare) {
        final List<StationResponse> stationResponses = path.getStations().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
        final int distance = path.getDistance();

        return new PathResponse(stationResponses, distance, discountedFare);
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
