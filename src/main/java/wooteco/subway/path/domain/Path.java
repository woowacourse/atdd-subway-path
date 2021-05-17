package wooteco.subway.path.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class Path {
    private final Station source;
    private final Station target;
    private final List<Section> sections;
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public Path(Station source, Station target, List<Section> sections) {
        this.source = source;
        this.target = target;
        this.sections = sections;
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        setGraph();
        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private void setGraph() {
        for (Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            graph.addVertex(upStation);
            graph.addVertex(downStation);
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
        }
    }

    public List<Station> getShortestStations() {
        List<Station> stations = dijkstraShortestPath.getPath(source, target).getVertexList();
        return stations;
    }

    public int getShortestDistance() {
        double pathWeight = dijkstraShortestPath.getPath(source, target).getWeight();
        return (int) pathWeight;
    }
}
