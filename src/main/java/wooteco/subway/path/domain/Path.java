package wooteco.subway.path.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.exception.PathException;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class Path {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public Path(Sections sections) {
        validateSections(sections);
        this.graph = createGraph(sections);
    }

    private void validateSections(Sections sections) {
        if(sections == null || sections.isEmpty()) {
            throw new PathException("구간이 존재하지 않습니다.");
        }
    }

    public List<Station> findPath(Station source, Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        try {
            return dijkstraShortestPath.getPath(source, target).getVertexList();
        } catch (IllegalArgumentException e) {
            throw new PathException("존재하지 않는 역입니다.");
        }
    }

    public int findDistance(Station source, Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        return (int) dijkstraShortestPath.getPathWeight(source, target);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> createGraph(Sections sections) {

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for(Station station : sections.getAllStations()) {
            graph.addVertex(station);
        }

        for(Section section : sections.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }

        return graph;
    }
}
