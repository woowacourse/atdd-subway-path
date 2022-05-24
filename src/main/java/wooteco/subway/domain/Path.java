package wooteco.subway.domain;

import java.util.List;
import java.util.Objects;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Path {
    private final List<Station> stations;
    private final Distance distance;

    private Path(List<Station> stations, Distance distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static Path from(ShortestPathAlgorithm<Station, DefaultWeightedEdge> shortestPathAlgorithm,
                            Station source, Station target) {
        checkStations(source, target);
        try {
            GraphPath<Station, DefaultWeightedEdge> path = shortestPathAlgorithm.getPath(source, target);
            checkPath(path);
            return new Path(path.getVertexList(), Distance.fromKilometer(path.getWeight()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("해당 역은 경로에 존재하지 않습니다.");
        }
    }

    private static void checkStations(Station source, Station target) {
        checkNull(source, target);
        checkEquals(source, target);
    }

    private static void checkNull(Station source, Station target) {
        if (Objects.isNull(source) || Objects.isNull(target)) {
            throw new IllegalArgumentException("출발역과 도착역은 모두 필수입니다.");
        }
    }

    private static void checkEquals(Station source, Station target) {
        if (Objects.equals(source, target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같아 경로를 찾을 수 없습니다.");
        }
    }

    private static void checkPath(GraphPath<Station, DefaultWeightedEdge> path) {
        if (Objects.isNull(path)) {
            throw new IllegalStateException("해당하는 경로가 존재하지 않습니다.");
        }
    }

    public int calculateFare() {
        return distance.calculateFare();
    }

    public List<Station> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance.getValue();
    }
}
