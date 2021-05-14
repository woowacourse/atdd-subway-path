package wooteco.subway.controller.dto.response;

import java.util.List;

public class PathResponseDto {
    private List<StationResponseDto> stations;
    private int distance;

    public PathResponseDto() {
    }

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
