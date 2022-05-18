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
