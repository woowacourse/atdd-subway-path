package wooteco.subway.domain.path;

import java.util.List;
import wooteco.subway.domain.path.fare.Fare;
import wooteco.subway.domain.station.Station;

public class Path {

    private final List<Station> stations;
    private final int distance;
    private final int fare;

    public Path(List<Station> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static Path of(List<Station> stations, int distance) {
        Fare fare = Fare.of(distance);
        return new Path(stations, distance, fare.getValue());
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

    @Override
    public String toString() {
        return "Path{" +
                "stations=" + stations +
                ", distance=" + distance +
                ", fare=" + fare +
                '}';
    }
}
