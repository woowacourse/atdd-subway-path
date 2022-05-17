package wooteco.subway.service.dto;

import java.util.List;
import wooteco.subway.service.dto.station.StationResponseDto;

public class PathResponseDto {

    private final List<StationResponseDto> stations;
    private final int distance;
    private final int fare;

    public PathResponseDto(List<StationResponseDto> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationResponseDto> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
