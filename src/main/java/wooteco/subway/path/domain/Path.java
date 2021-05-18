package wooteco.subway.path.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class Path {
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public static Path of (Sections sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        List<Station> stations = sections.getStations();
        for (Station station : stations) {
            graph.addVertex(station);
        }

        List<Section> sectionList = sections.getSections();
        for (Section section : sectionList) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }

        return new Path(new DijkstraShortestPath<>(graph));
    }

    public Path(DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath) {
        this.dijkstraShortestPath = dijkstraShortestPath;
    }

    public List<Station> shortestPath(Station from, Station to) {
        return dijkstraShortestPath.getPath(from, to).getVertexList();
    }

    public double shortestPathLength(Station from, Station to) {
        return dijkstraShortestPath.getPathWeight(from, to);
    }
}
