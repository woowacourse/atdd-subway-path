package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Route {

    private final List<Section> sections;

    public Route() {
        this.sections = new ArrayList<>();
    }

    public void addSections(final List<Section> sections) {
        this.sections.addAll(sections);
    }

    public List<Station> calculateShortestPath(final Station source, final Station target) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        Sections sections = new Sections(this.sections);
        List<Station> stations = sections.getStations();
        for (Station station : stations) {
            graph.addVertex(station);
        }
        for (Section section : this.sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<Station> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();
        return shortestPath;
    }
}
