package wooteco.subway.domain.strategy.path;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;

@Component
public class PathFindDijkstraStrategy implements PathFindStrategy {

    @Override
    public Path calculatePath(Station source, Station target, Sections sections) {
        validateSameStation(source, target);

        WeightedMultigraph<Station , DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertex(sections, graph);
        addEdgeWeight(sections, graph);

        DijkstraShortestPath<Station, DefaultWeightedEdge> shortPath = new DijkstraShortestPath<>(graph);

        return new Path(
                shortPath.getPath(source, target).getVertexList(),
                (int) shortPath.getPathWeight(source, target));
    }

    private void addEdgeWeight(Sections sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Section value : sections.getValues()) {
            graph.setEdgeWeight(graph.addEdge(value.getUpStation(), value.getDownStation()), value.getDistance());
        }
    }

    private void addVertex(Sections sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Station station : sections.getDistinctStations()) {
            graph.addVertex(station);
        }
    }

    private void validateSameStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같을 수 없습니다.");
        }
    }
}
