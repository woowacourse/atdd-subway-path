package wooteco.subway.dto;

import wooteco.subway.domain.Path;
import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private final List<StationResponse> stations;
    private final double distance;
    private final int fare;

    public PathResponse(final List<StationResponse> stations, final double distance, final int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse from(final Path path) {
        final List<StationResponse> stationResponses = path.getStations().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
        final double distance = path.getDistance();
        final int price = path.getFare().getPrice();

        return new PathResponse(stationResponses, distance, price);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
