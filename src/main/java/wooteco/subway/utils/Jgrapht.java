package wooteco.subway.utils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

public class Jgrapht {

    public static DijkstraShortestPath initSectionGraph(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertex(sections, graph);
        addEdge(sections, graph);
        return new DijkstraShortestPath(graph);
    }

    public static List<Station> createShortestPath(DijkstraShortestPath shortestPath, Station upStation, Station downStation) {
        return shortestPath.getPath(upStation,
                downStation).getVertexList();
    }

    public static int calculateDistance(DijkstraShortestPath shortestPath, Station upStation, Station downStation) {
        return (int) (shortestPath.getPath(upStation, downStation).getWeight());
    }

    private static void addVertex(List<Section> sections,
            WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        Set<Station> stations = getStations(sections);
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private static void addEdge(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(),
                    section.getDownStation()), section.getDistance());
        }
    }

    private static Set<Station> getStations(List<Section> sections) {
        Set<Station> stations = new LinkedHashSet<>();
        for (Section section : sections) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }
        return stations;
    }
}