package wooteco.subway.domain.vo;

import java.util.List;

import wooteco.subway.domain.Lines;
import wooteco.subway.domain.Station;

public class Path {

    private final List<Station> stations;
    private final int distance;
    private final Lines lines;

    private Path(List<Station> stations, int distance, Lines lines) {
        this.stations = stations;
        this.distance = distance;
        this.lines = lines;
    }

    public static Path of(List<Station> stations, double distance, Lines lines) {
        return new Path(stations, (int) Math.floor(distance), lines);
    }

    public List<Station> getStations() {
        return this.stations;
    }

    public int getDistance() {
        return this.distance;
    }

    public Lines getLines() {
        return lines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Path path = (Path)o;

        if (getDistance() != path.getDistance())
            return false;
        return getStations() != null ? getStations().equals(path.getStations()) : path.getStations() == null;
    }

    @Override
    public int hashCode() {
        int result = getStations() != null ? getStations().hashCode() : 0;
        result = 31 * result + getDistance();
        return result;
    }
}
