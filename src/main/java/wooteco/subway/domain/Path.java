package wooteco.subway.domain;

import java.util.Collections;
import java.util.List;

public class Path {

    private final List<Station> stations;
    private final int distance;
    private final Fare fare;

    public Path(final List<Station> stations, final int distance, final Fare fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int getDistance() {
        return distance;
    }

    public Fare getFare() {
        return fare;
    }
}
