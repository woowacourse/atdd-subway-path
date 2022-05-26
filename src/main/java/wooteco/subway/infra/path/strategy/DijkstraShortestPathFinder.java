package wooteco.subway.infra.path.strategy;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.infra.path.CustomEdge;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.Path;
import wooteco.subway.infra.path.PathFinder;
import wooteco.subway.exception.EmptyResultException;

import java.util.List;

@Component
public class DijkstraShortestPathFinder implements PathFinder {

    private static void addVertex(WeightedMultigraph<Station, CustomEdge> graph, List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private static void addEdge(WeightedMultigraph<Station, CustomEdge> graph, List<Section> sections, Long lineId) {
        for (Section section : sections) {
            graph.addEdge(section.getUpStation(), section.getDownStation(), new CustomEdge(lineId, section.getDistance()));
        }
    }

    @Override
    public Path findShortestPath(Station source, Station target, List<Line> lines) {
        WeightedMultigraph<Station, CustomEdge> graph = new WeightedMultigraph<>(CustomEdge.class);

        setGraph(lines, graph);

        DijkstraShortestPath<Station, CustomEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, CustomEdge> path = dijkstraShortestPath.getPath(source, target);
        checkNoPath(path);

        return Path.of(path.getVertexList(), path.getWeight(), path.getEdgeList());
    }

    private void setGraph(List<Line> lines, WeightedMultigraph<Station, CustomEdge> graph) {
        for (Line line : lines) {
            addVertex(graph, line.getStations());
        }

        for (Line line : lines) {
            addEdge(graph, line.getSections(), line.getId());
        }
    }

    private void checkNoPath(GraphPath<Station, CustomEdge> path) {
        if (path == null) {
            throw new EmptyResultException("출발역과 도착역 사이에 연결된 경로가 없습니다.");
        }
    }
}
