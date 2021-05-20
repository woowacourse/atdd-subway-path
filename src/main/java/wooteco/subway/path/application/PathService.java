package wooteco.subway.path.application;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

@Service
public class PathService {

    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findPath(final Long sourceId, final Long targetId) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        List<Line> lines = lineService.findLines();
        for(Line line : lines){
            for(Section section : line.getSections().asList()){
                graph.addVertex(section.getUpStation());
                graph.addVertex(section.getDownStation());
                DefaultWeightedEdge edge = graph.addEdge(section.getUpStation(), section.getDownStation());
                graph.setEdgeWeight(edge, section.getDistance());
            }
        }

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);

        List<Station> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();
        int totalDistance = (int)dijkstraShortestPath.getPathWeight(source, target);

        return new PathResponse(StationResponse.listOf(shortestPath), totalDistance);
    }
}
