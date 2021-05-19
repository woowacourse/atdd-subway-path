package wooteco.subway.path.domain;

import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class PathGraph {
    private final WeightedGraph<Station, DefaultWeightedEdge> graph;
    private final List<Line> lines;

    public PathGraph(final List<Line> lines, final WeightedGraph<Station, DefaultWeightedEdge> graph) {
        this.lines = new ArrayList<>(lines);
        this.graph = graph;
        setGraph();
    }

    private void setGraph() {
        for (Line line : lines) {
            setVertex(line.getStations());
            setEdge(line.getSections());
        }
    }

    private void setVertex(final List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void setEdge(final Sections sections) {
        for (Section section : sections.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    public WeightedGraph<Station, DefaultWeightedEdge> getGraph() {
        return graph;
    }
}
