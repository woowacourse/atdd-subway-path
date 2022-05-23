package wooteco.subway.domain.path;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import wooteco.subway.domain.Station;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.Lines;

@Component
public class DijkstraPathFindingStrategy implements PathFindingStrategy {
    @Override
    public int getShortestDistance(Station source, Station target, Lines lines) {
        DijkstraShortestPath<Station, WeightEdgeWithLineId> dijkstraShortestPath = getDijkstraShortestPath(lines);

        return (int)dijkstraShortestPath.getPath(source, target).getWeight();
    }

    @Override
    public List<Station> getShortestPath(Station source, Station target, Lines lines) {
        DijkstraShortestPath<Station, WeightEdgeWithLineId> dijkstraShortestPath = getDijkstraShortestPath(lines);

        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    @Override
    public List<Long> getLineIds(Station source, Station target, Lines lines) {
        DijkstraShortestPath<Station, WeightEdgeWithLineId> dijkstraShortestPath = getDijkstraShortestPath(lines);
        Set<WeightEdgeWithLineId> edges = new LinkedHashSet<>(
            dijkstraShortestPath.getPath(source, target).getEdgeList());
        return edges.stream()
            .map(WeightEdgeWithLineId::getLineId)
            .collect(Collectors.toList());
    }

    private DijkstraShortestPath<Station, WeightEdgeWithLineId> getDijkstraShortestPath(Lines lines) {
        WeightedMultigraph<Station, WeightEdgeWithLineId> graph = new WeightedMultigraph<>(WeightEdgeWithLineId.class);

        for (Station station : lines.getStations()) {
            graph.addVertex(station);
        }
        lines.forEach(line -> makeGraph(graph, line));
        return new DijkstraShortestPath<>(graph);
    }

    private void makeGraph(WeightedMultigraph<Station, WeightEdgeWithLineId> graph, Line line) {
        line.getSections().forEach(section -> {
            graph.addEdge(section.getUpStation(), section.getDownStation(),
                new WeightEdgeWithLineId(line.getId(), section.getDistance()));
        });
    }
}
