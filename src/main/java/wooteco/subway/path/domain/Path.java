package wooteco.subway.path.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph
            = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath
            = new DijkstraShortestPath<>(graph);

    public Path(List<Station> stations, List<Section> sections) {
        refresh(stations, sections);
    }


    public void refresh(List<Station> stations, List<Section> sections) {
        List<Station> originStations = new ArrayList<>(graph.vertexSet());
        List<DefaultWeightedEdge> originEdge = new ArrayList(graph.edgeSet());

        graph.removeAllEdges(originEdge);
        graph.removeAllVertices(originStations);

        for (Station station : stations) {
            graph.addVertex(station);
        }
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    public List<Station> shortestPath(Station from, Station to) {
        return dijkstraShortestPath.getPath(from, to)
                                   .getVertexList();
    }

    public int shortestPathLength(Station from, Station to) {
        return (int) dijkstraShortestPath.getPathWeight(from, to);
    }
}
