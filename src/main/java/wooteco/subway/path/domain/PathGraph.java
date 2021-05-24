package wooteco.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class PathGraph {
    private final List<Line> lines;

    public PathGraph(List<Line> lines) {
        this.lines = lines;
    }

    public WeightedMultigraph<Station, DefaultWeightedEdge> graph() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph =
                new WeightedMultigraph<>(DefaultWeightedEdge.class);
        makeGraph(graph);
        return graph;
    }

    private void makeGraph(final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Line line : lines) {
            addStationVertex(graph, line.getStations());
            setStationEdgeWeight(graph, line.sections());
        }
    }

    private void addStationVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void setStationEdgeWeight(final WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections) {
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }
}
