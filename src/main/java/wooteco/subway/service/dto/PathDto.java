package wooteco.subway.service.dto;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.domain.Station;

public class PathDto {

    private final List<StationDto> stations;
    private final int distance;
    private final int fare;

    public PathDto(List<Station> stations, int distance, int fare) {
        this.stations = toStationDtos(List.copyOf(stations));
        this.distance = distance;
        this.fare = fare;
    }

    private List<StationDto> toStationDtos(List<Station> stations) {
        return stations.stream()
            .map(StationDto::from)
            .collect(Collectors.toUnmodifiableList());
    }

    public List<StationDto> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
