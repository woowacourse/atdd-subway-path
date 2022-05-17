package wooteco.subway.dto.response;

import java.util.List;
import wooteco.subway.domain.Station;

public class PathResponse {
    private List<Station> stations;
    private Long distance;
    private int fare;

    public PathResponse(List<Station> stations, Long distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
