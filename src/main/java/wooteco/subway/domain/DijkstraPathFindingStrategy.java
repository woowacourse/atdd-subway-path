package wooteco.subway.domain;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

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

    private DijkstraShortestPath<Station, WeightEdgeWithLineId> getDijkstraShortestPath(Lines lines) {
        WeightedMultigraph<Station, WeightEdgeWithLineId> graph = new WeightedMultigraph<>(WeightEdgeWithLineId.class);
        for (Station station : lines.getStations()) {
            graph.addVertex(station);
        }
        lines.forEach(line -> {
            line.getSections().forEach(section -> {
                WeightEdgeWithLineId weightEdgeWithLineId = new WeightEdgeWithLineId(line.getId(),
                    section.getDistance());
                graph.addEdge(section.getUpStation(), section.getDownStation(), weightEdgeWithLineId);
            });
        });
        return new DijkstraShortestPath<>(graph);
    }
}
