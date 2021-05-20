package wooteco.subway.path.domain;

import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.List;

public abstract class GraphSubwayPathStrategy implements SubwayPathStrategy{

    protected final WeightedGraph<Station, DefaultWeightedEdge> graph;

    public GraphSubwayPathStrategy(List<Station> stations, List<Section> sections,
                WeightedGraph<Station, DefaultWeightedEdge> graph) {
        this.graph = graph;
        initializeGraph(stations, sections);
    }

    private void initializeGraph(List<Station> stations, List<Section> sections) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
        for (Section section : sections) {
            graph.setEdgeWeight(
                    graph.addEdge(section.getUpStation(), section.getDownStation()),
                    section.getDistance());
        }
    }
}
