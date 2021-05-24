package wooteco.subway.path.repository;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Repository;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.domain.PathRepository;
import wooteco.subway.station.domain.Station;

import java.util.List;

@Repository
public class PathRepositoryImpl implements PathRepository {
    DijkstraShortestPath<Station, DefaultWeightedEdge> graph;

    @Override
    public Path generateShortestDistancePath(final Station start, final Station destination) {
        if (graph == null) {
            return new Path();
        }

        GraphPath<Station, DefaultWeightedEdge> shortestPath = graph.getPath(start, destination);
        return new Path(shortestPath.getVertexList(), (int) shortestPath.getWeight());
    }

    @Override
    public void generateAllPath(List<Line> lines) {
        if (lines.isEmpty()) {
            return;
        }

        graph = new DijkstraShortestPath<>(buildAllPath(lines));
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> buildAllPath(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        lines.forEach(line -> {
            registerNode(line.getStations(), graph);
            registerEdgeDistanceWeight(line.getSections(), graph);
        });

        return graph;
    }

    private void registerNode(List<Station> stations, WeightedMultigraph graph) {
        stations.forEach(graph::addVertex);
    }

    private void registerEdgeDistanceWeight(Sections sections, WeightedMultigraph graph) {
        sections.getSections().forEach(section -> {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
                    section.getDistance());
        });
    }
}
