package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayGraph {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> subwayGraph;

    public SubwayGraph() {
        subwayGraph = new WeightedMultigraph(DefaultWeightedEdge.class);
    }


    public void init(final Sections sections) {
        addStations(sections);
        addSections(sections);
    }

    private void addStations(Sections sections) {
        for (Station station : sections.getStations()) {
            subwayGraph.addVertex(station);
        }
    }

    private void addSections(Sections sections) {
        for (Section section : sections.getSections()) {
            subwayGraph.setEdgeWeight(subwayGraph.addEdge(section.getUpStation(), section.getDownStation()),
                    section.getDistance());
        }
    }

    public List<Station> findShortestPath(Station source, Station target) {
        DijkstraShortestPath pathFinder = new DijkstraShortestPath(subwayGraph);
        return pathFinder.getPath(source, target).getVertexList();
    }
}
