package wooteco.subway.domain.path;

import wooteco.subway.domain.station.Station;

public class PathElement implements Comparable<PathElement> {
    private final Station station;
    private final int distance;
    private final int extraFare;

    PathElement(Station station, int distance, int extraFare) {
        this.station = station;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public Station getStation() {
        return station;
    }

    public int getDistance() {
        return distance;
    }

    public int getExtraFare() {
        return extraFare;
    }

    @Override
    public int compareTo(PathElement o) {
        if (Integer.compare(this.distance, o.distance) == 0) {
            return Integer.compare(this.extraFare, o.extraFare);
        }
        return Integer.compare(this.distance, o.distance);
    }
}
