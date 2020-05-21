package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.domain.Edge;
import wooteco.subway.admin.domain.EdgeType;
import wooteco.subway.admin.dto.SearchPathResponse;
import wooteco.subway.admin.service.PathService;

@RestController
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<SearchPathResponse> searchPath(
            @RequestParam String startStationName,
            @RequestParam String targetStationName,
            @RequestParam String type
    ) {
        return ResponseEntity.ok().body(pathService.searchPath(startStationName, targetStationName, EdgeType.of(type)));
    }
}
