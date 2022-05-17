package wooteco.subway.dto.info;

import java.util.List;

public class PathServiceResponse {
    private final List<StationDto> stations;
    private final int distance;

    public PathServiceResponse(List<StationDto> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<StationDto> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
