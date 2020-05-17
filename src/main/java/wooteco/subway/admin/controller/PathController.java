package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.dto.path.PathRequest;
import wooteco.subway.admin.dto.path.PathRequestWithId;
import wooteco.subway.admin.dto.path.PathResponse;
import wooteco.subway.admin.service.GraphService;
import wooteco.subway.admin.service.PathService;
import wooteco.subway.admin.service.StationService;

import java.util.List;

@RestController
public class PathController {
    private final PathService pathService;
    private final StationService stationService;
    private final GraphService graphService;

    public PathController(PathService pathService, StationService stationService, GraphService graphService) {
        this.pathService = pathService;
        this.stationService = stationService;
        this.graphService = graphService;
    }

    @PostMapping("/path")
    ResponseEntity<PathResponse> findPath(@RequestBody PathRequest pathRequest){
        PathRequestWithId PathRequestWithId = stationService.toPathRequestWithId(pathRequest);
        List<Long> pathFormedId = graphService.findPathFormedId(PathRequestWithId);
        PathResponse pathResponse = pathService.findPath(pathFormedId);
        return ResponseEntity.ok(pathResponse);
    }
}
