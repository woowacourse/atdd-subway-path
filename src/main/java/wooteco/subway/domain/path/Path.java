package wooteco.subway.domain.path;

import java.util.List;
import java.util.Objects;
import wooteco.subway.domain.station.Station;

public class Path {

    private static final int BASIC_FARE = 1250;
    private static final int OVER_FARE_DIGIT = 100;
    private static final int FIRST_OVER_FARE_AREA_THRESHOLD = 10;
    private static final int SECOND_OVER_FARE_AREA_THRESHOLD = 50;
    private static final int FIRST_OVER_FARE_DISTANCE_LIMIT = 5;
    private static final int SECOND_OVER_FARE_DISTANCE_LIMIT = 8;

    private final int totalDistance;
    private final List<Station> stations;

    public Path(int totalDistance, List<Station> stations) {
        this.totalDistance = totalDistance;
        this.stations = stations;
    }

    public int calculateFare() {
        if (totalDistance <= FIRST_OVER_FARE_AREA_THRESHOLD) {
            return BASIC_FARE;
        }
        return BASIC_FARE + getOverFare();
    }

    private int getOverFare() {
        if (totalDistance <= SECOND_OVER_FARE_AREA_THRESHOLD) {
            int overDistance = totalDistance - FIRST_OVER_FARE_AREA_THRESHOLD;
            return calculateOverFare(overDistance, FIRST_OVER_FARE_DISTANCE_LIMIT);
        }
        int overDistance = totalDistance - SECOND_OVER_FARE_AREA_THRESHOLD;
        return getFirstAreaMaxOverFare() + calculateOverFare(overDistance, SECOND_OVER_FARE_DISTANCE_LIMIT);
    }

    private int getFirstAreaMaxOverFare() {
        int firstAreaDistance = SECOND_OVER_FARE_AREA_THRESHOLD - FIRST_OVER_FARE_AREA_THRESHOLD;
        return (firstAreaDistance / FIRST_OVER_FARE_DISTANCE_LIMIT) * OVER_FARE_DIGIT;
    }

    private int calculateOverFare(int overDistance, int limit) {
        double overDigit = Math.ceil((overDistance - 1) / limit) + 1;
        return (int) (overDigit * OVER_FARE_DIGIT);
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public List<Station> getStations() {
        return stations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Path that = (Path) o;
        return totalDistance == that.totalDistance
                && Objects.equals(stations, that.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalDistance, stations);
    }

    @Override
    public String toString() {
        return "PathResult{" + "totalDistance=" + totalDistance + ", stations=" + stations + '}';
    }
}
