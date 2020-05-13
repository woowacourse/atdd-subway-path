package wooteco.subway.admin.dto;

import java.util.List;

public class MockPassInstance {
    List<StationResponse> stations;
    Long distance;

    public MockPassInstance() {
    }

    public MockPassInstance(List<StationResponse> stations, Long distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
    }
}
