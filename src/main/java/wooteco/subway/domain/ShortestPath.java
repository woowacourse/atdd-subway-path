package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class ShortestPath {

    private final GraphPath<Station, DefaultWeightedEdge> path;

    private ShortestPath(GraphPath<Station, DefaultWeightedEdge> path) {
        this.path = path;
    }

    public static ShortestPath generate(List<Section> sections, Station departure, Station arrival) {
        return new ShortestPath(generatePath(sections, departure, arrival));
    }

    private static GraphPath<Station, DefaultWeightedEdge> generatePath(List<Section> sections, Station departure,
                                                                        Station arrival) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
                DefaultWeightedEdge.class);

        addSectionsToGraph(sections, graph);

        final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        try {
            return shortestPath.getPath(departure, arrival);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("찾으시는 경로가 존재하지 않습니다!");
        }
    }

    private static void addSectionsToGraph(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Section section : sections) {
            final Station upStation = section.getUpStation();
            final Station downStation = section.getDownStation();

            graph.addVertex(upStation);
            graph.addVertex(downStation);
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
        }
    }

    public List<Station> getPath() {
        return path.getVertexList();
    }

    public int getDistance() {
        return (int) path.getWeight();
    }
}
