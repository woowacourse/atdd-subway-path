package wooteco.subway.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.property.fare.Fare;

public class PathResponse {

    private final List<StationResponse> stations;
    private final Integer distance;
    private final Integer fare;

    public PathResponse(List<StationResponse> stations, Integer distance, Integer fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(Path path, Fare fare) {
        return new PathResponse(
            path.getStations()
                .stream()
                .map(StationResponse::from)
                .collect(Collectors.toList()),
            path.getDistance().getValue(),
            fare.getAmount()
        );
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getFare() {
        return fare;
    }
}
