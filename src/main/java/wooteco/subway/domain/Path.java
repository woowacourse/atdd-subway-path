package wooteco.subway.domain;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public Path(List<Section> sections) {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        addVertex(sections);
        addEdge(sections);
    }

    public List<Station> createShortestPath(Station upStation, Station downStation) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        return dijkstraShortestPath.getPath(upStation,
                downStation).getVertexList();
    }

    public int calculateDistance(Station upStation, Station downStation) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return (int) (dijkstraShortestPath.getPath(upStation, downStation).getWeight());
    }

    private void addVertex(List<Section> sections) {
        Set<Station> stations = getStations(sections);
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void addEdge(List<Section> sections) {
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(),
                    section.getDownStation()), section.getDistance());
        }
    }

    private Set<Station> getStations(List<Section> sections) {
        Set<Station> stations = new LinkedHashSet<>();
        for (Section section : sections) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }
        return stations;
    }
}
