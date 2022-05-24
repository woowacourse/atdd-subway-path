package wooteco.subway.domain;

import java.util.List;
import wooteco.subway.domain.fare.Fare;

public class Path {

    private final List<Station> stations;
    private final Distance distance;
    private final Fare fare;

    public Path(final List<Station> stations, final Distance distance, final Fare fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }

    public Fare getFare() {
        return fare;
    }
}
