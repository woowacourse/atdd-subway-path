package wooteco.subway.path.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

public class JgraphtPathFinder implements ShortestPathFinder {

    private final WeightedMultigraph graph;

    public JgraphtPathFinder(List<Station> stations, List<Section> sections) {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        registerLines(stations);
        registerSections(sections);
    }

    private void registerSections(List<Section> sections) {
        for (Section section : sections) {
            graph.setEdgeWeight(
                graph.addEdge(section.getUpStation(), section.getDownStation()),
                section.getDistance()
            );
        }
    }

    private void registerLines(List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    @Override
    public List<Station> findShortestPath(Station from, Station to) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath.getPath(from,to).getVertexList();
    }

    @Override
    public int findShortestDistance(Station from, Station to) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return (int) dijkstraShortestPath.getPath(from,to).getWeight();
    }
}
