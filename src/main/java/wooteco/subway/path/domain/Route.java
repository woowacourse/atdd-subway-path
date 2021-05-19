package wooteco.subway.path.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

public class Route {
    
    private final WeightedMultigraph<Station, DefaultWeightedEdge> route;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public Route(List<Section> sections) {
        route = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        dijkstraShortestPath = new DijkstraShortestPath<>(route);
        initVertex(findUniqueStations(sections));
        initEdgeAndWeight(sections);
    }

    private void initVertex(List<Station> stations) {
        for (Station station : stations) {
            route.addVertex(station);
        }
    }

    private void initEdgeAndWeight(List<Section> sections) {
        for (Section section : sections) {
            route.setEdgeWeight(route.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    private List<Station> findUniqueStations(List<Section> sections) {
        Set<Station> stations = new HashSet<>();
        for (Section section : sections) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }
        return new ArrayList<>(stations);
    }

    public List<Station> findShortestStation(Station sourceStation, Station targetStation) {
        return dijkstraShortestPath.getPath(sourceStation, targetStation).getVertexList();
    }

    public int findShortestDistance(Station sourceStation, Station targetStation) {
        return (int) dijkstraShortestPath.getPath(sourceStation, targetStation).getWeight();
    }
}
