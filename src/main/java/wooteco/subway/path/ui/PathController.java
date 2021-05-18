package wooteco.subway.path.ui;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;


@Controller
@RequestMapping("/paths")
public class PathController {

    private final StationService stationService;
    private final LineService lineService;

    public PathController(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    // TODO: 경로조회 기능 구현하기
    @GetMapping
    public ResponseEntity<PathResponse> findPath(@RequestParam long source, @RequestParam long target ) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph =
                new WeightedMultigraph<>(DefaultWeightedEdge.class);

        List<Line> lines = lineService.findLines();
        initPath(graph, lines);

        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);

        return shortestPath(graph, sourceStation, targetStation);
    }

    private ResponseEntity<PathResponse> shortestPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Station sourceStation, Station targetStation) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        List<StationResponse> stationResponses = StationResponse.listOf(
                dijkstraShortestPath.getPath(sourceStation, targetStation).getVertexList()
        );

        int distance = (int)dijkstraShortestPath.getPathWeight(sourceStation, targetStation);

        PathResponse pathResponse = new PathResponse(stationResponses, distance);
        return ResponseEntity.ok().body(pathResponse);
    }

    private void initPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Line> lines) {
        for(Line line : lines) {
            Sections sections = line.getSections();
            setPath(graph, sections);
        }
    }

    private void setPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Sections sections) {
        for(Section section : sections.getSections()) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),section.getDistance());
        }
    }
}
