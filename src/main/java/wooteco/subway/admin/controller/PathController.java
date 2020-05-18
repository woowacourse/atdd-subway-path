package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.controller.validator.PathValidator;
import wooteco.subway.admin.domain.strategy.DijkstraStrategy;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.PathService;

@RequestMapping("/api/paths")
@RestController
public class PathController {
    private final PathService pathService;
    private final PathValidator pathValidator;

    public PathController(PathService pathService,
        PathValidator pathValidator) {
        this.pathService = pathService;
        this.pathValidator = pathValidator;
    }

    @GetMapping()
    public ResponseEntity<PathResponse> findShortestPath(PathRequest pathRequest) {
        pathValidator.valid(pathRequest);

        PathResponse pathResponse = pathService.findShortestPath(pathRequest, new DijkstraStrategy());

        return ResponseEntity
            .ok()
            .body(pathResponse);
    }
}
