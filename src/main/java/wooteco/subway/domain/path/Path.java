package wooteco.subway.domain.path;

import wooteco.subway.domain.CustomEdge;
import wooteco.subway.domain.Station;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Path {
    private final List<Station> stations;
    private final int distance;
    private final List<CustomEdge> edges;

    private Path(List<Station> stations, int distance, List<CustomEdge> edges) {
        this.stations = stations;
        this.distance = distance;
        this.edges = edges;
    }

    public static Path of(List<Station> stations, double distance, List<CustomEdge> edges) {
        return new Path(stations, (int) Math.floor(distance), edges);
    }

    public List<Long> findShortestPathLines() {
        return edges.stream()
                .map(CustomEdge::getLineId)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Station> getStations() {
        return this.stations;
    }

    public int getDistance() {
        return this.distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return distance == path.distance && Objects.equals(stations, path.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance);
    }
}
