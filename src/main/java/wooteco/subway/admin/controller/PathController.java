package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.PathService;

@RestController
@RequestMapping("/path")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/source/{sourceId}/target/{targetId}")
    public ResponseEntity<PathResponse> findPath(@PathVariable String sourceId,
        @PathVariable String targetId) {
        PathResponse pathResponse = pathService.showPaths(station1.getId(), station3.getId());
        return ResponseEntity.ok(pathResponse);
    }
}
