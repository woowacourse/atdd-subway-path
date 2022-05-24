package wooteco.subway.ui.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.service.dto.PathServiceResponse;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private int fare;

    public PathResponse() {
    }

    public PathResponse(PathServiceResponse pathServiceResponse) {
        this.stations = pathServiceResponse.getStations()
                .stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
        this.distance = pathServiceResponse.getDistance();
        this.fare = pathServiceResponse.getFare();
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
