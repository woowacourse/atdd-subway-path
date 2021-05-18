package wooteco.subway.line.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class SectionGraph {

    private final List<Section> sections;
    private final DijkstraShortestPath dijkstraShortestPath;
    private final Station source;
    private final Station target;

    public SectionGraph(List<Section> sections, List<Station> stations, Station source, Station target) {
        this.sections = sections;
        this.dijkstraShortestPath = pathGraph(stations);
        this.source = source;
        this.target = target;
    }

    private DijkstraShortestPath pathGraph(List<Station> stations) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        for (Station station : stations) {
            graph.addVertex(station);
        }
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }

        return new DijkstraShortestPath(graph);
    }

    public List<Station> shortestPathOfStations() {
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    public int shortestDistance() {
        return (int) dijkstraShortestPath.getPath(source, target).getWeight();
    }
}
