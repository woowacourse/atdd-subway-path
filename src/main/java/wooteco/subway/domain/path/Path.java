package wooteco.subway.domain.path;

import java.util.List;
import java.util.Objects;
import wooteco.subway.domain.station.Station;

public class Path {

    private final int totalDistance;
    private final List<Station> stations;
    private final int extraFare;

    public Path(int totalDistance, List<Station> stations, int extraFare) {
        this.totalDistance = totalDistance;
        this.stations = stations;
        this.extraFare = extraFare;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getExtraFare() {
        return extraFare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Path path = (Path) o;
        return totalDistance == path.totalDistance && extraFare == path.extraFare && Objects.equals(stations,
                path.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalDistance, stations, extraFare);
    }

    @Override
    public String toString() {
        return "PathResult{" + "totalDistance=" + totalDistance + ", stations=" + stations + '}';
    }
}
