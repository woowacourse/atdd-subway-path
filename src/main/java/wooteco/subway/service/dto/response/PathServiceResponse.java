package wooteco.subway.service.dto.response;

import java.util.List;
import wooteco.subway.domain.Station;

public class PathServiceResponse {

    private final List<Station> stations;
    private final Long distance;
    private final Long fare;

    public PathServiceResponse(List<Station> stations, Long distance, Long fare) {
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

    public Long getFare() {
        return fare;
    }
}
