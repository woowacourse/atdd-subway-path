package wooteco.subway.admin.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.dto.req.PathRequest;
import wooteco.subway.admin.dto.res.PathResponse;
import wooteco.subway.admin.service.PathService;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(
        @Valid @RequestParam("source") String source,
        @Valid @RequestParam("target") String target,
        @Valid @RequestParam("criteria") String criteria) {
        PathRequest pathRequest = new PathRequest(source, target, criteria);
        PathResponse pathResponse = pathService.showPaths(pathRequest);
        return ResponseEntity.ok(pathResponse);
    }
}
