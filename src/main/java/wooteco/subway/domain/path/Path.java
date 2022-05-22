package wooteco.subway.domain.path;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
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

    public List<Station> getVertexes() {
        return vertexes;
    }

    public List<Long> findPassedLineIds() {
        return edges.stream().map(SectionWeightedEdge::getLineId)
                .distinct()
                .collect(Collectors.toList());
    }

    public int getDistance() {
        return distance;
    }
}
