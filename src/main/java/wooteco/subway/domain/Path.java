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
        if (isUnder10km(distance)) {
            return BASIC_FARE + extraFare;
        }
        if (isBetween10kmAnd50km(distance)) {
            return calculateOverFareUnder50(distance) + extraFare;
        }
        return calculateOverFareOver50(distance) + extraFare;
    }

    private boolean isBetween10kmAnd50km(double distance) {
        return distance <= 50;
    }

    private boolean isUnder10km(double distance) {
        return distance <= 10;
    }

    private int calculateOverFareUnder50(double distance) {
        return (int) (Math.ceil((distance - 10) / 5) * 100) + BASIC_FARE;
    }

    private int calculateOverFareOver50(double distance) {
        return calculateOverFareUnder50(50) +
                (int) (Math.ceil((distance - 50) / 8) * 100);
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
