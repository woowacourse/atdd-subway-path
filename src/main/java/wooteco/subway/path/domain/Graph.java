package wooteco.subway.path.domain;

import java.util.HashSet;
import java.util.Set;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.exceptions.SubWayException;
import wooteco.subway.exceptions.SubWayExceptionSet;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class Graph {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public Graph(List<Section> sections) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        resetGraph(sections);
    }

    public void resetGraph(List<Section> sections) {
        removeAllVertexes();
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    private void removeAllVertexes() {
        Set<Station> stations = new HashSet<>(graph.vertexSet());
        graph.removeAllVertices(stations);
    }

    public void validateStation(Station station) {
        if (!graph.containsVertex(station)) {
            throw new SubWayException(SubWayExceptionSet.NOT_CONNECT_STATION_EXCEPTION);
        }
    }

    public WeightedMultigraph<Station, DefaultWeightedEdge> getGraph() {
        return graph;
    }
}
