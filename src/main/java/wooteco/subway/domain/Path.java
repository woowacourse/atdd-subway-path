package wooteco.subway.domain;

import java.util.Collections;
import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {
    private final List<Station> stations;
    private final int distance;
    private final Cost cost;

    private Path(List<Station> stations, int distance, Cost cost) {
        this.stations = stations;
        this.distance = distance;
        this.cost = cost;
    }

    public static Path from(List<Section> sections, Station departure, Station arrival) {
        final GraphPath<Station, DefaultWeightedEdge> path = generatePath(sections, departure, arrival);
        final List<Station> stations = path.getVertexList();
        final int distance = (int) path.getWeight();
        final Cost cost = Cost.from(distance);
        return new Path(stations, distance, cost);
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

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int getCost() {
        return this.cost.getCost();
    }

    public int getDistance() {
        return distance;
    }
}
