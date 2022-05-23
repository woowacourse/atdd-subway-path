package wooteco.subway.domain.path;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.domain.Lines;
import wooteco.subway.domain.Station;

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
        List<WeightEdgeWithLineId> edges = dijkstraShortestPath.getPath(source, target).getEdgeList();
        return edges.stream()
            .map(WeightEdgeWithLineId::getLineId)
            .collect(Collectors.toList());
    }

    private DijkstraShortestPath<Station, WeightEdgeWithLineId> getDijkstraShortestPath(Lines lines) {
        WeightedMultigraph<Station, WeightEdgeWithLineId> graph = new WeightedMultigraph<>(WeightEdgeWithLineId.class);
        for (Station station : lines.getStations()) {
            graph.addVertex(station);
        }
        lines.forEach(line -> {
            line.getSections().forEach(section -> {
                graph.addEdge(section.getUpStation(), section.getDownStation(),
                    new WeightEdgeWithLineId(line.getId(), section.getDistance()));
            });
        });
        return new DijkstraShortestPath<>(graph);
    }
}
