package wooteco.subway.dto;


import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.path.Path;

public class PathResponse {

    private final List<StationResponse> stations;
    private final int distance;
    private final int fare;

    private PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(Path path, Fare fare) {
        List<StationResponse> stationResponses = generateStationResponses(path);
        int distanceValue = path.getDistance().getValue();
        int fareValue = fare.getValue();

        return new PathResponse(stationResponses, distanceValue, fareValue);
    }

    private static List<StationResponse> generateStationResponses(Path path) {
        return path.getStations()
                .stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
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

    @Override
    public String toString() {
        return "PathResponse{" +
                "stations=" + stations +
                ", distance=" + distance +
                ", fare=" + fare +
                '}';
    }
}
