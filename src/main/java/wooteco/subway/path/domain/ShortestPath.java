package wooteco.subway.path.domain;

import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Component;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShortestPath {
    private AbstractBaseGraph<Station, DefaultWeightedEdge> graph;
    private ShortestPathAlgorithm<Station, DefaultWeightedEdge> shortestPathAlgorithm;

    public ShortestPath(AbstractBaseGraph<Station, DefaultWeightedEdge> graph, ShortestPathAlgorithm<Station, DefaultWeightedEdge> shortestPathAlgorithm) {
        this.graph = graph;
        this.shortestPathAlgorithm = shortestPathAlgorithm;
    }

    public void refresh(List<Station> stations, List<Section> sections) {
        clear();

        setVertex(stations);
        setEdge(sections);
    }

    private void clear() {
        List<Station> originStations = new ArrayList(graph.vertexSet());
        List<DefaultWeightedEdge> originEdge = new ArrayList(graph.edgeSet());

        graph.removeAllEdges(originEdge);
        graph.removeAllVertices(originStations);
    }

    private void setVertex(List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void setEdge(List<Section> sections) {
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    public List<Station> route(Station from, Station to) {
        return shortestPathAlgorithm.getPath(from, to)
                                    .getVertexList();
    }

    public int length(Station from, Station to) {
        return (int) shortestPathAlgorithm.getPathWeight(from, to);
    }
}
