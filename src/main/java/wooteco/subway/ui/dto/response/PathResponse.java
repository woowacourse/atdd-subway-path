package wooteco.subway.ui.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.service.dto.response.PathServiceResponse;

public class PathResponse {

    private List<StationResponse> stations;
    private Long distance;
    private Long fare;

    private PathResponse() {
    }

    public PathResponse(List<StationResponse> stationResponses, Long distance, Long fare) {
        this.stations = stationResponses;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse from(PathServiceResponse pathServiceResponse) {
        final List<StationResponse> stationResponses = pathServiceResponse.getStations()
                .stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());

        return new PathResponse(stationResponses, pathServiceResponse.getDistance(), pathServiceResponse.getFare());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
    }

    public Long getFare() {
        return fare;
    }
}
