package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.dto.response.PathResponse;
import wooteco.subway.admin.service.PathService;

@RestController
@RequestMapping("/path")
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@RequestParam("source") Long sourceId, @RequestParam("target") Long targetId) {
        PathResponse response = pathService.findPath(sourceId, targetId);
        return ResponseEntity.ok(response);
    }
}
