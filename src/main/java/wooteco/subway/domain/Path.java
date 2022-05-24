package wooteco.subway.domain;

import java.util.List;
import java.util.Objects;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Path path = (Path) o;
        return Objects.equals(stations, path.stations) && Objects.equals(distance, path.distance)
                && Objects.equals(fare, path.fare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance, fare);
    }

    @Override
    public String toString() {
        return "Path{" +
                "stations=" + stations +
                ", distance=" + distance +
                ", fare=" + fare +
                '}';
    }
}
