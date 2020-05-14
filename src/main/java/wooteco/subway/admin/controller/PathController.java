package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.dto.ShortestDistanceResponse;
import wooteco.subway.admin.service.PathService;

@RequestMapping("/paths")
@RestController
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<ShortestDistanceResponse> findShortestDistancePath(
        @RequestParam("source") String source,
        @RequestParam("target") String target,
        @RequestParam("type") String type) {
        ShortestDistanceResponse shortestDistanceResponse = pathService.searchShortestDistancePath(
            source, target, type);
        return ResponseEntity.ok().body(shortestDistanceResponse);
    }
}
