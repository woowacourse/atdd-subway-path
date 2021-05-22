package wooteco.subway.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }
}
