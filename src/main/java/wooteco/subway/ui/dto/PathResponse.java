package wooteco.subway.ui.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.service.dto.PathServiceResponse;

public class PathResponse {

    private List<StationResponse> stationResponses;
    private int distance;
    private int fare;

    public PathResponse() {
    }

    private PathResponse(List<StationResponse> stationResponses, int distance, int fare) {
        this.stationResponses = stationResponses;
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

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
