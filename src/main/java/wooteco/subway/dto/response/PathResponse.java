package wooteco.subway.dto.response;

import java.util.List;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Station;

public class PathResponse {

    private final List<Station> stations;
    private final int distance;
    private final int fare;

    public PathResponse(Path path, int fare) {
        this.stations = path.getStations();
        this.distance = path.getDistance();
        this.fare = fare;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
