package wooteco.subway.utils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.ShortestPathEdge;
import wooteco.subway.domain.Station;

public class Jgrapht {

    public static DijkstraShortestPath initSectionGraph(List<Section> sections) {
        WeightedMultigraph<Station, ShortestPathEdge> graph
                = new WeightedMultigraph<>(ShortestPathEdge.class);
        addVertex(sections, graph);
        addEdge(sections, graph);
        return new DijkstraShortestPath<>(graph);
    }

    public static List<Station> createShortestPath(DijkstraShortestPath<Station, ShortestPathEdge> shortestPath, Station upStation, Station downStation) {
        return shortestPath.getPath(upStation, downStation).getVertexList();
    }

    public static int calculateDistance(DijkstraShortestPath<Station, ShortestPathEdge> shortestPath, Station upStation, Station downStation) {
        return (int) (shortestPath.getPath(upStation, downStation).getWeight());
    }

    public static int calculateExtraFare(DijkstraShortestPath<Station, ShortestPathEdge> shortestPath, Station upStation, Station downStation) {
        List<ShortestPathEdge> edgeList = shortestPath.getPath(upStation, downStation).getEdgeList();
        return edgeList.stream()
                .mapToInt(edge -> edge.getExtraFare())
                .max()
                .orElseGet(() -> 0);
    }

    private static void addVertex(List<Section> sections, WeightedMultigraph<Station, ShortestPathEdge> graph) {
        Set<Station> stations = getStations(sections);
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private static void addEdge(List<Section> sections, WeightedMultigraph<Station, ShortestPathEdge> graph) {
        for (Section section : sections) {
            Line line = section.getLine();
            int distance = section.getDistance();
            graph.addEdge(section.getUpStation(), section.getDownStation(), new ShortestPathEdge(line.getExtraFare(), distance));
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
