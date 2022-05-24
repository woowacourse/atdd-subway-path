package wooteco.subway.domain.path;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;

public class Path {

    private final List<Station> vertexes;
    private final List<SectionWeightedEdge> edges;
    private final int distance;

    public Path(GraphPath<Station, SectionWeightedEdge> path) {
        validatePathExist(path);
        this.vertexes = path.getVertexList();
        this.edges = path.getEdgeList();
        this.distance = (int) path.getWeight();
    }

    private void validatePathExist(GraphPath<Station, SectionWeightedEdge> path) {
        if (path == null) {
            throw new IllegalStateException("해당 경로가 존재하지 않습니다.");
        }
    }

    public int getMaxExtraFare(List<Line> lines) {
        return lines.stream()
                .filter(line -> findPassedLineIds().contains(line.getId()))
                .mapToInt(Line::getExtraFare)
                .distinct()
                .max()
                .orElseThrow(() -> new IllegalStateException("가장 비싼 요금을 구할 수 없습니다."));
    }

    public List<Long> findPassedLineIds() {
        return edges.stream().map(SectionWeightedEdge::getLineId)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Station> getVertexes() {
        return vertexes;
    }

    public int getDistance() {
        return distance;
    }
}
