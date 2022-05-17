package wooteco.subway.dto;

import java.util.List;
import java.util.stream.Collectors;

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

    public PathResponse(PathDto pathDto) {
        this(pathDto.getStations()
                        .stream()
                        .map(StationResponse::new)
                        .collect(Collectors.toList()),
                pathDto.getDistance(), pathDto.getFare());
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
