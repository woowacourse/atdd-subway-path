package wooteco.subway.domain;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {
    private final Lines lines;

    public Path(Lines lines) {
        this.lines = lines;
    }

    public int getShortestDistance(Station source, Station target) {
        DijkstraShortestPath<Station, WeightEdgeWithLineId> dijkstraShortestPath = getDijkstraShortestPath();

        return (int)dijkstraShortestPath.getPath(source, target).getWeight();
    }

    public List<Station> getShortestPath(Station source, Station target) {
        DijkstraShortestPath<Station, WeightEdgeWithLineId> dijkstraShortestPath = getDijkstraShortestPath();

        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    private DijkstraShortestPath<Station, WeightEdgeWithLineId> getDijkstraShortestPath() {
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
