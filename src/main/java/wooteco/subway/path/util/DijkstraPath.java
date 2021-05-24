package wooteco.subway.path.util;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

public class DijkstraPath {

    private final Station sourceStation;
    private final Station targetStation;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> graph;

    public DijkstraPath(Station sourceStation, Station targetStation,
        Sections sections) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
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

    private List<StationResponse> findShortestRouteToStationResponse(
        StationService stationService) {
        List<StationResponse> stationResponses = new ArrayList<>();
        List<Station> shortestPaths = graph.getPath(sourceStation, targetStation).getVertexList();

        for (Station station : shortestPaths) {
            stationResponses.add(new StationResponse(stationService.findIdByName(station.getName()),
                station.getName()));
        }
        return stationResponses;
    }

    public List<StationResponse> findShortestPath(StationService stationService) {
        List<StationResponse> stationResponses = new ArrayList<>();
        List<Station> shortestPaths = graph.getPath(sourceStation, targetStation).getVertexList();

        for (Station station : shortestPaths) {
            stationResponses.add(new StationResponse(stationService.findIdByName(station.getName()),
                station.getName()));
        }
        return stationResponses;
    }

    public int findShortestDistance() {
        return (int) graph.getPathWeight(sourceStation, targetStation);
    }
}
