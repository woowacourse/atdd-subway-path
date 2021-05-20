package wooteco.subway.path.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class Graph {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph
            = new WeightedMultigraph(DefaultWeightedEdge.class);
    private final DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

    public void initialize(List<Section> sections) {
        for (Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            graph.addVertex(upStation);
            graph.addVertex(downStation);
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
        }
    }

    public List<Station> shortestPath(Station sourceStation, Station targetStation) {
        GraphPath path = dijkstraShortestPath.getPath(sourceStation, targetStation);
        return path.getVertexList();
    }

    public int shortestDistance(Station sourceStation, Station targetStation) {
        GraphPath path = dijkstraShortestPath.getPath(sourceStation, targetStation);
        return (int) path.getWeight();
    }

    public void addSectionInfo(Station upStation, Station downStation, int distance) {
        graph.addVertex(upStation);
        graph.addVertex(downStation);
        graph.setEdgeWeight(graph.addEdge(upStation, downStation), distance);
    }
}
