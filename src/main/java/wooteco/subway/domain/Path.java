package wooteco.subway.domain;

import java.util.List;

public class Path {
    private final List<Station> stations;
    private final int distance;

    private Path(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static Path of(List<Station> stations, double distance) {
        return new Path(stations, (int) Math.floor(distance));
    }

    public List<Station> getStations() {
        return this.stations;
    }

    public int getDistance() {
        return this.distance;
    }

}
