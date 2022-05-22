package wooteco.subway.domain;

import java.util.LinkedList;
import java.util.List;

public class Path implements Shortest {
    private final List<Station> stations;
    private final int distance;
    private final double fare;

    public Path(final List<Station> stations, final int distance, final double fare) {
        this.stations = new LinkedList<>(stations);
        this.distance = distance;
        this.fare = fare;
    }

    public List<Station> getStations() {
        return new LinkedList<>(stations);
    }

    public int getDistance() {
        return distance;
    }

    public double getFare() {
        return fare;
    }

    @Override
    public Path getShortestPath(final Station source, final Station target, final Fare fare, final int age) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Long getExpensiveLineId(final Station source, final Station target) {
        throw new UnsupportedOperationException();
    }
}
