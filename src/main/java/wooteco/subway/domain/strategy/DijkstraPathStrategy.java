package wooteco.subway.domain.strategy;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.duplicate.DuplicateStationException;
import wooteco.subway.exception.invalidrequest.InvalidPathRequestException;

@Component
public class DijkstraPathStrategy implements PathStrategy {

    @Override
    public Path calculatePath(Station source, Station target, Sections sections) {
        validateSourceAndTargetNotSame(source, target);
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertices(sections, graph);
        addEdges(sections, graph);
        DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        try {
            return new Path(shortestPath.getPath(source, target).getVertexList(),
                    (int) shortestPath.getPathWeight(source, target));
        } catch (IllegalArgumentException e) {
            throw new InvalidPathRequestException("목적지까지 도달할 수 없습니다.");
        }
    }

    private void addVertices(Sections sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Station station : sections.getDistinctStations()) {
            graph.addVertex(station);
        }
    }

    private void addEdges(Sections sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Section value : sections.getValues()) {
            graph.setEdgeWeight(graph.addEdge(value.getUpStation(), value.getDownStation()), value.getDistance());
        }
    }

    private void validateSourceAndTargetNotSame(Station source, Station target) {
        if (source.equals(target)) {
            throw new DuplicateStationException("경로의 시작과 끝은 같은 역일 수 없습니다.");
        }
    }

}
