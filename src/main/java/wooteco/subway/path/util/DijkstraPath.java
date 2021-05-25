package wooteco.subway.path.util;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

public class DijkstraPath {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> graph;

    public DijkstraPath(Sections sections) {
        this.graph = createGraph(sections);
    }


    private DijkstraShortestPath<Station, DefaultWeightedEdge> createGraph(Sections sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
            = new WeightedMultigraph(DefaultWeightedEdge.class);

        for (Section section : sections.getSections()) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
                section.getDistance());
        }
        return new DijkstraShortestPath<>(graph);
    }

    public List<StationResponse> findShortestRouteToStationResponse(Station sourceStation,
        Station targetStation) {
        return StationResponse.listOf(graph.getPath(sourceStation, targetStation).getVertexList());
    }

    public int findShortestDistance(Station sourceStation, Station targetStation) {
        return (int) graph.getPathWeight(sourceStation, targetStation);
    }
}
