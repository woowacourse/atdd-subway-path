package wooteco.subway.domain.path;

import java.util.List;
import java.util.Objects;
import wooteco.subway.domain.station.Station;

public class Path {

    private final int totalDistance;
    private final List<Station> stations;

    public Path(int totalDistance, List<Station> stations) {
        this.totalDistance = totalDistance;
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }

     public int calculateFare() {
         int basicFare = 1250;
         int fareBetween10And50 = Math.min(800, calculateOverFare(totalDistance - 10, 5));
         int fareOver50 = calculateOverFare(totalDistance - 50, 8);
         return basicFare + fareBetween10And50 + fareOver50;
    }

    private int calculateOverFare(int overDistance, int limit) {
        if (overDistance <= 0) {
            return 0;
        }
        return (int) ((Math.ceil((overDistance - 1) / limit) + 1) * 100);
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
        return totalDistance == that.totalDistance && Objects.equals(stations, that.stations);
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
