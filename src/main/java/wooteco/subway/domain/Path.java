package wooteco.subway.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Path {
    private final List<Station> stations;
    private final int distance;
    private final List<CustomEdge> edgeList;

    private Path(List<Station> stations, int distance, List<CustomEdge> edgeList1) {
        this.stations = stations;
        this.distance = distance;
        this.edgeList = edgeList1;
    }

    public static Path of(List<Station> stations, double distance, List<CustomEdge> edgeList) {
        return new Path(stations, (int) Math.floor(distance), edgeList);
    }

    public List<Long> findShortestPathLines(){
        return edgeList.stream()
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
