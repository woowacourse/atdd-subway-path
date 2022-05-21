package wooteco.subway.domain.path;

import wooteco.subway.domain.station.Station;

public class PathElement implements Comparable<PathElement> {
    private final Station station;
    private final int distance;

    public PathElement(Station station, int distance) {
        this.station = station;
        this.distance = distance;
    }

    public Station getStation() {
        return station;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public int compareTo(PathElement o) {
        return Integer.compare(this.distance, o.distance);
    }
}
