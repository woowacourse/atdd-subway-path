package wooteco.subway.ui.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.service.dto.PathServiceResponse;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private int fare;

    private PathResponse() {
    }

    private PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public PathResponse(PathServiceResponse pathServiceResponse) {
        this(pathServiceResponse.getStations()
                        .stream()
                        .map(StationResponse::new)
                        .collect(Collectors.toList()),
                pathServiceResponse.getDistance(), pathServiceResponse.getFare());
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
