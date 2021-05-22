package wooteco.subway.path.application.dto;

import java.util.List;

public class PathResponseDto {

    private final List<StationResponseDto> stations;
    private final int distance;

    public PathResponseDto(List<StationResponseDto> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<StationResponseDto> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

}
