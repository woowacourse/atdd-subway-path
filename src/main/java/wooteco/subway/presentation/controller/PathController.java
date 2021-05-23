package wooteco.subway.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.application.PathService;
import wooteco.subway.presentation.dto.response.PathResponse;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> getPath(@RequestParam Long source, @RequestParam Long target) {
        PathResponse pathResponse = pathService.shortestPath(source, target);
        return ResponseEntity.ok(pathResponse);
    }
}
