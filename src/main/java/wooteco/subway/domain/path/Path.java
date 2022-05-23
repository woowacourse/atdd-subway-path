package wooteco.subway.domain.path;

import java.util.List;
import java.util.NoSuchElementException;
import wooteco.subway.domain.Station;

public class Path {

    private final List<Station> vertices;
    private final List<SubwayWeightEdge> edges;
    private final int distance;

    public Path(final List<Station> vertices, final List<SubwayWeightEdge> edges, final int distance) {
        this.vertices = vertices;
        this.edges = edges;
        this.distance = distance;
    }

    public int getMaxExtraFare() {
        return edges.stream()
                .mapToInt(SubwayWeightEdge::getExtraFare)
                .max()
                .orElseThrow(() -> new NoSuchElementException("가장 높은 금액의 추가 요금을 찾는 도중 오류가 발생했습니다."));
    }

    public List<Station> getVertices() {
        return vertices;
    }

    public int getDistance() {
        return distance;
    }
}
