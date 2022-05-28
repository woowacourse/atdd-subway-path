package wooteco.subway.controller.dto.response;

import java.util.List;

public class PathResponseForm {

    private final List<StationResponseForm> stations;
    private final Long distance;
    private final Long fare;

    public PathResponseForm(List<StationResponseForm> stations, Long distance, Long fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationResponseForm> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
    }

    public Long getFare() {
        return fare;
    }
}
