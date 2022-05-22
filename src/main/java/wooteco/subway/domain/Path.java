package wooteco.subway.domain;

import java.util.List;

public class Path {

    private static final int BASIC_FARE = 1250;

    private List<Station> stations;
    private int extraFare;
    private int distance;

    public Path(List<Station> stations, int extraFare, int distance) {
        this.stations = stations;
        this.extraFare = extraFare;
        this.distance = distance;
    }

    public int chargeFare() {
        int distanceCharge = DistanceCharge.findDistanceCharge(distance).calculate(distance);
        return BASIC_FARE + distanceCharge;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getExtraFare() {
        return extraFare;
    }
}
