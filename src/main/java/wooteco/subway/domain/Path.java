package wooteco.subway.domain;

import java.util.Collections;
import java.util.List;

public class Path {
    private final List<Station> stations;
    private final int distance;
    private final Fare fare;

    private Path(List<Station> stations, int distance, Fare fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static Path from(List<Section> sections, Station departure, Station arrival) {
        ShortestPath shortestPath = ShortestPath.generate(sections, departure, arrival);
        final List<Station> stations = shortestPath.getPath();
        final int distance = shortestPath.getDistance();
        final Fare fare = Fare.from(distance);
        return new Path(stations, distance, fare);
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int getFare() {
        return this.fare.getFare();
    }

    public int getDistance() {
        return distance;
    }
}
