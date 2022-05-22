package wooteco.subway.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.service.dto.PathDto;
import wooteco.subway.service.dto.StationDto;

public class PathResponse {

    private final List<StationResponse> stations;
    private final int distance;
    private final int fare;

    public PathResponse(List<StationDto> stations, int distance, int fare) {
        this.stations = stations.stream()
            .map(StationResponse::from)
            .collect(Collectors.toList());
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse from(PathDto pathDto) {
        return new PathResponse(pathDto.getStations(), pathDto.getDistance(), pathDto.getFare());
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
