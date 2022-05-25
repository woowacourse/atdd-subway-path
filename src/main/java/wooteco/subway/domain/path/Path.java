package wooteco.subway.domain.path;

import java.util.List;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.fare.Fare;

public class Path {

    private final List<Station> stations;
    private final int distance;
    private final Fare fare;

    public Path(List<Station> stations, int distance, Fare fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare.getFare();
    }
}
