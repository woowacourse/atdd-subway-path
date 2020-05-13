package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.PathService;

@RequestMapping("/paths")
@RestController
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@RequestParam String source, @RequestParam String target) {
        PathResponse pathResponse = pathService.findShortestPath(source, target);
        return ResponseEntity.ok()
                .eTag(String.valueOf(pathResponse.hashCode()))
                .body(pathResponse);
    }
}
