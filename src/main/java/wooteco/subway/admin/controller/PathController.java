package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.PathService;

@RequestMapping("/api/paths")
@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping()
    public ResponseEntity<PathResponse> findShortestPath(PathRequest pathRequest) {
        PathResponse pathResponse = pathService.findShortestPathByDistance(pathRequest);

        return ResponseEntity
            .ok()
            .body(pathResponse);
    }
}
