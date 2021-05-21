package wooteco.subway.path.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayMap {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public SubwayMap() {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    public Path getShortestPath(Station source, Station target) {
        List<Station> stations = dijkstraShortestPath.getPath(source, target).getVertexList();
        int distance = (int) dijkstraShortestPath.getPathWeight(source, target);

        return new Path(stations, distance);
    }

    public void initPath(List<Line> lines) {
        for (Line line : lines) {
            Sections sections = line.getSections();
            setPath(sections);
        }
    }

    private void setPath(Sections sections) {
        for (Section section : sections.getSections()) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }
}
